package com.scm.scm20.services.impl;

import com.scm.scm20.controller.PageController;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;
import com.scm.scm20.repositories.ContactRepo;
import com.scm.scm20.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepo.save(contact);
    }

    // @Override
    // public List<Contact> getContactsByUser(User user) {
    // return contactRepo.findByUser(user);
    // }
    @Override
    public Page<Contact> getContactsByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Optional<Contact> getContactById(String contactId) {
        return contactRepo.findByContactId(contactId);
    }

    @Override
    public Optional<Contact> getContactByIdAndUser(String contactId, User user) {
        return contactRepo.findByContactIdAndUser(contactId, user);
    }

    @Override
    public void updateContact(ContactForm contactForm, User user) {
        // Implementation for updating contact
        Contact existingContact = contactRepo.findByContactIdAndUser(contactForm.getContactId(), user).orElse(null);
        if (existingContact != null) {
            existingContact.setName(contactForm.getName());
            existingContact.setEmail(contactForm.getEmail());
            existingContact.setPhoneNumber(contactForm.getPhone());
            existingContact.setAddress(contactForm.getAddress());
            existingContact.setProfilePic(contactForm.getProfilePic());
            existingContact.setDescription(contactForm.getDescription());
            contactRepo.save(existingContact);
        }
    }

    @Override
    public void deleteContact(String contactId) {
        contactRepo.deleteById(contactId);
    }

    // @Override
    // public List<Contact> searchContacts(User user, String keyword) {
    // return contactRepo.searchContacts(user, keyword);
    // }
    @Override
    public Page<Contact> searchContacts(User user, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return contactRepo.searchContacts(user, keyword, pageable);
    }

    @Override
    public void toggleFavorite(String contactId, User user) {
        Contact contact = contactRepo.findByContactIdAndUser(contactId, user)
                .orElseThrow(() -> new RuntimeException("Contact not found!"));
        contact.setFavorite(!contact.isFavorite());
        contactRepo.save(contact);
    }

    @Override
    public Page<Contact> getFavoriteContacts(User user, Pageable pageable) {
        return contactRepo.findByUserAndFavoriteTrue(user, pageable);
    }

}
