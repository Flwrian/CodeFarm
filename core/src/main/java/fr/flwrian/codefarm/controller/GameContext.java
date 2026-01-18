package fr.flwrian.codefarm.controller;

import fr.flwrian.codefarm.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.World;

public class GameContext {
    public Player player;
    public World world;
    public Base base;

    public GameContext(Player p, World w, Base b) {
        this.player = p;
        this.world = w;
        this.base = b;
    }
}
