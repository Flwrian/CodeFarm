package fr.flwrian.codefarm.recipe;

import fr.flwrian.codefarm.item.ItemType;
import java.util.Map;

public class Recipes {
    public static final Recipe WOOD_TO_PLANK = new Recipe(
        "wood_to_plank",
        Map.of(ItemType.WOOD, 1L),
        Map.of(ItemType.PLANK, 4L)
    );


    public static void registerAll(RecipeManager manager) {
        manager.register(WOOD_TO_PLANK);
    }
}
