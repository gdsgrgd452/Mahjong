package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Pung;
import com.example.mahjong.model.tiles.FlowerTile;
import com.example.mahjong.model.tiles.HonorTile;
import com.example.mahjong.model.tiles.SuitedTile;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.PlayerService;
import com.example.mahjong.service.PungService;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Checks {

    @Autowired
    private HelperFunctions h;
    @Autowired
    ChowsChecks chowsChecks;
    @Autowired
    PungsChecks pungsChecks;
    @Autowired
    WinsChecks winsChecks;

    boolean v = false;


    public void lookForSetsToDisplayFirstGo(List<Player> players) {
        Player winnerAtStart;
        for (Player player : players) {
            winnerAtStart = lookForWinAtStart(player);
            if (winnerAtStart != null) {
                System.out.println("Winner!: ");
                System.out.println(player.getUsername());
                return;
            }
            lookForPungsOnFirstGo(player);
            lookForChowsOnFirstGo(player);
        }
    }

    private Player lookForWinAtStart(Player player) {
        return winsChecks.lookForWin(player, player.getCurrentHandNoPlaced());
    }

    private void lookForPungsOnFirstGo(Player player) {
        pungsChecks.lookForPungs(player, player.getCurrentHandNoPlaced());
    }

    private void lookForChowsOnFirstGo(Player player) {
        chowsChecks.lookForChows(player, player.getCurrentHandNoPlaced());
    }

    /**
     * Just finds and returns possible actions to show to the players, that they can take <br>
     * Should not update anything
     */
    public Map<Player, String> lookForActionsAfterDiscard(List<Player> players, Player currentPlayer, Tile tile) {

        Map<Player, String> playerAndAction = new HashMap<>();

        List<Player> playersCopy = new ArrayList<>(players);
        for (Player player : playersCopy) player.setActionToTake(null); //No actions to take as about to re calculate new ones

        Player nextPlayer = h.iterateThroughListWithLooping(playersCopy, currentPlayer);
        playersCopy.remove(currentPlayer);

        //Look for wins here (1st priority) - Currently not working
//        for (Player player : playersCopy) {
//            playerWithAction = winsChecks.lookForWinAfterDiscard(player, tile);
//            if (playerWithAction != null) {
//                System.out.println("Winner!: ");
//                System.out.println(playerWithAction.getUsername());
//                return playerWithAction;
//            }
//        }

        //Then check for pungs (2nd priority)
        for (Player player : playersCopy) {
            List<Tile> foundPung = pungsChecks.lookForPungsAfterDiscard(player, tile);
            if (foundPung != null) {
                List<Integer> tileIds = foundPung.stream().map(Tile::getTileId).toList();
                player.setTilesInActionToTake(tileIds);
                System.out.println("Tiles in action to take: " + player.getTilesInActionToTake());
                playerAndAction.put(player, "P");
                return playerAndAction;
            }
        }

        //Then check for chows (3rd priority)
        List<Tile> foundChow = chowsChecks.lookForChowsAfterDiscard(nextPlayer, tile);
        System.out.println("Found chow: " + foundChow);
        if (foundChow != null) {
            List<Integer> tileIds = foundChow.stream().map(Tile::getTileId).toList();
            nextPlayer.setTilesInActionToTake(tileIds);
            System.out.println("Tiles in action to take: " + nextPlayer.getTilesInActionToTake());
            playerAndAction.put(nextPlayer, "C");
            return playerAndAction;
        }
        return null;
    }
}
