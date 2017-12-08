package edu.tamu.cap.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.tamu.cap.auth.model.CustomUserDetails;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.weaver.auth.service.AbstractWeaverUserDetailsService;

@Service
public class AppUserDetailsService extends AbstractWeaverUserDetailsService<User, UserRepo> {

    @Override
    public UserDetails buildUserDetails(User user) {
        return new CustomUserDetails(user);
    }

}
