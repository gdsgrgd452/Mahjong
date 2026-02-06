package com.example.mahjong.repository;

import com.example.mahjong.model.tiles.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TileRepository extends JpaRepository<Tile, Integer> {

}
