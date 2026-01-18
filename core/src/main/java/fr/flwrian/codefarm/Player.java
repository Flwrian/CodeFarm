package fr.flwrian.codefarm;

public class Player {
    public int x, y;
    public int dirX = 0, dirY = 1;

    public int wood = 0;
    public int stone = 0;

    private float moveCooldown = 0;
    private float moveDelay = 0.15f;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(float dt) {
        if (moveCooldown > 0) {
            moveCooldown -= dt;
        }
    }

    public boolean canAct() {
        return moveCooldown <= 0;
    }

    public boolean canMove(World w, int dx, int dy) {
        int tile = w.getTile(x + dx, y + dy);
        return tile != World.TREE && tile != World.STONE && inWorldBounds(w, dx, dy);
    }

    public boolean inWorldBounds(World w, int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        return newX >= 0 && newX < w.width && newY >= 0 && newY < w.height;
    }

    public boolean move(World w, int dx, int dy) {
        if (!canAct()) return false;
        if (dx == 0 && dy == 0) return false;

        if (canMove(w, dx, dy)) {
            x += dx;
            y += dy;
        }

        dirX = dx;
        dirY = dy;
        moveCooldown = moveDelay;
        return true;
    }

    public boolean harvest(World w) {
        if (!canAct()) return false;

        int tx = x + dirX;
        int ty = y + dirY;
        int tile = w.getTile(tx, ty);

        if (tile == World.TREE) {
            w.setTile(tx, ty, World.GRASS);
            wood++;
            moveCooldown = moveDelay;
            return true;
        }
        if (tile == World.STONE) {
            w.setTile(tx, ty, World.GRASS);
            stone++;
            moveCooldown = moveDelay;
            return true;
        }
        return false;
    }
}
