package com.example.mahjong.logic;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.Tile;
import com.example.mahjong.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TileDisplayer {

    @Autowired
    public TileService tileService;

    public List<Tile> getAllTilesForDisplay(Game game) {
        return tileService.getAllByGame(game);
    }
}
