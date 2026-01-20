package fr.flwrian.codefarm.recipe;

import java.util.Map;
import fr.flwrian.codefarm.item.ItemType;

public class Recipe {
    public final String id;
    public final Map<ItemType, Long> inputs;
    public final Map<ItemType, Long> outputs;

    public Recipe(String id, Map<ItemType, Long> inputs, Map<ItemType, Long> outputs) {
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
    }
}
