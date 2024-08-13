import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Sapling extends Plant implements Transform {
    private int health;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;


    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = CreateMethods.createStump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), getImageList(imageStore, Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= this.getHealthLimit()) {
            Tree tree = CreateMethods.createTree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(), getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN), getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList(Tree.TREE_KEY), TREE_HEALTH_MAX);

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction
                    (this, world, imageStore), this.getActionPeriod());
        }
    }

    private int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    private double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }

    public static void parseSapling(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            Entity entity = CreateMethods.createSapling(id, pt, imageStore.getImageList(SAPLING_KEY), health);
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", SAPLING_KEY, SAPLING_NUM_PROPERTIES));
        }
    }
    public void subHealth() {
        this.setHealth(this.getHealth() - 1);
    }
}