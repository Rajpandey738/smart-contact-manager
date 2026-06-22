package com.scm.scm20.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.User;
import com.scm.scm20.repositories.ContactRepo;
import com.scm.scm20.services.DashboardService;

@Service
public class DashboardServiceImp implements DashboardService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public int getTotalContactsForUser(User user) {
        return contactRepo.countByUser(user);
    }

}
