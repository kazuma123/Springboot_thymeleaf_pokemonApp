package com.app_pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {
	private Long id;
	private String nombre;
    private String forma;
    private String bebe;
    private String legendario;
    private String mitico;
    private String habitad;
    private String color;
}
