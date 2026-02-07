package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.GameService;
import com.example.mahjong.service.PlayerService;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class GameStarter {
    @Autowired
    private TileService tileService;
    @Autowired
    public GameService gameService;
    @Autowired
    public PlayerService playerService;
    private Game game;

    public void initialiseGame() {
        this.game = gameService.createGame();
        addPlayersToGame();
        createTiles();
        dealHandToEachPlayer();
    }

    public void addPlayersToGame() {
        playerService.findAllPlayers().forEach(p -> gameService.addPlayer(this.game, p));
    }

    public void createTiles() {
        // Suited (108 total > 3 suits * 9 numbers * 4 copies)
        List<String> suits = List.of("Bamboo", "Dots", "Characters");
        createSubsetOfTiles("Suited", suits, 9, 4);

        // Honor (28 total > 7 types * 4 copies)
        List<String> honors = List.of("North", "South", "East", "West", "Red", "Green", "White");
        createSubsetOfTiles("Honor", honors, 1, 4);

        // Flower (8 total > 2 sets of 4)
        List<String> flowers = List.of("Flower");
        createSubsetOfTiles("Flower", flowers, 4, 1);
    }

    public void createSubsetOfTiles(String type, List<String> suits, Integer totalCount, Integer totalCopies) {
        for (String suit : suits) {
            for (int num = 1; num <= totalCount; num++) {
                for (int copy = 0; copy < totalCopies; copy++) {
                    if (Objects.equals(type, "Flower")) System.out.println(suits);
                    Tile t = tileService.createTile(type, suit, num, this.game);
                    gameService.addTile(this.game, t);
                }
            }
        }
    }

    public void dealHandToEachPlayer() {
        playerService.findAllPlayers().forEach(this::dealHand);
    }

    public void dealHand(Player player) {
        for (int num = 0; num <= 13; num++) {
            playerService.addTile(player, getRandomTile());
        }
    }

    public Tile getRandomTile() {
        List<Tile> allTiles = tileService.getAllByGame(this.game);
        Random r = new Random();
        return allTiles.get(r.nextInt(allTiles.size()));
    }
}
