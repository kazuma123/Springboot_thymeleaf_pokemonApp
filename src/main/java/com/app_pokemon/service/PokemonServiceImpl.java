package com.app_pokemon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app_pokemon.model.Pokemon;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PokemonServiceImpl implements PokemonService {
	private String url = "https://pokeapi.co/api/v2/pokemon-species";
	List<Pokemon> pokemones;
	String resultado = null;
	ObjectNode json = null;
	ArrayNode array;
	ObjectMapper maper = null;
	int length = 0;
	
	@Autowired
	RestTemplate template;

	@Override
	public List<Pokemon> getList() throws IOException, JsonProcessingException{
		pokemones = new ArrayList<>();
		maper = new ObjectMapper();
		for (int i = 1; i < getLength(); i++) {
			resultado = template.getForObject(url + "/" + i, String.class);
			array = (ArrayNode) maper.readTree("[" + resultado + "]");
			for (Object ob : array) {
				json = (ObjectNode) ob;
				pokemones.add(new Pokemon(json.get("id").asLong(), json.get("name").asText(),
						json.get("shape").get("name").asText(), json.get("is_baby").asText(),
						json.get("is_legendary").asText(), json.get("is_mythical").asText(),
						json.get("habitat").get("name").asText(), json.get("color").get("name").asText()

				));
			}
		}

		return pokemones;
	}

	@Override
	public Pokemon buscarPokemon(Long id) {
		// TODO Auto-generated method stub
		return pokemones.stream().filter(p -> p.getId() == id).collect(Collectors.toList()).get(0);
	}

	public int getLength() throws JacksonException, JsonProcessingException {
		resultado = template.getForObject(url, String.class);
		array = (ArrayNode) maper.readTree("[" + resultado + "]");
		for (Object ob : array) {
			json = (ObjectNode) ob;
			length = json.get("results").size();
		}
		return length;
	}
	
	@Override
	public Page<Pokemon> findPaginated(Pageable pageable) throws JsonProcessingException, IOException {
		List<Pokemon> listPokemons = getList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Pokemon> list;

        if (listPokemons.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listPokemons.size());
            list = listPokemons.subList(startItem, toIndex);
        }

        Page<Pokemon> listPage
          = new PageImpl<Pokemon>(list, PageRequest.of(currentPage, pageSize), listPokemons.size());

        return listPage;
    }
}
