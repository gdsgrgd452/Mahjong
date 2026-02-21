package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Chow;
import com.example.mahjong.model.actions.Pung;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class Discard {

    @Autowired
    private TileService tileService;
    @Autowired
    GameService gameService;
    @Autowired
    Checks checks;
    @Autowired
    Actions actions;
    @Autowired
    private HelperFunctions h;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PungService pungService;
    @Autowired
    private ChowService chowService;
    @Autowired
    private PungsChecks pungsChecks;
    @Autowired
    private ChowsChecks chowsChecks;

    /**
     * Called from the controller after a discard call is made <br>
     * Handles everything from when a player attempts a discard to just after when a new player is swapped to and a new tile is added to their hand
     */
    public void discardTile(Integer tileId) {   //Function called closest to the players interface
        Game game = gameService.findFirstGame();
        Tile tile = tileService.findTileById(tileId);

        //need to remove just discarded tile from pung/chow
        removeJustDiscardedTileFromAction(game, tile);

        //Re get tile
        tile = tileService.findTileById(tileId);
        if (tile.getPung() != null || tile.getChow() != null) {
            System.out.println("Still in a set");
            return;
        }

        unSetNewJustDiscardedTile(game, tile); //Ensures the newly discarded one is the only marked one, calls the discard method


        //Before you swap turns un mark the newly picked up ones
        List<Tile> previousPlayerTiles = game.getCurrentPlayer().getCurrentHandNoPlaced();
        List<Tile> previousPlayerNewTiles = previousPlayerTiles.stream().filter(Tile::isJustPickedUp).toList();
        for (Tile previousPlayerNewTile : previousPlayerNewTiles) previousPlayerNewTile.setJustPickedUp(false);


        //Finish here to update display
        gameService.saveGame(game);
    }

    /**
     * Called when a valid action (Pung, chow ect) is NOT found <br>
     * Swaps to next player in the lists turn, gives them a tile from the board then handles if it is a flower
     */
    public void whenNoActionsFound(Game game) {
        Player playerTurn = actions.swapToNextPlayersTurn(game, game.getCurrentPlayer());
        Tile newTileFromWall = h.addRandomTileToPlayer(game, playerTurn); //Here since they are just the next in the list they can pick from the wall
        newTileFromWall.setJustPickedUp(true);
        actions.findFlowers(game, playerTurn);
        System.out.println("No action found swapped to next player and picked a tile from the wall");
        gameService.saveGame(game);
    }

    /**
     * Called when a valid action (Pung, chow ect) is found <br>
     * Swaps to the player's turn who has a valid action and gives them the tile.
     * @param playerWithAction Player who picked up the tile because they have a valid action
     * @param actionType The type of action "P"-Pung or "C"-Chow
     */
    public void whenSomeActionsFound(Game game, Player playerWithAction, String actionType) {

        Tile tile = tileService.getJustDiscardedTile(game);

        //Breaking here where it adds all tiles to one pung/chow
        //To do with db relationship?

        //Turn list of ids into list of tiles here

        if (Objects.equals(actionType, "P")) {
            System.out.println("Tiles: " + playerWithAction.getTilesInActionToTake());
            pungsChecks.handlePung(playerWithAction, playerWithAction.getTilesInActionToTake());
        } else if (Objects.equals(actionType, "C")) {
            chowsChecks.handleChow(playerWithAction, playerWithAction.getTilesInActionToTake());
        }
        playerWithAction.setActionToTake(null);
        Player playerTurn = actions.swapToPlayerWithAction(game, playerWithAction);
        tile.setJustDiscarded(false);
        Tile newTileFromOtherPlayer = h.addTileToPlayer(playerTurn, tile);
        newTileFromOtherPlayer.setJustPickedUp(true);
        System.out.println("Found an action for a player, swapped to them and added the tile to the player");
    }


    /**
     * Sets any tiles in the game where justDiscarded = true to false <br>
     * Sets the newly discarded tile's justDiscarded to true
     * @param tile Tile that has just been discarded
     */
    private void unSetNewJustDiscardedTile(Game game, Tile tile) {
        List<Tile> justDiscardedTiles = tileService.findAllTilesByGame(game).stream().filter(Tile::isJustDiscarded).toList();
        for (Tile justDiscardedTile : justDiscardedTiles) {
            justDiscardedTile.setJustDiscarded(false);
            tileService.saveTile(justDiscardedTile);
        }
        tileService.discardTile(tile);
    }



    private void removeJustDiscardedTileFromAction(Game game, Tile tile) {
        Pung pung = tile.getPung();
        if (pung != null) {
            List<Tile> tilesInPung = new ArrayList<>(pungService.findPungById(pung.getPungId()).getTiles());
            for (Tile tileInPung : tilesInPung) {
                System.out.println("Trying to remove tile from pung: " + tileInPung.getTileId());
                tileService.removeFromPung(tileInPung);
            }
            System.out.println("Tiles: " + pungService.findPungById(pung.getPungId()).getTiles());
            pungService.deletePung(pung.getPungId());
            System.out.println("Removed pung as player discarded a tile");
        }
        Chow chow = tile.getChow();
        if (chow != null) {
            List<Tile> tilesInChow = new ArrayList<>(chowService.findChowById(chow.getChowId()).getTiles());
            for (Tile tileInChow : tilesInChow) {
                System.out.println("Trying to remove tile from chow: " + tileInChow.getTileId());
                tileService.removeFromChow(tileInChow);
            }
            System.out.println("Tiles: " + chowService.findChowById(chow.getChowId()).getTiles());
            chowService.deleteChow(chow.getChowId());
            System.out.println("Removed chow as player discarded a tile");
        }
    }

}
