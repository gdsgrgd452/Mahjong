package com.example.mahjong.controller;


import com.example.mahjong.logic.GameStarter;
import com.example.mahjong.logic.TileDisplayer;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.GameService;
import com.example.mahjong.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class GamePageController {

    @Autowired
    public GameStarter gameStarter;
    @Autowired
    public TileDisplayer tileDisplayer;
    @Autowired
    public GameService gameService;
    @Autowired
    private PlayerService playerService;

    @GetMapping("/game")
    public String serveGamePage(Model model) {
        gameStarter.initialiseGame();
        Player player = playerService.findPlayerById(1);
        model.addAttribute("playerHand", player.getCurrentHand());
        return "game";
    }

    @GetMapping("/tilesDisplay")
    public String serveTilesDisplayPage(Model model) {
        Game game = gameService.findFirstGame();
        List<Tile> tiles = tileDisplayer.getAllTilesForDisplay(game);
        model.addAttribute("tiles", tiles);
        return "tilesDisplay";
    }
}
