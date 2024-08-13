import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class Ghost extends ActivityAction {
    public static final String GHOST_KEY = "ghost";
    public static final double GHOST_ANIMATION_PERIOD = .2;
    public static final double GHOST_ACTION_PERIOD = 1;
    public Ghost(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> ghostTarget = world.findNearest(entity.getPosition(), new ArrayList<>(List.of(Tomb.class)));

        if (ghostTarget.isEmpty()){
            world.removeEntity(scheduler, this);
        }

        if (ghostTarget.isPresent()) {
            Point tgtPos = ghostTarget.get().getPosition();

            if (moveTo(world, ghostTarget.get(), scheduler)) {
                world.removeEntity(scheduler, ghostTarget.get());
                DudeNotFull dude = new DudeNotFull(Dude.DUDE_KEY + "_" + ghostTarget.get().getId(), tgtPos, imageStore.getImageList(Dude.DUDE_KEY), this.getActionPeriod(), this.getAnimationPeriod(), Dude.DUDE_LIMIT, 0, Dude.DUDE_HEALTH);

                world.addEntity(dude);
                dude.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getActionPeriod());
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        BiPredicate<Point, Point> neighbors = (Point p1, Point p2) ->
                ((Math.abs(p1.getX() - p2.getX()) == 1) && (p1.getY() == p2.getY())) ||
                        ((p1.getX() == p2.getX()) && (Math.abs(p1.getY() - p2.getY()) == 1));
        List<Point> path = pathingStrategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && (!world.isOccupied(p) || ((world.isOccupied(p)) && (world.getOccupancyCell(p) instanceof Stump))),
                neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {
            return this.getPosition();
        }

        Point nextPos = path.get(0);
        if (world.isOccupied(nextPos) && !(world.getOccupancyCell(nextPos) instanceof Stump)) {
            return this.getPosition();
        }

        return nextPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
}