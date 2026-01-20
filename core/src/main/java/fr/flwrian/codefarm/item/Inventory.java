package fr.flwrian.codefarm.item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private final Map<ItemType, Long> items;
    private long maxCapacity; // -1 = infinite
    
    public Inventory() {
        this(-1); // Infinite capacity by default
    }
    
    public Inventory(long maxCapacity) {
        this.items = new HashMap<>();
        this.maxCapacity = maxCapacity;
        
        // Initialize all items to 0
        for (ItemType type : ItemType.values()) {
            items.put(type, 0L);
        }
    }

    /**
     * Add items
     */
    public boolean add(ItemType type, long amount) {
        if (amount <= 0) return false;
        
        long current = items.getOrDefault(type, 0L);
        long newAmount = current + amount;
        
        // Check capacity
        if (maxCapacity > 0 && getTotalCount() + amount > maxCapacity) {
            return false; // Inventory full
        }
        
        items.put(type, newAmount);
        return true;
    }

    /**
     * Remove items
     */
    public boolean remove(ItemType type, long amount) {
        if (amount <= 0) return false;
        
        long current = items.getOrDefault(type, 0L);
        
        if (current < amount) {
            return false; // Not enough items
        }
        
        items.put(type, current - amount);
        return true;
    }

    /**
     * Check if we have enough items
     */
    public boolean has(ItemType type, long amount) {
        return get(type) >= amount;
    }

    /**
     * Get the quantity of an item
     */
    public long get(ItemType type) {
        return items.getOrDefault(type, 0L);
    }

    /**
     * Directly set the quantity (for cheat/debug)
     */
    public void set(ItemType type, long amount) {
        items.put(type, Math.max(0, amount));
    }

    /**
     * Completely clear the inventory
     */
    public void clear() {
        for (ItemType type : ItemType.values()) {
            items.put(type, 0L);
        }
    }

    /**
     * Transfer items to another inventory
     */
    public boolean transfer(Inventory target, ItemType type, long amount) {
        if (!this.has(type, amount)) {
            return false;
        }
        
        if (!target.add(type, amount)) {
            return false;
        }
        
        this.remove(type, amount);
        return true;
    }

    /**
     * Transfer ALL items of a type
     */
    public boolean transferAll(Inventory target, ItemType type) {
        long amount = this.get(type);
        if (amount <= 0) return false;
        
        return transfer(target, type, amount);
    }

    /**
     * Get the total number of items (all types combined)
     */
    public long getTotalCount() {
        return items.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    /**
     * Get all non-empty items
     */
    public Map<ItemType, Long> getAll() {
        Map<ItemType, Long> result = new HashMap<>();
        for (Map.Entry<ItemType, Long> entry : items.entrySet()) {
            if (entry.getValue() > 0) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Check if the inventory is empty
     */
    public boolean isEmpty() {
        return getTotalCount() == 0;
    }

    /**
     * Check if the inventory is full
     */
    public boolean isFull() {
    if (maxCapacity <= 0) return false; // Infinite capacity
        return getTotalCount() >= maxCapacity;
    }

    /**
     * Get remaining space
     */
    public long getRemainingSpace() {
        if (maxCapacity <= 0) return Long.MAX_VALUE;
        return maxCapacity - getTotalCount();
    }

    // Getters/Setters
    public long getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(long capacity) {
        this.maxCapacity = capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Inventory[");
        for (Map.Entry<ItemType, Long> entry : getAll().entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}