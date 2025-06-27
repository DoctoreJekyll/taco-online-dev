package com.lm2a.tacoonline.oldData;

import com.lm2a.tacoonline.model.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Ingredient findOne(String id);
}
