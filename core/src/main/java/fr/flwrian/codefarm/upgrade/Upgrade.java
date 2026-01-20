package fr.flwrian.codefarm.upgrade;

import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.item.ItemType;

import java.util.Map;

/**
 * Interface pour tous les upgrades du jeu
 */
public interface Upgrade {
    /**
     * ID unique de l'upgrade
     */
    String getId();
    
    /**
     * Nom affiché dans l'UI
     */
    String getName();
    
    /**
     * Description de l'effet
     */
    String getDescription();
    
    /**
     * Coût en ressources
     */
    Map<ItemType, Long> getCost();
    
    /**
     * Tier de l'upgrade (pour unlock progressif)
     */
    int getTier();
    
    /**
     * Prérequis (upgrades qui doivent être achetés avant)
     */
    String[] getPrerequisites();
    
    /**
     * Vérifie si l'upgrade peut être appliqué
     */
    boolean canApply(GameContext ctx);
    
    /**
     * Applique l'effet de l'upgrade
     */
    void apply(GameContext ctx);
    
    /**
     * Vérifie si l'upgrade a été acheté
     */
    boolean isPurchased();
    
    /**
     * Marque l'upgrade comme acheté
     */
    void setPurchased(boolean purchased);
}