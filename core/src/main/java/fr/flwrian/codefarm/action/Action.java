package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.game.GameContext;

public interface Action {
    int totalCost();
    int remainingCost();
    boolean canStart(GameContext ctx);
    void start(GameContext ctx);
    void applyTick(GameContext ctx);
    boolean isFinished();
    void finish(GameContext ctx);
    String toString();
}

