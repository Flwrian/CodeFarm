package fr.flwrian.codefarm.upgrade;

public enum UpgradeCategory {
    PLAYER("Player", "Upgrades for the player"),
    BASE("Base", "Base improvements"),
    AUTOMATION("Automation", "Automation upgrades"),
    RESOURCES("Resources", "Resource gathering"),
    CRAFTING("Crafting", "Crafting improvements");

    public final String displayName;
    public final String description;

    UpgradeCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}