package fr.flwrian.codefarm.item;

public enum ItemType {
    // Tier 1 - Basic resources
    WOOD("Wood", "Basic wood from trees", 1),
    STONE("Stone", "Basic stone from rocks", 1),
    
    // Tier 2 - Advanced resources
    IRON("Iron", "Iron ore from mines", 2),
    COAL("Coal", "Coal for fuel", 2),
    
    // Tier 3 - Precious resources
    GOLD("Gold", "Precious gold", 3),
    GEM("Gem", "Rare gems", 3),
    
    // Tier 4 - Crafted items
    PLANK("Plank", "Processed wood", 2),
    IRON_BAR("Iron Bar", "Smelted iron", 3),
    TOOL("Tool", "Crafted tool", 3);

    public final String displayName;
    public final String description;
    public final int tier;

    ItemType(String displayName, String description, int tier) {
        this.displayName = displayName;
        this.description = description;
        this.tier = tier;
    }

    /**
     * Get type from string
     */
    public static ItemType fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}