import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class AlienFull extends Alien implements Transform {
    public AlienFull(String id, Point position,
                     double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount, List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, resourceCount, images);
    }

    public void executeActivity(Entity entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(entity.getPosition(), new ArrayList<>(List.of(UFO.class)));

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(entity, createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        AlienNotFull alien = CreateMethods.createAlienNotFull(this.getId(), this.getPosition(),
                this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

        world.removeEntity(scheduler, this);

        world.addEntity(alien);
        alien.scheduleActions(scheduler, world, imageStore);
        return true;
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
