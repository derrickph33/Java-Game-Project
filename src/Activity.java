public class Activity extends Action{

    public Activity(ActivityAction entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Activity createActivityAction(ActivityAction entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler){
        this.entity().executeActivity(this.entity(), this.world(), this.imageStore(), scheduler);
    }
}
