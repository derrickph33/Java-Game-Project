import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import processing.core.PImage;

public abstract class Entity {
    private static final int PROPERTY_KEY = 0;
    private static final int PROPERTY_ID = 1;
    private static final int PROPERTY_COL = 2;
    private static final int PROPERTY_ROW = 3;
    private static final int ENTITY_NUM_PROPERTIES = 4;

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String id;


    public Entity(String id, Point position,
                  List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public List<PImage> getImages() {
        return this.images;
    }

    public static List<PImage> getImageList(ImageStore imageStore, String key) {
        return imageStore.images.getOrDefault(key, imageStore.defaultImages);
    }

    int getImageIndex() {
        return imageIndex;
    }

    public String getId() {return id;}

    public Point getPosition() {return position;}

    public Point setPosition(Point newPos) {
        return this.position = newPos;
    }

    public static void nextImage(Entity entity) {
        entity.imageIndex = entity.imageIndex + 1;
    }

    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

    public static void parseEntity(WorldModel world, String line, ImageStore imageStore) {
        String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= ENTITY_NUM_PROPERTIES) {
            String key = properties[PROPERTY_KEY];
            String id = properties[PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

            properties = properties.length == ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Obstacle.OBSTACLE_KEY -> Obstacle.parseObstacle(world, properties, pt, id, imageStore);
                case Dude.DUDE_KEY -> Dude.parseDude(world, properties, pt, id, imageStore);
                case Fairy.FAIRY_KEY -> Fairy.parseFairy(world, properties, pt, id, imageStore);
                case House.HOUSE_KEY -> House.parseHouse(world, properties, pt, id, imageStore);
                case Tree.TREE_KEY -> Tree.parseTree(world, properties, pt, id, imageStore);
                case Sapling.SAPLING_KEY -> Sapling.parseSapling(world, properties, pt, id, imageStore);
                case Stump.STUMP_KEY -> Stump.parseStump(world, properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }

}