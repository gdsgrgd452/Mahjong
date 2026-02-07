package com.example.mahjong.service;

import com.example.mahjong.model.Player;
import com.example.mahjong.repository.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetails implements UserDetailsService {
    private PlayerRepository playerRepository; //User repository for interacting with the database
    public UserDetails(PlayerRepository playerRepository) { //Initialises the user repository
        this.playerRepository = playerRepository;
    }
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Loads a user by username
        Player player = playerRepository.findByUsername(username);
        if (player == null) { //If the user is not found throw an error
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(player.getUsername()).password(player.getPassword()).roles(player.getRole()).build(); //Returns a User object
    }
}