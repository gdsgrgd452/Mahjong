package com.example.mahjong.service;

import com.example.mahjong.exception.GameCreationFailedException;
import com.example.mahjong.exception.GetFromDatabaseFailedException;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository; //The player repository for interacting with the database
    public PlayerService(PlayerRepository playerRepository) { //Initialises the user Repository
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers() {
        try {
            return playerRepository.findAllByRole("USER"); //Needs to be updated at some point
        } catch (Exception e) {
            throw new GetFromDatabaseFailedException("Failed to get player from database", e);
        }
    }

    public Player findPlayerById(Integer playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new GetFromDatabaseFailedException("Player not found with ID: " + playerId));
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
