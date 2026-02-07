package com.example.mahjong.model.tiles;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.Player;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // All tiles are in 1 table
@DiscriminatorColumn(name = "type") // The column that selects for the subclass
public class Tile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int tileId;
    public int getTileId() {
        return tileId;
    }
    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    String suit;
    public String getSuit() {
        return suit;
    }
    public void setSuit(String suit) {
        this.suit = suit;
    }

    @ManyToOne @JoinColumn(name = "game_id")
    private Game game;
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    @ManyToOne @JoinColumn(name = "player_id")
    private Player player;
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Transient //To prevent errors because honor tiles have no number
    public Integer getNumber() {
        return null;
    }

}
