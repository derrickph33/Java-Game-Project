import processing.core.PImage;

import java.util.List;

public abstract class Alien extends ActivityAction {
    public static final String ALIEN_KEY = "alien";
    public static final int ALIEN_ACTION_PERIOD = 1;
    public static final int ALIEN_ANIMATION_PERIOD = 1;
    private int resourceLimit;
    private int resourceCount;
    public Alien(String id, Point position,
                     double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount, List<PImage> images)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }
    public abstract Point nextPosition(WorldModel world, Point destPos);

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int addResourceCount() {
        return this.resourceCount++;
    }
}
