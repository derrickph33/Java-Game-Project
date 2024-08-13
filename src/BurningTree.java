import processing.core.PImage;

import java.util.List;

public class BurningTree extends ActivityAction {
    public static final String BURNING_TREE_KEY = "burningtree";
    public static final double BURNING_TREE_ACTION_PERIOD = .1;
    public static final double BURNING_TREE_ANIMATION_PERIOD = .1;
    public BurningTree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(entity, createActivityAction(world, imageStore), this.getActionPeriod());
    }
}
