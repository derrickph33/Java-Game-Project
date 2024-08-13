import processing.core.PImage;

import java.util.List;

public class Stump extends Entity{
    public static final String STUMP_KEY = "stump";
    private static final int STUMP_NUM_PROPERTIES = 0;
    public Stump(String id, Point position, List<PImage> images){
        super(id, position, images);
    }

    public static void parseStump(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == STUMP_NUM_PROPERTIES) {
            Entity entity = CreateMethods.createStump(id, pt, imageStore.getImageList(STUMP_KEY));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", STUMP_KEY, STUMP_NUM_PROPERTIES));
        }
    }
}