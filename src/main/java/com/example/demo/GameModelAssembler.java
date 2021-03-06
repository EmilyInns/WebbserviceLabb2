package com.example.demo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {


    @Override
    public EntityModel<Game> toModel(Game game) {
        return new EntityModel<>(game,
                linkTo(methodOn(GameController.class).one(game.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).all()).withRel("games"));
    }

    @Override
    public CollectionModel<EntityModel<Game>> toCollectionModel(Iterable<? extends Game> entities) {
        var collection = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(collection,
                linkTo(methodOn(GameController.class).all()).withSelfRel());
    }
}