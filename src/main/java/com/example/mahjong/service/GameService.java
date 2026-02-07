package com.example.mahjong.service;

import com.example.mahjong.exception.GameCreationFailedException;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository; //The game repository for interacting with the database
    public GameService(GameRepository gameRepository) { //Initialises the user Repository
        this.gameRepository = gameRepository;
    }

    public Game createGame() { //Creates a user object and saves it to the database through the repository
        try {
            Game game = new Game();
            gameRepository.save(game);
            return game;
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to initialize a new Mahjong game", e);
        }
    }

    public void addTile(Game game, Tile tile) {
        try {
            game.addTile(tile);
            gameRepository.save(game);
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to add a new tile");
        }
    }

    public Game findFirstGame() {
        try {
            return gameRepository.findFirstByGameId(1);
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to add a new tile");
        }
    }

}