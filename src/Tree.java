import processing.core.PImage;

import java.util.List;

public class Tree extends Plant implements Transform {
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final int TREE_HEALTH_MAX = 3;

    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    public static void parseTree(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Entity entity = CreateMethods.createTree(id, pt, Double.parseDouble(properties[TREE_ACTION_PERIOD]), Double.parseDouble(properties[TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[TREE_HEALTH]), imageStore.getImageList(TREE_KEY), 3);
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", TREE_KEY, TREE_NUM_PROPERTIES));
        }
    }

    public void subHealth() {
        this.setHealth(this.getHealth() - 1);
    }
}