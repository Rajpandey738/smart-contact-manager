package com.scm.scm20.repositories;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
//import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {
        int countByUser(User user);

        // List<Contact> findByUser(User user);
        // Paginationa apply new version
        Page<Contact> findByUser(User user, Pageable pageable);

        Optional<Contact> findByContactId(String contactId);

        Optional<Contact> findByContactIdAndUser(String contactId, User user);

        @Query("""
                        SELECT c
                        FROM contacts c
                        WHERE c.user = :user
                        AND (
                        LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR
                        LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR
                        c.phoneNumber LIKE CONCAT('%', :keyword, '%')
                        )
                        """)
        Page<Contact> searchContacts(User user, String keyword, Pageable pageable);

        Page<Contact> findByUserAndFavoriteTrue(User user, Pageable pageable);
}
