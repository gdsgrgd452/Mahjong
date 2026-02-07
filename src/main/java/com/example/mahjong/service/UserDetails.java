package com.example.mahjong.service;

import com.example.mahjong.model.User;
import com.example.mahjong.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetails implements UserDetailsService {
    private UserRepository userRepository; //User repository for interacting with the database
    public UserDetails(UserRepository userRepository) { //Initialises the user repository
        this.userRepository = userRepository;
    }
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Loads a user by username
        User user = userRepository.findByUsername(username);
        if (user == null) { //If the user is not found throw an error
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build(); //Returns a User object
    }
}