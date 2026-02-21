package com.example.mahjong.repository;

import com.example.mahjong.model.Game;
import com.example.mahjong.model.tiles.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This repo works for Suited, FlowerTile and HonorTile
@Repository
public interface TileRepository extends JpaRepository<Tile, Integer> {
    List<Tile> getAllByGame(Game game);
    List<Tile> findAllByGameAndPlayerIsNull(Game game);
    List<Tile> findAllByGameAndPlayerIsNullAndDiscarded(Game game, boolean discarded);
    List<Tile> findAllByGameAndPlayerIsNullAndDiscardedAndJustDiscarded(Game game, boolean discarded, boolean justDiscarded);
    Tile findFirstByGameAndPlayerIsNullAndDiscardedAndJustDiscarded(Game game, boolean discarded, boolean justDiscarded);
}
