package com.example.mahjong.service;

import com.example.mahjong.exception.GameCreationFailedException;
import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.*;
import com.example.mahjong.repository.TileRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TileService {
    private final TileRepository tileRepository;

    public TileService(TileRepository tileRepository) { //Initialises the tile Repository
        this.tileRepository = tileRepository;
    }

    public Tile createTile(String type, String suit, Integer number, Game game) {
        try {
            Tile tile = null;
            switch(type) {
                case "Suited":
                    SuitedTile suited = new SuitedTile();
                    suited.setSuit(suit);
                    suited.setNumber(number);
                    tile = suited;
                    break;

                case "Honor":
                    HonorTile honor = new HonorTile();
                    honor.setSuit(suit);
                    tile = honor;
                    break;

                case "Flower":
                    FlowerTile flower = new FlowerTile();
                    flower.setSuit(suit);
                    flower.setNumber(number);
                    tile = flower;
                    break;

                default:
                    throw new IllegalArgumentException("Unknown tile type: " + type);
            }
            tile.setGame(game);
            tileRepository.save(tile);
            return tile;

        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to create tile of type: " + type, e);
        }
    }

    public List<Tile> getAllByGame(Game game) {
        try {
            return tileRepository.getAllByGame(game);
        } catch (Exception e) {
            throw new GameCreationFailedException("Failed to get all tiles from game id: ", e);
            //Add new exception here
        }
    }
}