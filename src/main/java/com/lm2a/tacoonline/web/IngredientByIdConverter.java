package com.lm2a.tacoonline.web;

import com.lm2a.tacoonline.data.IngredientRepository;
import com.lm2a.tacoonline.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    final IngredientRepository ingredientRepository;

    @Override
    public Ingredient convert(String source) {
        Optional<Ingredient> optIngredient = ingredientRepository.findById(source);
        if(optIngredient.isPresent()){
            return optIngredient.get();
        }
        return null;
    }
}
