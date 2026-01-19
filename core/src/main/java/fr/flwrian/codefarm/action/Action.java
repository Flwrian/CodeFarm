package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public interface Action {
    int cost();
    boolean canExecute(GameContext ctx);
    void execute(GameContext ctx);
}
