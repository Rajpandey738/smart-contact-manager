package com.scm.scm20.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
//import java.util.List;

import com.scm.scm20.helper.Helper;
import com.scm.scm20.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;
import com.scm.scm20.forms.ProfileForm;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.DashboardService;
import com.scm.scm20.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    // user dashboard
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userDashboard(Authentication authentication, Model model) {

        User user = (User) authentication.getPrincipal();
        int totalContacts = dashboardService.getTotalContactsForUser(user);

        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        System.out.println("Total Contacts = " + totalContacts);
        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("currentTime", currentTime);
        model.addAttribute("activePage", "dashboard");
        return "user/dashboard";
    }

    // user profile
    @GetMapping("/profile")
    public String profileInfo(Model model,
            Authentication authentication) {

        User loggedInUser = (User) authentication.getPrincipal();

        User dbUser = userRepo
                .findById(loggedInUser.getUserId())
                .orElseThrow();

        ProfileForm profileForm = ProfileForm.builder()
                .name(dbUser.getName())
                .phoneNumber(dbUser.getPhoneNumber())
                .address(dbUser.getAddress())
                .profilePic(dbUser.getProfilePic())
                .build();

        model.addAttribute("profileForm", profileForm);
        model.addAttribute("activePage", "profile");

        return "user/profile";
    }

    // EDIT PROFILE
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute ProfileForm profileForm, Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) authentication.getPrincipal();
        userService.updateProfile(profileForm, loggedInUser);

        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "green");

        return "redirect:/user/profile";
    }

    // change profile pic
    @PostMapping("/profile/upload-image")
    public String uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            Authentication authentication) {

        System.out.println("File Name: " + file.getOriginalFilename());

        return "redirect:/user/profile";
    }

    // Add Contact
    @GetMapping("/add-contact")
    public String addContact(Authentication authentication, Model model) {

        model.addAttribute("contactForm", new ContactForm());
        model.addAttribute("activePage", "add-contact");
        return "user/add-contact";
    }

    @PostMapping("/add-contact")
    public String saveContact(@ModelAttribute ContactForm contactForm, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        // Logic to save contact using the data from contactForm
        // You can access the authenticated user if needed
        User user = (User) authentication.getPrincipal();

        Contact contact = new Contact();

        contact.setContactId(UUID.randomUUID().toString());

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhone());
        contact.setAddress(contactForm.getAddress());
        contact.setProfilePic(contactForm.getProfilePic());
        contact.setDescription(contactForm.getDescription());

        contact.setUser(user);

        // Call a service method to save the contact for the user
        contactService.saveContact(contact);

        redirectAttributes.addFlashAttribute("message", "Contact added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "green");

        // Redirect to a success page or back to the add contact form
        return "redirect:/user/add-contact?success";
    }

    // View Contacts
    @GetMapping("/view-contacts")
    public String viewContacts(Model model, Authentication authentication,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "all") String filter) {

        User loggedInUser = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Contact> contactPage;

        if ("favorite".equals(filter)) {
            contactPage = contactService.getFavoriteContacts(loggedInUser, pageable);
        } else {
            if (keyword != null && !keyword.trim().isEmpty()) {
                contactPage = contactService.searchContacts(loggedInUser, keyword, page, 5);
            } else {
                contactPage = contactService.getContactsByUser(loggedInUser, page, 5);
            }
        }

        model.addAttribute("contacts", contactPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactPage.getTotalPages());
        model.addAttribute("activePage", "view-contacts");
        model.addAttribute("keyword", keyword);
        model.addAttribute("filter", filter);
        return "user/view-contacts";
    }

    // View Contact Details
    @GetMapping("/contact/{contactId}")
    public String viewContactDetails(@PathVariable String contactId, Model model, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) authentication.getPrincipal();
        Contact contact = contactService.getContactByIdAndUser(contactId, loggedInUser).orElse(null);

        // old approach
        // if (!contact.getUser().getUserId().equals(loggedInUser.getUserId())) {
        // redirectAttributes.addFlashAttribute("message", "Unauthorized access! You
        // cannot view this contact.");
        // redirectAttributes.addFlashAttribute("messageType", "red");
        // return "redirect:/user/view-contacts";
        // // throw new RuntimeException("Unauthorized Access");
        // }

        // new approach
        if (contact == null) {
            redirectAttributes.addFlashAttribute("message", "Contact not found!");
            redirectAttributes.addFlashAttribute("messageType", "red");
            return "redirect:/user/view-contacts";
        }

        model.addAttribute("contact", contact);
        model.addAttribute("activePage", "view-contacts");
        return "user/contact-details";
    }

    // edit contact
    @GetMapping("/edit-contact/{contactId}")
    public String editContact(@PathVariable String contactId,
            @RequestParam(defaultValue = "view-contacts") String source, Model model, Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) authentication.getPrincipal();
        Contact contact = contactService.getContactByIdAndUser(contactId, loggedInUser).orElse(null);
        if (contact == null) {
            redirectAttributes.addFlashAttribute("message", "Contact not found!");
            redirectAttributes.addFlashAttribute("messageType", "red");
            return "redirect:/user/view-contacts";
        }

        ContactForm contactForm = new ContactForm();
        contactForm.setContactId(contact.getContactId());
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhone(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setProfilePic(contact.getProfilePic());
        contactForm.setDescription(contact.getDescription());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("source", source);
        model.addAttribute("activePage", "view-contacts");
        return "user/edit-contact";
    }

    // edit contact post mapping
    @PostMapping("/update-contact")
    public String updateContact(@ModelAttribute ContactForm contactForm, @RequestParam String source,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User loggedInUser = userService.getUserByEmail(email);
        contactService.updateContact(contactForm, loggedInUser);
        redirectAttributes.addFlashAttribute("message", "Contact updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "green");
        if ("contact-details".equals(source)) {
            return "redirect:/user/contact/" + contactForm.getContactId();
        }
        return "redirect:/user/view-contacts";
    }

    // Delete Contact
    @GetMapping("/delete-contact/{contactId}")
    public String deleteContact(@PathVariable String contactId, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) authentication.getPrincipal();
        Contact contact = contactService.getContactByIdAndUser(contactId, loggedInUser).orElse(null);

        // deleting contact
        contactService.deleteContact(contact.getContactId());

        redirectAttributes.addFlashAttribute("message", "Contact deleted successfully");
        redirectAttributes.addFlashAttribute("messageType", "green");

        return "redirect:/user/view-contacts";

    }

    // Favorite contact
    @GetMapping("/contact/favorite/{contactId}")
    public String toggleFavorite(@PathVariable String contactId, Authentication authentication) {
        User loggedInUser = (User) authentication.getPrincipal();
        contactService.toggleFavorite(contactId, loggedInUser);

        return "redirect:/user/view-contacts";
    }

}