package com.example.mahjong.model;

import com.example.mahjong.model.tiles.Tile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int playerId;
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int userId) {
        this.playerId = userId;
    }

    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private String role;
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne @JoinColumn(name = "game_id") //Links to the game the player is in
    private Game game;
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL) //Links the game to the tiles it has
    private List<Tile> currentHand = new ArrayList<>();
    public List<Tile> getCurrentHand() {
        return currentHand;
    }
    public void setCurrentHand(List<Tile> currentHand) {
        this.currentHand = currentHand;
    }
    public void addTile(Tile tile) {
        this.currentHand.add(tile);
        tile.setPlayer(this);
    }
}
