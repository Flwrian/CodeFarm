package fr.flwrian.codefarm;

import fr.flwrian.codefarm.environment.World;
import fr.flwrian.codefarm.item.Inventory;
import fr.flwrian.codefarm.item.ItemType;

import fr.flwrian.codefarm.recipe.Recipe;

public class Player {
    public int x, y;
    public Direction direction = Direction.UP;
    
    public Inventory inventory;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.inventory = new Inventory(10);
    }

    public boolean canMove(World w, Direction dir) {
        int newX = x + dir.dx;
        int newY = y + dir.dy;
        
        if (!isInBounds(w, newX, newY)) {
            return false;
        }
        
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

    public boolean harvest(World w) {
        int targetX = x + direction.dx;
        int targetY = y + direction.dy;
        
        if (!isInBounds(w, targetX, targetY)) {
            return false;
        }
        
        int tile = w.getTile(targetX, targetY);
        
        if (tile == World.TREE) {
            if (inventory.isFull()) {
                return false;
            }
            w.setTile(targetX, targetY, World.GRASS);
            inventory.add(ItemType.WOOD, 1);
            return true;
        }
        
        if (tile == World.STONE) {
            if (inventory.isFull()) {
                return false;
            }
            w.setTile(targetX, targetY, World.GRASS);
            inventory.add(ItemType.STONE, 1);
            return true;
        }
        
        return false;
    }

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
    
    // lua helpers
    public long getWood() {
        return inventory.get(ItemType.WOOD);
    }
    
    public long getStone() {
        return inventory.get(ItemType.STONE);
    }

    // Crafting method
    public boolean craft(Recipe recipe) {

        // Check for enough space
        if (inventory.getRemainingSpace() < recipe.outputs.values().stream().mapToLong(Long::longValue).sum() - recipe.inputs.values().stream().mapToLong(Long::longValue).sum()) {
            System.out.println("Not enough space to craft!");
            return false;
        }
        
        // Check if player has all required items
        for (var entry : recipe.inputs.entrySet()) {
            if (inventory.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        // Remove inputs
        for (var entry : recipe.inputs.entrySet()) {
            inventory.remove(entry.getKey(), entry.getValue());
        }
        // Add outputs
        for (var entry : recipe.outputs.entrySet()) {
            inventory.add(entry.getKey(), entry.getValue());
        }
        return true;
    }
}
