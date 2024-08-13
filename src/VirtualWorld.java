import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public class VirtualWorld extends PApplet {
    private static String[] ARGS;
    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public String loadFile = "world.sav";
    public long startTimeMillis = 0;
    public double timeScale = 1.0;
    public static final String UFO_KEY = "ufo";

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.currentTime)/timeScale;
        this.update(frameTime);
        view.drawViewport(view);
    }

    public void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass());
        }

        if (world.isOccupied(pressed)) {
            System.out.println("Cannot place UFO here");
        } else {
            //need to add event in here to change background cells
            UFO ufo = CreateMethods.createUFO(UFO_KEY, pressed, imageStore.getImageList(UFO_KEY), .2, 0.4);
            world.addEntity(ufo);
            ufo.scheduleActions(scheduler, world, imageStore);

            int row = pressed.y;
            int col = pressed.x;

            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    Point pt = new Point(j, i);

                    if (!world.withinBounds(pt)) {
                        continue;
                    }
                    world.setBackgroundCell(pt, new Background("ufoEdge", imageStore.getImageList("ufoEdge")));
                    if (world.withinBounds(pt)) {
                        if (world.isOccupied(pt) && (world.getOccupancyCell(pt) instanceof Dude || world.getOccupancyCell(pt) instanceof Fairy)) {
                            world.removeEntityAt(pt);
                            Ghost ghost = new Ghost(Ghost.GHOST_KEY, pt, imageStore.getImageList(Ghost.GHOST_KEY), Ghost.GHOST_ACTION_PERIOD, Ghost.GHOST_ANIMATION_PERIOD);
                            world.addEntity(ghost);
                            ghost.scheduleActions(scheduler, world, imageStore);

                        } else if (world.isOccupied(pt) && (world.getOccupancyCell(pt) instanceof Tree || world.getOccupancyCell(pt) instanceof Sapling)) {
                            world.removeEntityAt(pt);
                            BurningTree bt = new BurningTree(BurningTree.BURNING_TREE_KEY, pt, imageStore.getImageList(BurningTree.BURNING_TREE_KEY),
                                    BurningTree.BURNING_TREE_ACTION_PERIOD, BurningTree.BURNING_TREE_ANIMATION_PERIOD);
                            world.addEntity(bt);
                            bt.scheduleActions(scheduler, world, imageStore);
                        }
                    }
                }
            }
        }
    }


    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.entities) {
            if (entity instanceof ActivityAction) {
                ActivityAction newentity = (ActivityAction) entity;
                newentity.scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private Point mouseToPoint() {
        return view.viewport.viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(world, in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(world, in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}