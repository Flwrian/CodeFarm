package fr.flwrian.codefarm.environment.structures;

import java.util.HashMap;
import java.util.Map;

import fr.flwrian.codefarm.Player;

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
        if (player.wood >= amount) {
            player.wood -= amount;
            return amount * prices.get("wood");
        }
        return 0;
    }

    public int sellStone(Player player, int amount) {
        if (player.stone >= amount) {
            player.stone -= amount;
            return amount * prices.get("stone");
        }
        return 0;
    }
}