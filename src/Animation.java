public class Animation extends Action{

    public Animation(ActivityAction entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Animation createAnimationAction(ActivityAction entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        return new Animation(entity, null, null, repeatCount);
    }

    public void executeAction(EventScheduler scheduler) {
        this.entity().nextImage(this.entity());

        if (this.getRepeatCount() != 1) {
            scheduler.scheduleEvent(this.entity(), createAnimationAction(this.entity(), this.world(), this.imageStore(), Math.max(this.getRepeatCount() - 1, 0)), this.entity().getAnimationPeriod());
        }
    }
}