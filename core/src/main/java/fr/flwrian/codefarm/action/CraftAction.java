package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.recipe.Recipe;

public class CraftAction implements Action {
    private final Recipe recipe;
    private int remaining = 10;

    public CraftAction(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public int totalCost() {
        return 10;
    }

    @Override
    public int remainingCost() {
        return remaining;
    }

    @Override
    public boolean canStart(GameContext ctx) {
        return ctx.player.craft(recipe);
    }

    @Override
    public void start(GameContext ctx) {
        
    }

    @Override
    public void applyTick(GameContext ctx) {
        remaining--;
    }

    @Override
    public boolean isFinished() {
        return remaining <= 0;
    }

    @Override
    public void finish(GameContext ctx) {
        
    }

    @Override
    public String toString() {
        return "Craft(" + recipe.id + ")";
    }
}
