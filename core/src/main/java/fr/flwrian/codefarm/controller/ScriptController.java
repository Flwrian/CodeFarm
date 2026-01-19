package fr.flwrian.codefarm.controller;

import java.util.ArrayDeque;
import java.util.Queue;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import com.badlogic.gdx.Gdx;

import fr.flwrian.codefarm.*;
import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.action.DepositAction;
import fr.flwrian.codefarm.action.HarvestAction;
import fr.flwrian.codefarm.action.MoveAction;

public class ScriptController implements Controller {

    private Globals globals;
    private GameContext ctx;

    private Queue<Action> actionQueue;


    public ScriptController(GameContext ctx, Queue<Action> actionQueue) {
        this.ctx = ctx;
        this.actionQueue = actionQueue;
        globals = JsePlatform.standardGlobals();
        registerApi();
        String script = Gdx.files.internal("scripts/player.lua").readString();
        globals.load(script, "player.lua").call();

    }

    private void registerApi() {
        globals.set("move", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String dir = arg.checkjstring();
                switch (dir) {
                    case "up":    actionQueue.add(new MoveAction(0, 1)); break;
                    case "down":  actionQueue.add(new MoveAction(0, -1)); break;
                    case "left":  actionQueue.add(new MoveAction(-1, 0)); break;
                    case "right": actionQueue.add(new MoveAction(1, 0)); break;
                }
                return LuaValue.NIL;
            }
        });

        globals.set("harvest", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                actionQueue.add(new HarvestAction());
                return LuaValue.NIL;
            }
        });

        globals.set("deposit", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                actionQueue.add(new DepositAction());
                return LuaValue.NIL;
            }
        });



    }

    @Override
    public void update(GameContext ctx) {
        LuaValue updateFunc = globals.get("update");
        if (!updateFunc.isnil()) {
            updateFunc.call();
        }
    }
}
