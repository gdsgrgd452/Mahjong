package com.example.mahjong.model;

import com.example.mahjong.model.tiles.Tile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int playerId;
    private String username;
    private String password;
    private String role;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL) //Links the game to the tiles it has
    private List<Tile> currentHand = new ArrayList<>();
    @OneToOne
    private Game game;

    public void addTile(Tile tile) {
        this.currentHand.add(tile);
        tile.setUser(this);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int userId) {
        this.playerId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
