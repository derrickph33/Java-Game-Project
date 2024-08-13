import processing.core.PImage;

import java.util.List;

public class House extends Entity {
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;

    public House(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public static void parseHouse(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Entity entity = new House(id, pt, imageStore.getImageList(HOUSE_KEY));
            world.tryAddEntity(world, entity);
        } else {
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", HOUSE_KEY, HOUSE_NUM_PROPERTIES));
        }
    }
}