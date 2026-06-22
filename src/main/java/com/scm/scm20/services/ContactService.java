package com.scm.scm20.services;

//import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;

public interface ContactService {

    Contact saveContact(Contact contact);

    // List<Contact> getContactsByUser(User user);
    Page<Contact> getContactsByUser(User user, int page, int size); // pagination version

    Optional<Contact> getContactById(String contactId);

    Optional<Contact> getContactByIdAndUser(String contactId, User user);

    void updateContact(ContactForm contactForm, User user);

    void deleteContact(String contactId);

    // List<Contact> searchContacts(User user, String keyword);
    Page<Contact> searchContacts(User user, String keyword, int page, int size);

    // Favorite contact list
    void toggleFavorite(String contactId, User user);

    Page<Contact> getFavoriteContacts(User user, Pageable pageable);
}
