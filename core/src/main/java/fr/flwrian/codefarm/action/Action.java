package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public interface Action {
    int totalCost();
    int remainingCost();
    boolean canStart(GameContext ctx);
    void start(GameContext ctx);
    void applyTick(GameContext ctx);   // consomme 1 tick
    boolean isFinished();
    void finish(GameContext ctx);
}

