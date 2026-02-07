package com.example.mahjong.service;

import com.example.mahjong.exception.GameCreationFailedException;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.repository.GameRepository;
import com.example.mahjong.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository; //The player repository for interacting with the database
    public PlayerService(PlayerRepository playerRepository) { //Initialises the user Repository
        this.playerRepository = playerRepository;
    }

    public void addTile(Player player, Tile tile) {
        try {
            player.addTile(tile);
            playerRepository.save(player);
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to add a new tile to player"); //Update exception
        }
    }
}
