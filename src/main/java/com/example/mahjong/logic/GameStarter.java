package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.GameService;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GameStarter {
    @Autowired
    private TileService tileService;
    @Autowired
    public GameService gameService;

    public void initaliseGame() {
        Game game = gameService.createGame();
        createTiles(game);
    }

    public void createTiles(Game game) {
        // Suited (108 total > 3 suits * 9 numbers * 4 copies)
        List<String> suits = List.of("Bamboo", "Dots", "Characters");
        createSubsetOfTiles("Suited", suits, 9, 4, game);

        // Honor (28 total > 7 types * 4 copies)
        List<String> honors = List.of("North", "South", "East", "West", "Red", "Green", "White");
        createSubsetOfTiles("Honor", honors, 1, 4, game);

        // Flower (8 total > 2 sets of 4)
        List<String> flowers = List.of("Flower");
        createSubsetOfTiles("Flower", flowers, 4, 1, game);

    }

    public void createSubsetOfTiles(String type, List<String> suits, Integer totalCount, Integer totalCopies, Game game) {
        for (String suit : suits) {
            for (int num = 1; num <= totalCount; num++) {
                for (int copy = 0; copy < totalCopies; copy++) {
                    if (Objects.equals(type, "Flower")) System.out.println(suits);
                    Tile t = tileService.createTile(type, suit, num, game);
                    gameService.addTile(game, t);
                }
            }
        }
    }
}
