package com.example.mahjong.service;

import com.example.mahjong.exception.GameCreationFailedException;
import com.example.mahjong.exception.GetFromDatabaseFailedException;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Chow;
import com.example.mahjong.model.actions.Pung;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository; //The player repository for interacting with the database
    public PlayerService(PlayerRepository playerRepository) { //Initialises the user Repository
        this.playerRepository = playerRepository;
    }

    public Player savePlayer(Player player) {
        try {
            playerRepository.save(player);
            return player;
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to save an existing player", e);
        }
    }

    public List<Player> findAllPlayers() {
        try {
            return playerRepository.findAll(); //Needs to be updated at some point
        } catch (Exception e) {
            throw new GetFromDatabaseFailedException("Failed to get any players from database: ", e);
        }
    }

    public List<Player> findAllPlayersByGame(Game game) {
        try {
            return playerRepository.findAllByGame(game); //Needs to be updated at some point
        } catch (Exception e) {
            throw new GetFromDatabaseFailedException("Failed to get players from database for game id: ", e);
        }
    }

    public Player findPlayerById(Integer playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new GetFromDatabaseFailedException("Player not found with ID: " + playerId));
    }

    public Player findPlayerWithActionToTake() {
        return playerRepository.findPlayerByActionToTakeIsNotNull();
    }

    public Tile addTileToHand(Integer playerId, Tile tile) {
        Player player = findPlayerById(playerId);
        player.addTile(tile);
        playerRepository.save(player);
        return tile;
    }

    public void removeTileFromHand(Integer playerId, Tile tile) {
        Player player = findPlayerById(playerId);
        player.removeTile(tile);
        playerRepository.save(player);
    }

    public Player addPung(Integer playerId, Pung newPung) {
        Player player = findPlayerById(playerId);
        player.addPung(newPung);
        player = playerRepository.save(player);
        return player;
    }

    //Consistent
    public void removePungFromPlayer(Integer playerId, Pung newPung) {
        Player player = findPlayerById(playerId);
        player.removePung(newPung);
        playerRepository.save(player);
    }

    public Player addChow(Integer playerId, Chow newChow) {
        Player player = findPlayerById(playerId);
        player.addChow(newChow);
        player = playerRepository.save(player);
        return player;
    }
}
