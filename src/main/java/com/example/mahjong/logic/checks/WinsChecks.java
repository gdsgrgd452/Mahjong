package com.example.mahjong.logic.checks;

import com.example.mahjong.logic.util.ComparisonHelperFunctions;
import com.example.mahjong.logic.util.HelperFunctions;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Pung;
import com.example.mahjong.model.tiles.FlowerTile;
import com.example.mahjong.model.tiles.Tile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WinsChecks {

    @Autowired
    HelperFunctions h;
    @Autowired
    ComparisonHelperFunctions c;


    //Broken because you need to check for new pungs and chows before checking for win
    public Player lookForWin(Player player, List<Tile> playerTiles) {

        Integer pungsCount = player.getPungs().size();
        Integer chowsCount = player.getChows().size();
        if (pungsCount + chowsCount != 4) return null; //Quick check for speed

        List<Tile> tilesNotInPungOrChow = playerTiles.stream().filter(this::tileIsNotInPungOrChow).toList();
        System.out.println("Tiles not in a pung or chow, should be 2");
        System.out.println(tilesNotInPungOrChow);

        if (tilesNotInPungOrChow.getFirst() == tilesNotInPungOrChow.getLast()) return player;
        return null;
    }

    public Player lookForWinAfterDiscard(Player player, Tile tile) {
        List<Tile> playerTiles = new ArrayList<>(h.simulateHandWithNewTileForChecks(player, tile));
        if (lookForWin(player, playerTiles) != null) return player;
        return null;
    }

    private boolean tileIsNotInPungOrChow(Tile tile) {
        return (tile.getChow() == null) && (tile.getPung() == null);
    }

    // Kong of Dragon tiles	1
    // Pung or kong of player’s Wind	1
    // Pung or kong of the Wint of the round	1
    // Flower or Season of player’s Wind	1
    // Two Dragon pungs or kongs and a pair of another Dragon	4
    // Seven pairs	4
    // Self-drawing the final tile	1
    // Win with the last tile from the Live Wall	1
    // Win with the last discard of the game	1
    // Win by stealing a kong	1
    // Win through replacement tile from Dead Wall	1

    /**
     * Goes through and calls all the different points checks
     * @return points - The total points for that player
     */
    private Integer calculatePoints(Player player) {
        List<String> dragonTypes = List.of("Green", "Red", "White");

        List<Pung> pungs = player.getPungs();
        List<Tile> currentHand = player.getCurrentHand();
        List<Tile> currentHandNoPlaced = player.getCurrentHandNoPlaced();

        Integer points = 0;

        points += pungsOfDragonTilesCheck(pungs, dragonTypes);
        points += noFlowersCheck(currentHand);
        points += allFlowersCheck(currentHand);
        points += bothSameSuitChecks(currentHand);
        points += fullChowHandCheck(player);
        points += fullPungHandCheck(player);

        return points;
    }


    /**
     * Chooses between the 2 checks for whether all tiles are 1 suit <br>
     */
    private Integer bothSameSuitChecks(List<Tile> currentHand) {
        Integer honorTilesCount = c.getOnlyHonorSuitedOrFlowerTiles(currentHand, "Honor").size();

        if (honorTilesCount == 0) { //No honor tiles so can check if all tiles are same suit
            return allSameSuitCheckNoHonor(currentHand);
        } else {
            return allSameSuitCheckWithHonor(currentHand);
        }
    }

    /**
     * Returns 6 if the player has only 1 suit of Suited tiles (No honor tiles), 0 otherwise
     * @deprecated current hand?
     */
    private Integer allSameSuitCheckNoHonor(List<Tile> currentHand) {
        List<Tile> suitedTiles = c.getOnlyHonorSuitedOrFlowerTiles(currentHand, "Suited");
        String suit = suitedTiles.getFirst().getSuit();

        Map<String, List<Tile>> tilesGroupedBySuit = c.groupTilesBySuit(currentHand);
        if (tilesGroupedBySuit.get(suit) == suitedTiles) return 6;
        return 0;
    }

    /**
     * Returns 3 if the player has only 1 suit of Suited tiles (And honor tiles), 0 otherwise
     * @deprecated current hand?
     */
    private Integer allSameSuitCheckWithHonor(List<Tile> currentHand) {
        List<Tile> suitedTiles = c.getOnlyHonorSuitedOrFlowerTiles(currentHand, "Suited");
        String suit = suitedTiles.getFirst().getSuit();

        Map<String, List<Tile>> tilesGroupedBySuit = c.groupTilesBySuit(currentHand);
        if (tilesGroupedBySuit.get(suit) == suitedTiles) return 3;
        return 0;
    }

    /**
     * @return pungOfDragonTilesCount - 1 point for each pung which is made up of dragons
     */
    private Integer pungsOfDragonTilesCheck(List<Pung> pungs, List<String> dragonTypes) {
        Integer pungOfDragonTilesCount = 0;

        for (Pung pung : pungs) {
            for (String type : dragonTypes) {
                if (pungTypeCheck(pung, type)) pungOfDragonTilesCount += 1;
            }
        }
        return pungOfDragonTilesCount;
    }

    /**
     * Returns 1 if there is no flowers, 0 if there is
     * @deprecated also does seasons later, current hand?
     */
    private Integer noFlowersCheck(List<Tile> currentHand) {
        for (Tile tile : currentHand) {
            if (tile instanceof FlowerTile) return 0;
        }
        return 1;
    }

    /**
     * Returns 2 if the player has all the flowers 1-4, 0 otherwise
     * @deprecated also does seasons later, current hand?
     */
    private Integer allFlowersCheck(List<Tile> currentHand) {
        List<Tile> flowerTiles = c.getOnlyHonorSuitedOrFlowerTiles(currentHand, "Flower");
        List<Integer> numbers = List.of(1, 2, 3, 4);

        for (Integer num : numbers) {
            if (flowerTiles.stream().noneMatch(t -> Objects.equals(t.getNumber(), num))) return 0;
        }
        return 2;
    }

    /**
     * Returns 1 if there is only chows and no pungs, 0 otherwise
     */
    private Integer fullChowHandCheck(Player player) {
        if (player.getPungs().isEmpty() && player.getChows().size() == 4) return 1;
        return 0;
    }

    /**
     * Returns 3 if there is only pungs and no chows, 0 otherwise
     */
    private Integer fullPungHandCheck(Player player) {
        if (player.getChows().isEmpty() && player.getPungs().size() == 4) return 3;
        return 0;
    }



    private boolean pungTypeCheck(Pung pung, String type) {
        Tile firstTile = pung.getTiles().getFirst();
        return Objects.equals(firstTile.getSuit(), type);
    }
}

