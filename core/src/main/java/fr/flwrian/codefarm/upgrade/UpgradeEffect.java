package fr.flwrian.codefarm.upgrade;

import fr.flwrian.codefarm.game.GameContext;

@FunctionalInterface
public interface UpgradeEffect {
    void apply(GameContext ctx);
}