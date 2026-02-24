package com.example.mahjong.logic.checks;

import com.example.mahjong.logic.util.ComparisonHelperFunctions;
import com.example.mahjong.logic.util.HelperFunctions;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Pung;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.PlayerService;
import com.example.mahjong.service.PungService;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PungsChecks {


    @Autowired
    private HelperFunctions h;
    @Autowired
    PlayerService playerService;
    @Autowired
    private PungService pungService;
    @Autowired
    private TileService tileService;
    @Autowired
    private ComparisonHelperFunctions c;

    boolean v = false;




    public Player lookForPungs(Player player, List<Tile> playerTiles) {
        Map<String, List<Tile>> grouped = c.groupTilesBySuitAndNumber(playerTiles);
        boolean validPungSaved = false;

        for (List<Tile> group : grouped.values()) {
            if (group.size() >= 3) {
                List<Tile> pungTiles = group.subList(0, 3);
                List<Integer> tileIds = pungTiles.stream().map(Tile::getTileId).toList();
                if (handlePung(player, tileIds)) validPungSaved = true;
            }
        }
        return validPungSaved ? player : null;
    }

    /**
     * Just finds a pung and returns the player with it, creates/saves nothing
     */
    public List<Tile> lookForPungsAfterDiscard(Player player, Tile tile) {
        List<Tile> playerTiles = h.simulateHandWithNewTileForChecks(player, tile);
        List<Tile> matchingTiles = playerTiles.stream().filter(t -> c.sameTypeNumberAndSuitCheck(t, tile)).toList();

        if (matchingTiles.size() >= 3) {
            List<Tile> pungTiles = new ArrayList<>();
            pungTiles.add(matchingTiles.get(0));
            pungTiles.add(matchingTiles.get(1));
            pungTiles.add(tile);
            if (!pungService.lookInPlayerForPungWithSameTiles(player, pungTiles)) return pungTiles;
        }
        return null;
    }

    public boolean handlePung(Player player, List<Integer> pungTilesIds) {
        List<Tile> pungTiles = pungTilesIds.stream().map(t -> tileService.findTileById(t)).toList();
        System.out.println("tiles should be correct here: " + pungTiles);
        if (pungService.lookInPlayerForPungWithSameTiles(player, pungTiles)) {
            return false;
        }
        Pung newPung = pungService.createPung();
        for (Tile tile : pungTiles) newPung = pungService.addTileToPung(newPung.getPungId(), tile);
        playerService.addPung(player.getPlayerId(), newPung);
        return true;
    }
}

