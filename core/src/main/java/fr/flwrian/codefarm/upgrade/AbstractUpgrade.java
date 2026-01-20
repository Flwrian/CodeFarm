package fr.flwrian.codefarm.upgrade;

import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.item.ItemType;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUpgrade implements Upgrade {
    
    protected final String id;
    protected final String name;
    protected final String description;
    protected final Map<ItemType, Long> cost;
    protected final int tier;
    protected final String[] prerequisites;
    protected boolean purchased;

    protected AbstractUpgrade(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.cost = builder.cost;
        this.tier = builder.tier;
        this.prerequisites = builder.prerequisites;
        this.purchased = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<ItemType, Long> getCost() {
        return new HashMap<>(cost);
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public String[] getPrerequisites() {
        return prerequisites != null ? prerequisites.clone() : new String[0];
    }

    @Override
    public boolean isPurchased() {
        return purchased;
    }

    @Override
    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public boolean canApply(GameContext ctx) {
        // Déjà acheté
        if (purchased) {
            return false;
        }
        
        // Vérifier les prérequis
        if (prerequisites != null) {
            for (String prereq : prerequisites) {
                Upgrade required = ctx.upgradeRegistry.get(prereq);
                if (required == null || !required.isPurchased()) {
                    return false;
                }
            }
        }
        
        // Vérifier les ressources
        for (Map.Entry<ItemType, Long> entry : cost.entrySet()) {
            if (!ctx.player.inventory.has(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String toString() {
        return name + " (" + id + ") - Tier " + tier;
    }

    // ========================================================================
    // Builder Pattern pour faciliter la création
    // ========================================================================
    
    public static abstract class Builder {
        protected String id;
        protected String name;
        protected String description;
        protected Map<ItemType, Long> cost = new HashMap<>();
        protected int tier = 1;
        protected String[] prerequisites;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder cost(ItemType type, long amount) {
            this.cost.put(type, amount);
            return this;
        }

        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }

        public Builder prerequisites(String... prereqs) {
            this.prerequisites = prereqs;
            return this;
        }

        public abstract AbstractUpgrade build();
    }
}