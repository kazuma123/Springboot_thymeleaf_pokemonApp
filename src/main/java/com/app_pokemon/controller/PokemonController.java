package com.app_pokemon.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.app_pokemon.model.Pokemon;
import com.app_pokemon.service.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class PokemonController {

	@Autowired
	PokemonService service;

	@GetMapping("/")
	public String listaPokemon(Model model, @RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size) throws JsonProcessingException, IOException {
		
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        
        Page<Pokemon> listPagePokemons = service.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("listPokemon", listPagePokemons);

        int totalPages = listPagePokemons.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		
		return "pokemon/listPokemon";
	}

	@GetMapping("/findPokemonById/{id}")
	public String detallePokemon(@PathVariable("id") Long id, Model model) {
		Pokemon listaUnico = service.buscarPokemon(id);
		model.addAttribute("pokemon", listaUnico);
		return "pokemon/detallePokemon";
	}
}
