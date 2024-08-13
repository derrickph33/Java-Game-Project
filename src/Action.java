public abstract class Action {
    private ActivityAction entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Action(ActivityAction entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    abstract void executeAction(EventScheduler scheduler);

    public ActivityAction entity() {return this.entity;}

    public WorldModel world() {
        return this.world;
    }

    public ImageStore imageStore() {
        return this.imageStore;
    }
    public int getRepeatCount() {return this.repeatCount;}
}
