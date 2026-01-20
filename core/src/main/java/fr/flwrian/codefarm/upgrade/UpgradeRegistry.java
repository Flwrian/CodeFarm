package fr.flwrian.codefarm.upgrade;

import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.item.ItemType;

import java.util.*;
import java.util.stream.Collectors;

public class UpgradeRegistry {
    
    private final Map<String, Upgrade> upgrades;
    private final Map<UpgradeCategory, List<Upgrade>> upgradesByCategory;

    public UpgradeRegistry() {
        this.upgrades = new LinkedHashMap<>();
        this.upgradesByCategory = new EnumMap<>(UpgradeCategory.class);
        
        for (UpgradeCategory category : UpgradeCategory.values()) {
            upgradesByCategory.put(category, new ArrayList<>());
        }
        
        registerDefaultUpgrades();
    }

    private void registerDefaultUpgrades() {
        // ====================================================================
        // TIER 1 - Basic Player Upgrades
        // ====================================================================
        
        register(UpgradeCategory.PLAYER, 
            new SimpleUpgrade.SimpleBuilder("bigger_backpack_1")
                .name("Bigger Backpack I")
                .description("Increases inventory capacity by 50")
                .cost(ItemType.WOOD, 100L)
                .cost(ItemType.STONE, 50L)
                .tier(1)
                .effect(ctx -> {
                    long cap = ctx.player.inventory.getMaxCapacity();
                    ctx.player.inventory.setMaxCapacity(cap + 50);
                })
                .build()
        );

        register(UpgradeCategory.PLAYER,
            new SimpleUpgrade.SimpleBuilder("bigger_backpack_2")
                .name("Bigger Backpack II")
                .description("Increases inventory capacity by 100")
                .cost(ItemType.WOOD, 250L)
                .cost(ItemType.STONE, 125L)
                .tier(2)
                .prerequisites("bigger_backpack_1")
                .effect(ctx -> {
                    long cap = ctx.player.inventory.getMaxCapacity();
                    ctx.player.inventory.setMaxCapacity(cap + 100);
                })
                .build()
        );

        // register(UpgradeCategory.RESOURCES,
        //     new SimpleUpgrade.SimpleBuilder("sharp_tools")
        //         .name("Sharp Tools")
        //         .description("Harvest 50% faster")
        //         .cost(ItemType.WOOD, 150L)
        //         .cost(ItemType.STONE, 75L)
        //         .tier(1)
        //         .effect(ctx -> ctx.player.harvestSpeedMultiplier *= 0.5f)
        //         .build()
        // );

        // register(UpgradeCategory.PLAYER,
        //     new SimpleUpgrade.SimpleBuilder("running_shoes")
        //         .name("Running Shoes")
        //         .description("Move 50% faster")
        //         .cost(ItemType.WOOD, 200L)
        //         .cost(ItemType.STONE, 100L)
        //         .tier(1)
        //         .effect(ctx -> ctx.player.moveSpeedMultiplier *= 0.5f)
        //         .build()
        // );

        // // ====================================================================
        // // TIER 1 - Base Upgrades
        // // ====================================================================
        
        // register(UpgradeCategory.BASE,
        //     new SimpleUpgrade.SimpleBuilder("storage_expansion_1")
        //         .name("Storage Expansion I")
        //         .description("Increases base storage by 500")
        //         .cost(ItemType.WOOD, 300L)
        //         .cost(ItemType.STONE, 150L)
        //         .tier(1)
        //         .effect(ctx -> {
        //             long cap = ctx.base.storage.getMaxCapacity();
        //             if (cap < 0) cap = 1000;
        //             ctx.base.storage.setMaxCapacity(cap + 500);
        //         })
        //         .build()
        // );

        // // ====================================================================
        // // TIER 2 - Advanced Upgrades
        // // ====================================================================
        
        // register(UpgradeCategory.AUTOMATION,
        //     new SimpleUpgrade.SimpleBuilder("auto_deposit")
        //         .name("Auto-Deposit")
        //         .description("Automatically deposit when inventory is full")
        //         .cost(ItemType.WOOD, 500L)
        //         .cost(ItemType.STONE, 250L)
        //         .tier(2)
        //         .prerequisites("bigger_backpack_1")
        //         .effect(ctx -> ctx.player.autoDeposit = true)
        //         .build()
        // );

        // register(UpgradeCategory.RESOURCES,
        //     new SimpleUpgrade.SimpleBuilder("iron_tools")
        //         .name("Iron Tools")
        //         .description("Unlock iron harvesting and harvest 3x faster")
        //         .cost(ItemType.WOOD, 1000L)
        //         .cost(ItemType.STONE, 500L)
        //         .tier(2)
        //         .prerequisites("sharp_tools")
        //         .effect(ctx -> {
        //             ctx.player.canHarvestIron = true;
        //             ctx.player.harvestSpeedMultiplier *= 0.33f;
        //         })
        //         .build()
        // );
    }

    /**
     * Enregistrer un upgrade
     */
    public void register(UpgradeCategory category, Upgrade upgrade) {
        upgrades.put(upgrade.getId(), upgrade);
        upgradesByCategory.get(category).add(upgrade);
    }

    /**
     * Obtenir un upgrade par ID
     */
    public Upgrade get(String id) {
        return upgrades.get(id);
    }

    /**
     * Obtenir tous les upgrades
     */
    public List<Upgrade> getAll() {
        return new ArrayList<>(upgrades.values());
    }

    /**
     * Obtenir les upgrades par catégorie
     */
    public List<Upgrade> getByCategory(UpgradeCategory category) {
        return new ArrayList<>(upgradesByCategory.get(category));
    }

    /**
     * Obtenir les upgrades disponibles (achetables maintenant)
     */
    public List<Upgrade> getAvailable(GameContext ctx) {
        return upgrades.values().stream()
                .filter(u -> u.canApply(ctx))
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les upgrades achetés
     */
    public List<Upgrade> getPurchased() {
        return upgrades.values().stream()
                .filter(Upgrade::isPurchased)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les upgrades par tier
     */
    public List<Upgrade> getByTier(int tier) {
        return upgrades.values().stream()
                .filter(u -> u.getTier() == tier)
                .collect(Collectors.toList());
    }

    /**
     * Acheter un upgrade
     */
    public boolean purchase(String id, GameContext ctx) {
        Upgrade upgrade = upgrades.get(id);
        
        if (upgrade == null) {
            System.err.println("❌ Upgrade not found: " + id);
            return false;
        }

        if (!upgrade.canApply(ctx)) {
            System.err.println("❌ Cannot purchase upgrade: " + upgrade.getName());
            return false;
        }

        // Déduire le coût
        for (Map.Entry<ItemType, Long> entry : upgrade.getCost().entrySet()) {
            ctx.player.inventory.remove(entry.getKey(), entry.getValue());
        }

        // Appliquer l'effet
        upgrade.apply(ctx);
        
        System.out.println("✅ Purchased: " + upgrade.getName());
        return true;
    }

    /**
     * Vérifier si tous les prérequis sont remplis
     */
    public boolean hasPrerequisites(Upgrade upgrade) {
        for (String prereq : upgrade.getPrerequisites()) {
            Upgrade required = upgrades.get(prereq);
            if (required == null || !required.isPurchased()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtenir le prochain tier débloqué
     */
    public int getMaxUnlockedTier() {
        return upgrades.values().stream()
                .filter(Upgrade::isPurchased)
                .mapToInt(Upgrade::getTier)
                .max()
                .orElse(1);
    }
}