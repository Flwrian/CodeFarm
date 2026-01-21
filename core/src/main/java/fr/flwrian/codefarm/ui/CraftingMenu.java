package fr.flwrian.codefarm.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import fr.flwrian.codefarm.item.Inventory;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import fr.flwrian.codefarm.recipe.Recipe;
import fr.flwrian.codefarm.recipe.RecipeManager;

public class CraftingMenu extends Window {
    private final java.util.List<Recipe> recipes;
    private final com.badlogic.gdx.scenes.scene2d.ui.List<Recipe> recipeListWidget;
    private final ScrollPane recipeScrollPane;
    private final Table detailsTable;
    private final TextButton craftButton;
    private final Skin skin;
    private final fr.flwrian.codefarm.Player player;
    private final Label feedbackLabel;

    public CraftingMenu(Skin skin, RecipeManager recipeManager, fr.flwrian.codefarm.Player player) {
        super("Crafting", skin);
        this.skin = skin;
        this.player = player;
        this.recipes = new java.util.ArrayList<>(recipeManager.all());
        // Create a List style programmatically to avoid relying on skin having
        // List$ListStyle
        List.ListStyle listStyle = new List.ListStyle();
        try {
            listStyle.font = skin.getFont("font");
        } catch (Exception e) {
            listStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        }
        try {
            listStyle.selection = skin.getDrawable("cell");
        } catch (Exception e) {
            listStyle.selection = null;
        }
        // If no selection drawable provided by skin, create a tiny transparent one to avoid NPE
        if (listStyle.selection == null) {
            Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pm.setColor(1f, 1f, 1f, 0.05f);
            pm.fill();
            Texture t = new Texture(pm);
            pm.dispose();
            listStyle.selection = new TextureRegionDrawable(new TextureRegion(t));
        }
        listStyle.fontColorSelected = skin.has("white", com.badlogic.gdx.graphics.Color.class) ? skin.getColor("white")
                : Color.WHITE;
        listStyle.fontColorUnselected = skin.has("gray", com.badlogic.gdx.graphics.Color.class) ? skin.getColor("gray")
                : Color.LIGHT_GRAY;
        // avoid mutating the shared skin font (don't change font scale here)
        this.recipeListWidget = new com.badlogic.gdx.scenes.scene2d.ui.List<>(listStyle);
        this.recipeListWidget.setItems(this.recipes.toArray(new Recipe[0]));
        this.recipeScrollPane = new ScrollPane(this.recipeListWidget, skin);
        this.detailsTable = new Table(skin);
        this.craftButton = new TextButton("Craft", skin);
        this.feedbackLabel = new Label("", skin);
        setupLayout();
        setupListeners();
        setSize(600, 400);
        // scale the whole window content down to make UI elements smaller without
        // touching the skin
        setTransform(true);
        setScale(0.85f);
        setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f - 200);
        setMovable(true);
    }

    private void setupLayout() {
        Table mainTable = new Table(skin);
        mainTable.add(recipeScrollPane).width(200).growY().padRight(10);
        mainTable.add(detailsTable).grow();
        row();
        add(mainTable).grow().colspan(2);
        row();
        add(craftButton).colspan(2).padTop(10);
        row();
        add(feedbackLabel).colspan(2).padTop(6);
        updateDetails(null);
    }

    private void setupListeners() {
        recipeListWidget.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Recipe selected = recipeListWidget.getSelected();
                updateDetails(selected);
            }
        });
        craftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Recipe selected = recipeListWidget.getSelected();
                if (selected == null)
                    return;

                // Check for missing ingredients
                Inventory inv = player.inventory;
                boolean missing = false;
                for (var entry : selected.inputs.entrySet()) {
                    if (!inv.has(entry.getKey(), entry.getValue())) {
                        missing = true;
                        break;
                    }
                }
                if (missing) {
                    showFeedback("Missing ingredients");
                    return;
                }

                // Check for space
                long needed = selected.outputs.values().stream().mapToLong(Long::longValue).sum()
                        - selected.inputs.values().stream().mapToLong(Long::longValue).sum();
                if (inv.getRemainingSpace() < needed) {
                    showFeedback("Not enough space in inventory");
                    return;
                }

                boolean ok = player.craft(selected);
                if (ok) {
                    showFeedback("Crafted");
                    updateDetails(selected);
                } else {
                    showFeedback("Craft failed");
                }
            }
        });
    }

    private void showFeedback(String text) {
        feedbackLabel.setText(text);
        // clear after 3 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                feedbackLabel.setText("");
            }
        }, 3f);
    }

    private void updateDetails(Recipe recipe) {
        detailsTable.clear();
        if (recipe == null) {
            detailsTable.add("Select a recipe").align(Align.center);
            return;
        }
        detailsTable.add("Ingredients:").left().row();
        for (var entry : recipe.inputs.entrySet()) {
            detailsTable.add("- " + entry.getKey().displayName + ": " + entry.getValue()).left().row();
        }
        detailsTable.add("Result:").padTop(10).left().row();
        for (var entry : recipe.outputs.entrySet()) {
            detailsTable.add(entry.getKey().displayName + " x" + entry.getValue()).left().row();
        }
    }

    // Optionally, override toString for Recipe to display a readable name in the
    // list
    // (Sinon, la liste affichera l'identifiant de l'objet Recipe)
}
