import processing.core.PImage;

import java.util.List;

public class UFO extends AnimationAction {
    public static final String UFO_KEY = "ufo";
    public static final int UFO_ANIMATION_PERIOD = 0;
    public static final int UFO_ACTION_PERIOD = 1;
    public static final int UFO_NUM_PROPERTIES = 2;

    public UFO(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int animationCompleted) {
        super(id, position, images, actionPeriod, animationPeriod, animationCompleted);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (this.getDone() != 4) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getAnimationPeriod());
            addAnimationCompleted();
        } else {
            scheduler.unscheduleAllEvents(this);
            world.addEntity(this);
            AlienNotFull alien = CreateMethods.createAlienNotFull(Alien.ALIEN_KEY, this.getPosition(), Alien.ALIEN_ACTION_PERIOD,
                    Alien.ALIEN_ANIMATION_PERIOD, 1, imageStore.getImageList(Alien.ALIEN_KEY));
            world.addEntity(alien);
            alien.scheduleActions(scheduler, world, imageStore);
        }
    }

    public static void parseUFO(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == UFO_NUM_PROPERTIES) {
            Entity entity = CreateMethods.createUFO(id, pt, imageStore.getImageList(UFO_KEY), Double.parseDouble(properties[UFO_ACTION_PERIOD]), Double.parseDouble(properties[UFO_ANIMATION_PERIOD]));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", UFO_KEY, UFO_NUM_PROPERTIES));
        }
    }
}
