package com.lm2a.tacoonline.web;


import com.lm2a.tacoonline.data.IngredientRepository;
import com.lm2a.tacoonline.oldData.TacoRepoImpl;
import com.lm2a.tacoonline.model.Ingredient;
import com.lm2a.tacoonline.model.Order;
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
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/design")
@RequiredArgsConstructor
public class DesignTacoController {

    final IngredientRepository ingredientRepository;
    final TacoRepoImpl tacoRepo;

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

    @ModelAttribute
    public Order order() {
        return new Order();
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(ing -> ing.getType().equals(type))
                .collect(Collectors.toList());
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute(name = "tktn") Taco design, Errors errors, Model model, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            fillModelWithIngredients(model);
            return "design";
        }
        Taco saved = tacoRepo.save(design);
        log.info("Processing Design Taco: {}", saved);
        return "redirect:/orders/current";
    }

    public void fillModelWithIngredients(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();

        ingredientRepository.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }
}
