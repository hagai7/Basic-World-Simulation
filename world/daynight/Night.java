package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the transition between day and night at the game.
 */
public class Night {
    private static final Color NIGHT_COLOR = Color.decode("#000000");
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";

    /**
     * Creates a new night object
     * @param gameObjects all game objects in the game.
     * @param layer the layer of this game object in the game.
     * @param windowDimensions the 2d dimensions of the screen.
     * @param cycleLength the number of seconds a "day" takes.
     * @return night game object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        RectangleRenderable rectangleRenderable = new RectangleRenderable(NIGHT_COLOR);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, rectangleRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        new Transition<>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null); // nothing further to execute upon reaching final value
        night.setTag(NIGHT_TAG); // for debug
        return night;
    }
}
