package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

/**
 * Manages the pepse game.
 */
public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    private static final int SEED = 22;
    private static final Color HALO_COLOR = new Color(0, 0, 0, 20);
    private static final int[] DELETABLE_LAYERS = new int[]{Layer.DEFAULT, Layer.STATIC_OBJECTS,
            Layer.STATIC_OBJECTS + 1};
    private static final float DELETE_THRESHOLD = 2f;
    private static final float ADD_THRESHOLD = 1f;
    private static final float CYCLE_MULTIPLIER = 2;
    private static final int HALO_LAYER = 10;

    //minimum and maximum x values of a current frame
    private int minX;
    private int maxX;

    private Vector2 windowDimensions;
    private Terrain terrain;
    private Avatar avatar;
    private Tree tree;
    
    @Override
    public void update(float deltaTime) {
        generateInfiniteWorld();
        deleteFarObjects();
        super.update(deltaTime);
    }

    /**
     * This method initializes a new game. It creates all game objects,
     * sets their values and initial positions and allow the start of a game.
     * @param imageReader an object used to read images from the disc and render them.
     * @param soundReader an object used to read sound files from the disc and render them.
     * @param inputListener a listener capable of reading user keyboard inputs.
     * @param windowController a controller used to control the window and its attributes.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.windowDimensions = windowDimensions;
        minX = 0;
        maxX = (int) windowDimensions.x();

        //create sky
        Sky.create(this.gameObjects(), windowDimensions, Layer.BACKGROUND);

        //create terrain
        Terrain terrain = new Terrain(this.gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(), SEED);
        this.terrain = terrain;
        terrain.createInRange(minX, maxX);

        //create night
        Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);

        //create sun
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(), CYCLE_LENGTH * CYCLE_MULTIPLIER);

        //create sunHalo
        GameObject sunHalo = SunHalo.create(gameObjects(), Layer.BACKGROUND + HALO_LAYER, sun, HALO_COLOR);
        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));

        //create tree
        Tree tree = new Tree(terrain::groundHeightAt, gameObjects(), Layer.DEFAULT, SEED);
        this.tree = tree;
        tree.createInRange(minX, maxX);

        //create avatar
        Vector2 initialAvatarLocation = new Vector2(windowDimensions.x() / 2,
                terrain.groundHeightAt(windowDimensions.x() / 2) - Avatar.AVATAR_SIZE);
        Avatar avatar = Avatar.create(gameObjects(), Layer.DEFAULT, initialAvatarLocation, inputListener,
                imageReader);
        this.avatar = avatar;

        //set camera on avatar (avatar always in the middle of the screen)
        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    /**
     * Generates new terrain and trees depending on location of avatar
     */
    private void generateInfiniteWorld() {
        // go right
        if (maxX - avatar.getCenter().x() < windowDimensions.x() * ADD_THRESHOLD) {
            terrain.createInRange(maxX, (int) (maxX + windowDimensions.x()));
            tree.createInRange(maxX, (int) (maxX + windowDimensions.x()));
            maxX = (int) (maxX + windowDimensions.x());
        // go left
        } else if (avatar.getCenter().x() - minX < windowDimensions.x() * ADD_THRESHOLD) {
            terrain.createInRange((int) (minX - windowDimensions.x()), minX);
            tree.createInRange((int) (minX - windowDimensions.x()), minX);
            minX = (int) (minX - windowDimensions.x());
        }
    }

    /**
     * Removes relevant game objects which are far from the avatar
     */
    private void deleteFarObjects() {
        // go right
        if (avatar.getCenter().x() - minX > DELETE_THRESHOLD * windowDimensions.x()) {
            for (int layer : DELETABLE_LAYERS) {
                for (GameObject block : gameObjects().objectsInLayer(layer)) {
                    float blockRightDistance = avatar.getCenter().x() - block.getCenter().x();
                    if (blockRightDistance > DELETE_THRESHOLD * windowDimensions.x()) {
                        gameObjects().removeGameObject(block, layer);
                    }
                }
            }
            minX = (int) (minX + windowDimensions.x());
        }
        // go left
        if (maxX - avatar.getCenter().x() > DELETE_THRESHOLD * windowDimensions.x()) {
            for (int layer : DELETABLE_LAYERS) {
                for (GameObject block : gameObjects().objectsInLayer(layer)) {
                    float blockLeftDistance = block.getCenter().x() - avatar.getCenter().x();
                    if (blockLeftDistance > DELETE_THRESHOLD * windowDimensions.x()) {
                        gameObjects().removeGameObject(block, layer);
                    }
                }
            }
            maxX = (int) (maxX - windowDimensions.x());
        }
    }

    /**
     * The main driver of the game.
     * @param args unused.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
