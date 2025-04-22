package com.synchrony.photo_service.services;

import com.synchrony.photo_service.models.user.UserProfile;
import com.synchrony.photo_service.repos.UserProfileRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {
    private final UserProfileRepo userProfileRepo;

    public AuthUserService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(userProfile.getUsername())
            .password(userProfile.getPassword())
            .roles("USER")
            .build();
    }
}
