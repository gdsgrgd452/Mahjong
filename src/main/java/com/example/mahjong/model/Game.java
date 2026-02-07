package com.example.mahjong.model;

import com.example.mahjong.model.tiles.Tile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int gameId;
    public int getGameId() {
        return gameId;
    }
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL) //Links the game to the Tiles it has
    private List<Tile> tilesInWholeGame = new ArrayList<>();
    public void addTile(Tile tile) {
        this.tilesInWholeGame.add(tile);
        tile.setGame(this);
    }

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL) //Links the game to the Players it has
    private List<Player> playersInGame = new ArrayList<>();
    public void addPlayer(Player player) {
        this.playersInGame.add(player);
        player.setGame(this);
    }
}
