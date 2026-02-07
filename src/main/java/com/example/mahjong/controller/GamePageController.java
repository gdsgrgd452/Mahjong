package com.example.mahjong.controller;


import com.example.mahjong.logic.GameStarter;
import com.example.mahjong.logic.TileDisplayer;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GamePageController {

    @Autowired
    public GameStarter gameStarter;
    @Autowired
    public TileDisplayer tileDisplayer;
    @Autowired
    public GameService gameService;

    @GetMapping("/game")
    public String serveGamePage(Model model) {
        gameStarter.initaliseGame();
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
