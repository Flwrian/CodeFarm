package fr.flwrian.codefarm.controller;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.structures.Structure;
import fr.flwrian.codefarm.environment.World;

public class GameContext {
    public Player player;
    public fr.flwrian.codefarm.environment.World world;
    public Base base;

    public GameContext(Player p, World w, Base b) {
        this.player = p;
        this.world = w;
        this.base = b;
    }

    /**
     * Helper: récupère la structure où se trouve le joueur
     */
    public Structure getCurrentStructure() {
        return world.getStructureContaining(player.x, player.y);
    }

    /**
     * Helper: vérifie si le joueur est dans une structure spécifique
     */
    public <T extends Structure> T getPlayerStructure(Class<T> type) {
        Structure struct = getCurrentStructure();
        if (type.isInstance(struct)) {
            return type.cast(struct);
        }
        return null;
    }
}