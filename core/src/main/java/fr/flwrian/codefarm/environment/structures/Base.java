package fr.flwrian.codefarm.environment.structures;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.item.Inventory;
import fr.flwrian.codefarm.item.ItemType;

public class Base extends Structure {
    
    public Inventory storage;

    public Base(int x, int y, int width, int height) {
        super("Base", x, y, width, height);
        this.storage = new Inventory();
    }


    public void deposit(Player player) {
        for (ItemType type : ItemType.values()) {
            long amount = player.inventory.get(type);
            if (amount > 0) {
                player.inventory.transferAll(storage, type);
            }
        }
    }

    public boolean withdraw(Player player, ItemType type, long amount) {
        return storage.transfer(player.inventory, type, amount);
    }
}