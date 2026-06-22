package com.scm.scm20.services;

import java.util.List;
import java.util.Optional;

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ProfileForm;

public interface UserService {
    User saveUser(User user);

    Optional<User> getUserById(String Id);

    Optional<User> updateUser(User user);

    void deleteUser(String Id);

    boolean existsByEmail(String email);

    boolean isUserExist(String Id);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    void updateProfile(ProfileForm profileForm, User user);

}