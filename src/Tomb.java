import processing.core.PImage;

import java.util.List;

public class Tomb extends Entity {
    public static final String TOMB_KEY = "tomb";

    public Tomb(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
