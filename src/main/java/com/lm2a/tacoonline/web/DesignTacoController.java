package com.lm2a.tacoonline.web;


import com.lm2a.tacoonline.data.IngredientRepository;
import com.lm2a.tacoonline.model.Ingredient;
import com.lm2a.tacoonline.model.Taco;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lm2a.tacoonline.model.Ingredient.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/design")
@RequiredArgsConstructor
public class DesignTacoController {

    final IngredientRepository ingredientRepository;

    @GetMapping
    public String showDesignForm(Model model) {

        fillModelWithIngredients(model);

        //model.addAttribute("tktn", new Taco());
        return "design";
    }

    @ModelAttribute(name = "tktn")
    public Taco taco() {
        return new Taco();
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(ing -> ing.getType().equals(type))
                .collect(Collectors.toList());
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute(name = "tktn") Taco design, Errors errors, Model model) {
        if (errors.hasErrors()) {
            fillModelWithIngredients(model);
            return "design";
        }
        log.info("Processing Design Taco: {}", design);
        return "redirect:/orders/current";
    }

    public void fillModelWithIngredients(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();

        ingredientRepository.findAll().forEach(i -> ingredients.add(i));

                //.forEach(ingredients::add);


//        List<Ingredient> ingredients = Arrays.asList(
//                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//
//        );

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }
}
