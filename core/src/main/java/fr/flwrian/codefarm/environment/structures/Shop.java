package fr.flwrian.codefarm.environment.structures;

import java.util.HashMap;
import java.util.Map;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.item.ItemType;

public class Shop extends Structure {
    private final Map<String, Integer> prices;

    public Shop(String name, int x, int y) {
        super(name, x, y, 1, 1);
        this.prices = new HashMap<>();
        initPrices();
    }

    private void initPrices() {
        prices.put("wood", 10);
        prices.put("stone", 15);
        prices.put("tool", 50);
    }

    public int getPrice(String item) {
        return prices.getOrDefault(item, 0);
    }

    public int sellWood(Player player, int amount) {
        if (player.inventory.get(ItemType.WOOD) >= amount) {
            player.inventory.remove(ItemType.WOOD, amount);
            return amount * prices.get("wood");
        }
        return 0;
    }

    public int sellStone(Player player, int amount) {
        if (player.inventory.get(ItemType.STONE) >= amount) {
            player.inventory.remove(ItemType.STONE, amount);
            return amount * prices.get("stone");
        }
        return 0;
    }
}