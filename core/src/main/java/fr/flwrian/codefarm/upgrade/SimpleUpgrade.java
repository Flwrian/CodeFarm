package fr.flwrian.codefarm.upgrade;

import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.item.ItemType;

public class SimpleUpgrade extends AbstractUpgrade {
    
    private final UpgradeEffect effect;

    private SimpleUpgrade(SimpleBuilder builder) {
        super(builder);
        this.effect = builder.effect;
    }

    @Override
    public void apply(GameContext ctx) {
        if (!canApply(ctx)) {
            throw new IllegalStateException("Cannot apply upgrade: " + id);
        }
        
        effect.apply(ctx);
        setPurchased(true);
    }

    public static class SimpleBuilder extends Builder {
        private UpgradeEffect effect;

        public SimpleBuilder(String id) {
            super(id);
        }

        public SimpleBuilder effect(UpgradeEffect effect) {
            this.effect = effect;
            return this;
        }

        @Override
        public SimpleBuilder name(String name) {
            super.name(name);
            return this;
        }

        @Override
        public SimpleBuilder description(String description) {
            super.description(description);
            return this;
        }

        @Override
        public SimpleBuilder cost(ItemType type, long amount) {
            super.cost(type, amount);
            return this;
        }

        @Override
        public SimpleBuilder tier(int tier) {
            super.tier(tier);
            return this;
        }

        @Override
        public SimpleBuilder prerequisites(String... prereqs) {
            super.prerequisites(prereqs);
            return this;
        }

        @Override
        public SimpleUpgrade build() {
            if (effect == null) {
                throw new IllegalStateException("Effect must be set");
            }
            return new SimpleUpgrade(this);
        }
    }
}