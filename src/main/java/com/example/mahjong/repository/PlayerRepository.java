package com.example.mahjong.repository;

import com.example.mahjong.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByUsername(String username); //Returns a user with a matching username

    List<Player> findAllByRole(String role);
}
