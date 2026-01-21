package fr.flwrian.codefarm.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import fr.flwrian.codefarm.item.Inventory;
import fr.flwrian.codefarm.item.ItemType;

public class InventoryMenu extends Window {
    private final fr.flwrian.codefarm.Player player;
    private final Skin skin;
    private final Table itemsTable;
    private final ScrollPane scrollPane;
    private final TextButton closeButton;
    private final Label hoverLabel;

    public InventoryMenu(Skin skin, fr.flwrian.codefarm.Player player) {
        super("Inventory", skin);
        this.skin = skin;
        this.player = player;

        itemsTable = new Table(skin);
        scrollPane = new ScrollPane(itemsTable, skin);
        closeButton = new TextButton("Close", skin);
    hoverLabel = new Label("", skin);

        setupLayout();
        setupListeners();

        setSize(400, 300);
        setPosition(Gdx.graphics.getWidth()/2f - 200, Gdx.graphics.getHeight()/2f - 150);
        setMovable(true);
        updateContents();
    }

    private void setupLayout() {
        Table main = new Table(skin);
        main.add(scrollPane).grow().pad(8);
        row();
        add(main).grow().colspan(2);
        row();
        add(hoverLabel).colspan(2).padTop(6);
        row();
        add(closeButton).colspan(2).padTop(8);
    }

    private void setupListeners() {
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                InventoryMenu.this.remove();
            }
        });
    }

    public void updateContents() {
        itemsTable.clear();
        Inventory inv = player.inventory;
        // Header (optional)
        // itemsTable.add("Item");
        // itemsTable.add("Count");
        itemsTable.row();
        for (ItemType type : ItemType.values()) {
            long count = inv.get(type);
            if (count <= 0) continue; // show only non-zero

            // Create a label for item name and attach a tooltip with the description
            Label nameLabel = new Label(type.displayName, skin);
            Label countLabel = new Label(String.valueOf(count), skin);

            // Show description in hoverLabel on mouse enter; clear on exit
            nameLabel.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    hoverLabel.setText(type.description + " (Tier " + type.tier + ")");
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    hoverLabel.setText("");
                }
            });

            itemsTable.add(nameLabel).left().pad(4);
            itemsTable.add(countLabel).right().pad(4);
            itemsTable.row();
        }
        // If empty, show message
        if (itemsTable.getChildren().size == 0) {
            itemsTable.add("Inventory empty").align(Align.center);
        }
    }
}
