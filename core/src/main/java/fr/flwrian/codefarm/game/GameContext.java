package fr.flwrian.codefarm.game;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.structures.Structure;
import fr.flwrian.codefarm.upgrade.UpgradeRegistry;
import fr.flwrian.codefarm.environment.World;

public class GameContext {
    public Player player;
    public fr.flwrian.codefarm.environment.World world;
    public Base base;
    public UpgradeRegistry upgradeRegistry;

    public GameContext(Player p, World w, Base b) {
        this.player = p;
        this.world = w;
        this.base = b;
        this.upgradeRegistry = new UpgradeRegistry();
    }


    public Structure getCurrentStructure() {
        return world.getStructureContaining(player.x, player.y);
    }


    public <T extends Structure> T getPlayerStructure(Class<T> type) {
        Structure struct = getCurrentStructure();
        if (type.isInstance(struct)) {
            return type.cast(struct);
        }
        return null;
    }
}