package fr.flwrian.codefarm.environment.structures;

import fr.flwrian.codefarm.Player;

public class Base extends Structure {
    public int storedWood = 0;
    public int storedStone = 0;

    public Base(int x, int y, int width, int height) {
        super("Base", x, y, width, height);
    }

    public void deposit(Player player) {
        storedWood += player.wood;
        storedStone += player.stone;
        player.wood = 0;
        player.stone = 0;
    }

    public boolean withdraw(int wood, int stone) {
        if (storedWood >= wood && storedStone >= stone) {
            storedWood -= wood;
            storedStone -= stone;
            return true;
        }
        return false;
    }
}