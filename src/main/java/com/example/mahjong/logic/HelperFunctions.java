package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.tiles.FlowerTile;
import com.example.mahjong.model.tiles.HonorTile;
import com.example.mahjong.model.tiles.SuitedTile;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.PlayerService;
import com.example.mahjong.service.TileService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HelperFunctions {

    @Autowired
    private TileService tileService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ComparisonHelperFunctions c;

    boolean v = false;

    public Tile getRandomTile(Game game) {
        List<Tile> allTiles = tileService.getLiveTiles(game); //Only unused ones
        Random r = new Random();
        return allTiles.get(r.nextInt(allTiles.size()));
    }

    public Tile addTileToPlayer(Player player, Tile tile) {
        return playerService.addTileToHand(player.getPlayerId(), tile);
    }

    public Tile addRandomTileToPlayer(Game game, Player player) {
        return playerService.addTileToHand(player.getPlayerId(), getRandomTile(game));
    }

    public List<Tile> simulateHandWithNewTileForChecks(Player player, Tile tile) {
        if (v) System.out.println("Simulating new hand for checks");
        List<Tile> playerTiles = new ArrayList<>(player.getCurrentHandNoPlaced());
        playerTiles.add(tile);
        playerTiles.sort(c.getTileLogicComparator());
        return playerTiles;
    }

    public Player iterateThroughListWithLooping(List<Player> players, Player playerTurn) {
        if (players == null || players.isEmpty()) {
            if (v) System.out.println("Players list empty");
            return null;
        }

        int currentIndex = players.indexOf(playerTurn);
        if (currentIndex == -1) currentIndex = 0;
        int nextIndex = (currentIndex + 1) % players.size(); //Ensures looping
        return players.get(nextIndex);
    }

}
