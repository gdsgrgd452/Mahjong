package com.example.mahjong.model.tiles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("Flower")
public class FlowerTile extends Tile  {
    int flowerNumber;
}
