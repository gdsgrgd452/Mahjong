package com.example.mahjong.model.tiles;

import com.example.mahjong.model.Game;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Defines the strategy - All tiles are in 1 table
@DiscriminatorColumn(name = "type") // Optional: names the column that tells JPA which subclass to use
public class Tile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int tileId;
    String suit;
    @ManyToOne @JoinColumn(name = "game_id")
    private Game game;

    @Transient
    public Integer getNumber() { //To prevent
        return null;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
