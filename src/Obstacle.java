import processing.core.PImage;

import java.util.List;

public class Obstacle extends ActivityAction {
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;
    public Obstacle(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        throw new UnsupportedOperationException("Invalid function for type Obstacle : executeActivity");
    }

    public static void parseObstacle(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Entity entity = CreateMethods.createObstacle(id, pt, Double.parseDouble(properties[OBSTACLE_ANIMATION_PERIOD]), imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", OBSTACLE_KEY, OBSTACLE_NUM_PROPERTIES));
        }
    }
}

