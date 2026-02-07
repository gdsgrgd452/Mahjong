package com.example.mahjong.model;

import com.example.mahjong.model.tiles.Tile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int gameId;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL) //Links the game to the tiles it has
    private List<Tile> tilesInWholeGame = new ArrayList<>();

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void addTile(Tile tile) {
        this.tilesInWholeGame.add(tile);
        tile.setGame(this);
    }
}
