package com.lm2a.tacoonline.model;


import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Taco {
    @Size(min = 5, max = 50, message="El nombre debe tener al menos 5 caracteres")
    private String name;
    private List<String> ingredients;
}
