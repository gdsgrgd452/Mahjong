package com.example.mahjong.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generates the ID
    int gameId;
}
