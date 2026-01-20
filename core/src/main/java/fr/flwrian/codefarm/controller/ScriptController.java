package fr.flwrian.codefarm.controller;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;

import fr.flwrian.codefarm.Direction;
import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.action.DepositAction;
import fr.flwrian.codefarm.action.HarvestAction;
import fr.flwrian.codefarm.action.MoveAction;
import fr.flwrian.codefarm.action.TurnAction;

public class ScriptController implements Controller {
    
    private final Globals globals;
    private final LuaValue coroutine;
    private Action nextAction = null;

    public ScriptController() {
        globals = JsePlatform.standardGlobals();
        registerActionAPI();
        
        String script = Gdx.files.internal("scripts/player.lua").readString();
        globals.load(script, "player.lua").call();
        
        LuaValue updateFunc = globals.get("update");
        if (!updateFunc.isnil()) {
            coroutine = globals.get("coroutine").get("create").call(updateFunc);
        } else {
            coroutine = null;
        }
    }

    private void registerActionAPI() {
        globals.set("move", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String dirStr = arg.checkjstring();
                Direction dir = Direction.fromString(dirStr);
                nextAction = new MoveAction(dir);
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("harvest", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new HarvestAction();
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("deposit", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new DepositAction();
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("turnRight", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new TurnAction(TurnAction.TurnType.RIGHT);
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("turnLeft", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new TurnAction(TurnAction.TurnType.LEFT);
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("turnAround", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new TurnAction(TurnAction.TurnType.AROUND);
                return globals.get("coroutine").get("yield").call();
            }
        });
    }

    @Override
    public Action update() {
        if (coroutine == null) return null;
        
        nextAction = null;
        LuaValue status = globals.get("coroutine").get("status").call(coroutine);
        String statusStr = status.tojstring();
        System.out.println("🔄 Coroutine status: " + statusStr);
        
        if (statusStr.equals("suspended")) {
            LuaValue result = globals.get("coroutine").get("resume").call(coroutine);
            if (!result.arg1().toboolean()) {
                System.err.println("❌ Lua Error: " + result.arg(2).tojstring());
            }
        } else if (statusStr.equals("dead")) {
            System.out.println("✅ Script finished");
        }
        
        return nextAction;
    }
}