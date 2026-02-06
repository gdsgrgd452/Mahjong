package com.example.mahjong.repository;

import com.example.mahjong.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot, Integer> {

}