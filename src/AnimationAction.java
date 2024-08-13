import processing.core.PImage;

import java.util.List;

public abstract class AnimationAction extends ActivityAction {
    private int done;
    public AnimationAction(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int animationCompleted) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.done = 0;
    }

    public abstract void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public int getDone() {
        return done;
    }

    public void addAnimationCompleted() {
        this.done++;
    }
}
