package fr.flwrian.codefarm.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.flwrian.codefarm.Direction;
import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.action.DepositAction;
import fr.flwrian.codefarm.action.HarvestAction;
import fr.flwrian.codefarm.action.MoveAction;
import fr.flwrian.codefarm.action.TurnAction;
import fr.flwrian.codefarm.game.GameContext;

public class KeyboardController implements Controller {
    
    private final GameContext ctx;

    public KeyboardController(GameContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Action update() {
        Direction wantedDirection = null;
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            wantedDirection = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            wantedDirection = Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            wantedDirection = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            wantedDirection = Direction.RIGHT;
        }
        
        if (wantedDirection != null) {
            if (ctx.player.direction != wantedDirection) {
                return createTurnAction(ctx.player.direction, wantedDirection);
            } else {
                return new MoveAction(wantedDirection);
            }
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            return new HarvestAction();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            return new DepositAction();
        }
        
        // if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
        //     return new TurnAction(TurnAction.TurnType.LEFT);
        // }
        
        // if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
        //     return new TurnAction(TurnAction.TurnType.RIGHT);
        // }
        
        return null;
    }


    private Action createTurnAction(Direction from, Direction to) {
        int rotations = 0;
        Direction current = from;
        
        while (current != to && rotations < 4) {
            current = current.rotateClockwise();
            rotations++;
        }
        
        if (rotations == 1) {
            return new TurnAction(TurnAction.TurnType.RIGHT);
        } else if (rotations == 2) {
            return new TurnAction(TurnAction.TurnType.AROUND);
        } else if (rotations == 3) {
            return new TurnAction(TurnAction.TurnType.LEFT);
        }
        
        return null;
    }
}