package com.example.mahjong.model.tiles;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Defines the strategy - All tiles are in 1 table
//@DiscriminatorColumn(name = "tile_type") // Optional: names the column that tells JPA which subclass to use
public class Tile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int id;
    String type;
}
