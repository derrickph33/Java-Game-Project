import processing.core.PImage;

import java.util.List;

public class CreateMethods {
    private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_HEALTH_LIMIT = 5;

    public static Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, images, 0, animationPeriod);
    }

    public static Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images, int healthLimit) {
        return new Tree(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static Stump createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }
    public static Sapling createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD,
                SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public static Fairy createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images, actionPeriod, animationPeriod);
    }

    public static AlienNotFull createAlienNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new AlienNotFull(id, position, actionPeriod, animationPeriod, resourceLimit, 0, images);
    }

    public static AlienFull createAlienFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new AlienFull(id, position, actionPeriod, animationPeriod, resourceLimit, 0, images);
    }


    public static UFO createUFO(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        return new UFO(id, position, images, actionPeriod, animationPeriod, 0);
    }

    public static Tomb createBrain(String id, Point position, List<PImage> images) {
        return new Tomb(id, position, images);
    }
}
