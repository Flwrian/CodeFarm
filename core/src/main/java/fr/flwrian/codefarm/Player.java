package fr.flwrian.codefarm;

import fr.flwrian.codefarm.environment.World;

public class Player {
    public int x, y;
    public Direction direction = Direction.UP;
    
    public int wood = 0;
    public int stone = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean canMove(World w, Direction dir) {
        int newX = x + dir.dx;
        int newY = y + dir.dy;
        
        // bounds?
        if (!isInBounds(w, newX, newY)) {
            return false;
        }
        
        // obstacles?
        int tile = w.getTile(newX, newY);
        return tile != World.TREE && tile != World.STONE;
    }

    public boolean move(World w, Direction dir) {
        if (!canMove(w, dir)) {
            return false;
        }
        
        x += dir.dx;
        y += dir.dy;
        direction = dir;
        return true;
    }

    /**
     * Harvest in front of the player
     */
    public boolean harvest(World w) {
        int targetX = x + direction.dx;
        int targetY = y + direction.dy;
        
        if (!isInBounds(w, targetX, targetY)) {
            return false;
        }
        
        int tile = w.getTile(targetX, targetY);
        
        if (tile == World.TREE) {
            w.setTile(targetX, targetY, World.GRASS);
            wood++;
            return true;
        }
        
        if (tile == World.STONE) {
            w.setTile(targetX, targetY, World.GRASS);
            stone++;
            return true;
        }
        
        return false;
    }

    /**
     * Turn to face a specific direction (without moving)
     * @param dir
     */
    public void face(Direction dir) {
        this.direction = dir;
    }

    public void turnRight() {
        direction = direction.rotateClockwise();
    }

    public void turnLeft() {
        direction = direction.rotateCounterClockwise();
    }

    public void turnAround() {
        direction = direction.opposite();
    }


    public int[] getTargetPosition() {
        return new int[]{x + direction.dx, y + direction.dy};
    }

    private boolean isInBounds(World w, int x, int y) {
        return x >= 0 && x < w.width && y >= 0 && y < w.height;
    }
}