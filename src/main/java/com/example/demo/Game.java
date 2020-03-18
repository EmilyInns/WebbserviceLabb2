package com.example.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Game {
    @Id @GeneratedValue Long id;
    String name;
    String developer;

    public Game(Long id, String name, String developer){
        this.id = id;
        this.name = name;
        this.developer = developer;
    }


}
