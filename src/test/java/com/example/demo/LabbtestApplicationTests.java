package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@WebMvcTest(GameController.class)
@Import({GameModelAssembler.class})
class LabbtestApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameRepository repository;

    @BeforeEach
    void setUp() {
        when(repository.findAll()).thenReturn(List.of(new Game(1L, "Dragon Age","Bioware"), new Game(2L, "Animal Crossing","Nintendo")));
        when(repository.findById(1L)).thenReturn(Optional.of(new Game(1L, "Dragon Age","Bioware")));
        when(repository.save(any(Game.class))).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            var g = (Game) args[0];
            return new Game(1L, g.getName(), g.getDeveloper());
        });

    }

    @Test
    void getAllReturnsListOfAllGames() throws Exception {
        mockMvc.perform(
                get("/api/games").contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.gameList[0]._links.self.href", is("http://localhost/api/games/1")));
            //     .andExpect(jsonPath("_embedded.gameList[0]._links.games.href", is("http://localhost/api/games/")))
            //    .andExpect(jsonPath("_embedded.gameList[0].name", is("Dragon Age")));
        //Build json paths with: https://jsonpath.com/
    }

    @Test
    @DisplayName("Calls Get method with url /api/games/1")
    void getGameWithValidId() throws Exception {
        mockMvc.perform(
                get("/api/games/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("content[0].links[2].rel", is("self")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/games/1")));
    }

    @Test
    @DisplayName("Calls Get method with invalid id url /api/games/3")
    void getGameWithInValidId() throws Exception {
        mockMvc.perform(
                get("/api/games/6").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addNewGameWithPost() throws Exception {
        mockMvc.perform(
                post("/api/games/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0,\"name\":\"Dragon Age\",\"developer\":\"Bioware\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void replaceGame() throws Exception {
        mockMvc.perform(
                put("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3},{\"name\":\"World of Warcraft\",\"developer\":\"Blizzard\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void editGame() throws Exception {
        mockMvc.perform(
                patch("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":5,\"name\":\"differentName\",\"developer\":\"notARealDev\"}"))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteByIDNotFound() throws Exception {
        mockMvc.perform(delete("/api/games/{id}", 3L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteByID() throws Exception {
        mockMvc.perform(delete("/api/games/1"))
                .andExpect(status().isOk());
    }


}
