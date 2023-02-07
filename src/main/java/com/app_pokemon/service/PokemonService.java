package com.app_pokemon.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app_pokemon.model.Pokemon;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PokemonService {
	List<Pokemon> getList() throws IOException, JsonProcessingException;
	Pokemon buscarPokemon(Long id);
	public Page<Pokemon> findPaginated(Pageable pageable)throws JsonProcessingException, IOException;
}
