package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.Direction;
import fr.flwrian.codefarm.game.GameContext;

public class TurnAction implements Action {
    
    public enum TurnType {
        LEFT, RIGHT, AROUND
    }
    
    private final TurnType turnType;
    private int remaining = 10;

    public TurnAction(TurnType turnType) {
        this.turnType = turnType;
    }

    public TurnAction(Direction direction) {
        switch (direction) {
            case LEFT:
                this.turnType = TurnType.LEFT;
                break;
            case RIGHT:
                this.turnType = TurnType.RIGHT;
                break;
            default:
                this.turnType = TurnType.AROUND;
                break;
        }
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
        return true;
    }

    @Override
    public void start(GameContext ctx) {}

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
        switch (turnType) {
            case LEFT:
                ctx.player.turnLeft();
                break;
            case RIGHT:
                ctx.player.turnRight();
                break;
            case AROUND:
                ctx.player.turnAround();
                break;
        }
    }

    @Override
    public String toString() {
        return "Turn(" + turnType + ")";
    }
}