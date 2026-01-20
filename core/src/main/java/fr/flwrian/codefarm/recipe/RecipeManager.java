package fr.flwrian.codefarm.recipe;

import java.util.*;

public class RecipeManager {
    private final Map<String, Recipe> recipes = new HashMap<>();

    public void register(Recipe recipe) {
        recipes.put(recipe.id, recipe);
    }

    public Recipe get(String id) {
        return recipes.get(id);
    }

    public Collection<Recipe> all() {
        return recipes.values();
    }
}
