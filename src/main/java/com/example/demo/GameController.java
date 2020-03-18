package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/api/games")
@Slf4j
public class GameController {

    final GameRepository repository;
    private final GameModelAssembler assembler;

    public GameController(GameRepository storage, GameModelAssembler gameModelAssembler) {
        this.repository = storage;
        this.assembler = gameModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Game>> all() {
        log.debug("All games called");
        return assembler.toCollectionModel(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Game>> one(@PathVariable long id) {
            return repository.findById(id)
                    .map(assembler::toModel)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        log.info("POST create Game " + game);
        var p = repository.save(game);
        log.info("Saved to repository " + p);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(GameController.class).slash(p.getId()).toUri());
        return new ResponseEntity<>(p, headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Game>> deleteGame(@PathVariable long id) {
        if (repository.existsById(id)) {
            //log.info("Product deleted");
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    ResponseEntity<Game> replaceGame(@RequestBody Game newGame, @PathVariable Long id) {
        return repository.findById(id)
                .map(game -> {
                    game.setName(newGame.getName());
                    game.setDeveloper(newGame.getDeveloper());
                    repository.save(game);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(linkTo(GameController.class).slash(game.getId()).toUri());
                    return new ResponseEntity<>(game, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    ResponseEntity<Game> modifyGame(@RequestBody Game newGame, @PathVariable Long id) {
        return repository.findById(id)
                .map(game -> {
                    if (newGame.getName() != null)
                        game.setName(newGame.getName());
                    if (newGame.getDeveloper() != null)
                        game.setDeveloper(newGame.getDeveloper());

                    repository.save(game);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(linkTo(GameController.class).slash(game.getId()).toUri());
                    return new ResponseEntity<>(game, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
