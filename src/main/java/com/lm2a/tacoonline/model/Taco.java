package com.lm2a.tacoonline.model;


import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Taco {
    private long id;
    private Date createdAt;
    @Size(min = 5, max = 50, message="El nombre debe tener al menos 5 caracteres")
    private String name;
    private List<Ingredient> ingredients;
}
