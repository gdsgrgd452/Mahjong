package com.example.mahjong.seeder;

import com.example.mahjong.model.Player;
import com.example.mahjong.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PlayerSeeder { //Class to add test users into the database when the application builds
    @Bean
    CommandLineRunner seedUsers(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Player player = new Player();
            String username = "PLAYER" + 1; //Adds a number to the user's username for identification
            String password = passwordEncoder.encode("password");
            player.setUsername(username);
            player.setPassword(password);
            player.setRole("USER");
            playerRepository.save(player);
        };
    }
}
