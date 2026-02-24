package com.example.mahjong.logic.checks;

import com.example.mahjong.logic.util.ComparisonHelperFunctions;
import com.example.mahjong.logic.util.HelperFunctions;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Chow;
import com.example.mahjong.model.tiles.SuitedTile;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.ChowService;
import com.example.mahjong.service.PlayerService;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChowsChecks {

    @Autowired
    private HelperFunctions h;
    @Autowired
    PlayerService playerService;
    @Autowired
    private ChowService chowService;
    @Autowired
    private TileService tileService;
    @Autowired
    private ComparisonHelperFunctions c;

    boolean v = false;

    //Just tries to get tiles which would let the new tile fit into a chow instead of looking through the hand
    /**
     * Just finds a chow and returns the player with it, creates/saves nothing
     * @deprecated Does not working going from player 4 to player 1
     */
    public List<Tile> lookForChowsAfterDiscard(Player player, Tile tile) {
        if (!(tile instanceof SuitedTile)) return null;

        List<Tile> playerTiles = player.getCurrentHandNoPlaced();

        //Broken??????

        //2 before and 1 before, 1 before and 1 after, 1 after and 2 after
        HashMap<Integer, List<Integer>> offsets = new HashMap<>(Map.of(0, List.of(-2, -1), 1, List.of(-1, 1), 2, List.of(1, 2)));
        for (int count = 0; count <= 2; count++) {
            Integer of1 = offsets.get(count).getFirst();
            Integer of2 = offsets.get(count).getLast();
            List<Tile> pattern = findTilesByValue(playerTiles, tile.getSuit(), tile.getNumber() + of1, tile.getNumber() + of2);
            if (pattern != null) {
                pattern.add(tile); // Add the discard to complete it
                return pattern;
                //if(handleChow(player, pattern)) return player;
            }
        }
        return null;
    }

    /**
     * Start of game check - scan entire hand for any valid Chows
     * @return Player - Returns the player if a valid chow is found and saved
     */
    public Player lookForChows(Player player, List<Tile> playerTiles) {
        List<Tile> sortedTiles = new ArrayList<>(playerTiles);
        sortedTiles.sort(c.getTileLogicComparator());

        List<List<Tile>> foundChows = new ArrayList<>();
        List<Tile> consumedTiles = new ArrayList<>();

        for (Tile t : sortedTiles) {
            if (consumedTiles.contains(t)) continue; // Already used this tile in another chow
            if (!(t instanceof SuitedTile)) continue;

            Optional<Tile> t1 = findTileInList(playerTiles, t.getSuit(), t.getNumber() + 1, consumedTiles);
            Optional<Tile> t2 = findTileInList(playerTiles, t.getSuit(), t.getNumber() + 2, consumedTiles);

            if (t1.isPresent() && t2.isPresent()) {
                List<Tile> chow = new ArrayList<>(List.of(t, t1.get(), t2.get()));
                foundChows.add(chow);
                consumedTiles.add(t);
                consumedTiles.add(t1.get());
                consumedTiles.add(t2.get());
            }
        }
        boolean validChowSaved = false;
        for (List<Tile> chowTiles : foundChows) {
            List<Integer> tileIds = chowTiles.stream().map(Tile::getTileId).toList();
            if (handleChow(player, tileIds)) validChowSaved = true;
        }
        return validChowSaved ? player : null;
    }

    private List<Tile> findTilesByValue(List<Tile> playerTiles, String suit, int n1, int n2) {
        Optional<Tile> t1 = findTileInList(playerTiles, suit, n1, new ArrayList<>());
        Optional<Tile> t2 = findTileInList(playerTiles, suit, n2, new ArrayList<>());

        if (t1.isPresent() && t2.isPresent()) {
            return new ArrayList<>(List.of(t1.get(), t2.get()));
        }
        return null;
    }

    private Optional<Tile> findTileInList(List<Tile> tiles, String suit, int number, List<Tile> exclude) {
        return tiles.stream()
                .filter(t -> !exclude.contains(t))
                .filter(t -> Objects.equals(t.getSuit(), suit) && Objects.equals(t.getNumber(), number))
                .findFirst();
    }

    public boolean handleChow(Player player, List<Integer> chowTileIds) {
        List<Tile> chowTiles = chowTileIds.stream().map(t -> tileService.findTileById(t)).toList();
        System.out.println("tiles should be correct here: " + chowTiles);
        if (chowService.lookInPlayerForChowWithSameTiles(player, chowTiles)) {
            if (v) System.out.println("Duplicate Chow");
            return false;
        }
        Chow newChow = chowService.createChow();
        for (Tile tile : chowTiles) newChow = chowService.addTileToChow(newChow.getChowId(), tile);
        playerService.addChow(player.getPlayerId(), newChow);
        System.out.println("Found valid chow: " + newChow.getTiles());
        return true;
    }


}
