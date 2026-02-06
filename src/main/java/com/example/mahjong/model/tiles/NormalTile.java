package com.example.mahjong.model.tiles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("Normal")
public class NormalTile extends Tile  {
    String suit;
    int number;
}