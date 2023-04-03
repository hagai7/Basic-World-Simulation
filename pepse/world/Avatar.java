package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.*;
import java.awt.event.KeyEvent;

/**
 * Represents the avatar of the game that is controlled by the user.
 * It extends GameObject.
 */
public class Avatar extends GameObject {

    private static final float VELOCITY_X = 200;
    private static final float VELOCITY_Y = -300;
    private static final float ACCELERATION_Y = 300;
    public static final float AVATAR_SIZE = 100;

    private static Renderable standRender;
    private static ImageReader imageReader;

    //paths to avatar position images
    private static final String STAND_PATH = "assets/stand.png";
    private static final String RUN_PATH = "assets/run/Run";
    private static final String JUMP_PATH = "assets/jump/Jump";
    private static final String FLY_PATH = "assets/fly/Fly";
    
    //renderable arrays
    private static final Renderable[] runRenderables = new Renderable[4];
    private static final Renderable[] jumpRenderables = new Renderable[4];
    private static final Renderable[] flyRenderables = new Renderable[4];

    //animation renderables
    private static AnimationRenderable runAnimation;
    private static AnimationRenderable flyAnimation;
    private static AnimationRenderable jumpAnimation;

    private static UserInputListener inputListener;

    /**
     * Constructor for Avatar.
     * It extends the super's GameObject constructor.
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param dimensions the 2d dimensions of the object on the screen.
     * @param renderable the image object to display on the screen.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        transform().setAccelerationY(ACCELERATION_Y);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    /**
     * Creates a new avatar object
     * @param gameObjects all game objects in the game.
     * @param layer the layer of this game object in the game.
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param inputListener a listener capable of reading user keyboard inputs.
     * @param imageReader an object used to read images from the disc and render them.
     * @return Avatar object.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {

        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;

        //set renderables of all positions of the avatar
        standRender = imageReader.readImage(STAND_PATH, true);
        setRenderables(runRenderables, RUN_PATH);
        runAnimation =  new AnimationRenderable(runRenderables, 0.2);
        setRenderables(jumpRenderables, JUMP_PATH);
        jumpAnimation =  new AnimationRenderable(jumpRenderables, 0.2);
        setRenderables(flyRenderables, FLY_PATH);
        flyAnimation =  new AnimationRenderable(flyRenderables, 0.2);

        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_SIZE, AVATAR_SIZE), standRender);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * This method is overwritten from GameObject.
     * It moves the avatar according to the user input.
     * Sets renderables of avatar's different positions.
     * @param renderArr array of renderables to set.
     * @param path path of image.
     */
    private static void setRenderables(Renderable[] renderArr, String path){
        for (int i = 0; i < renderArr.length; i++) {
            renderArr[i] = imageReader.readImage(path + " (" + (i+1) + ").png", true);
        }
    }

    /**
     * This method is overwritten from GameObject.
     * It moves the avatar according to the user input.
     * The avatar can move left or right, jump and fly.
     * @param deltaTime unused.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float velocityX = 0;
        //move left
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            renderer().setRenderable(runAnimation);
            renderer().setIsFlippedHorizontally(true);
            velocityX -= VELOCITY_X;
            transform().setVelocityX(velocityX);
        }
        //move right
        else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocityX += VELOCITY_X;
            renderer().setRenderable(runAnimation);
            renderer().setIsFlippedHorizontally(false);
            transform().setVelocityX(velocityX);
        }
        //jump
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            transform().setVelocityY(VELOCITY_Y);
            renderer().setRenderable(jumpAnimation);
        }
        //fly
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)){
            transform().setVelocityY(0.5f * VELOCITY_Y);
            new ScheduledTask(this, 0.5f, false,
                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            renderer().setRenderable(flyAnimation);
        }
        //stand in place
        else{
            transform().setVelocityX(0);
            renderer().setRenderable(standRender);
        }
    }

    /**
     * Called on the first frame of a collision.
     * @param other the other GameObject that is collided.
     * @param collision stores information regarding a given collision between two GameObjects.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Block){
            transform().setVelocityY(0);
        }
    }
}