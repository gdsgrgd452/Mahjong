package com.example.mahjong.repository;

import com.example.mahjong.model.Player;
import com.example.mahjong.model.actions.Pung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PungRepository extends JpaRepository<Pung, Integer> {

    List<Pung> findAllByPlayer(Player player);
}