package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {


   // Game findByName(String name);
   // Game findbyDeveloper(String developer);

}