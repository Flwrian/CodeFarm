package fr.flwrian.codefarm;

public class Base {
    public int x, y;
    public int storedWood = 0;
    public int storedStone = 0;

    public Base(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isPlayerOn(Player p) {
        return p.x == x && p.y == y;
    }

    public void deposit(Player p) {
        storedWood += p.wood;
        storedStone += p.stone;
        p.wood = 0;
        p.stone = 0;
    }
}
