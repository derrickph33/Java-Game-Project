import processing.core.PImage;

import java.util.List;

public abstract class ActivityAction extends Entity{
    private double actionPeriod;
    private double animationPeriod;
    public ActivityAction(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public abstract void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0), this.animationPeriod);
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, null, null, repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }

    public double getActionPeriod() {
        return actionPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }
}