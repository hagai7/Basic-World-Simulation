package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun of the game.
 */
public class Sun {
    private static final float START_ANGLE = 180f;
    private static final float SUN_X_DIVISOR = 2;
    private static final double Y_DIV = 18;
    private static final float Y_DIVISOR = 12;
    private static final float SUN_COORDS = 100;
    private static final float SUN_DIMENSIONS = 50;

    /**
     * Calculates the center position of the sun.
     * @param windowDimensions the 2d dimensions of the screen.
     * @param angleInSky the rotation angle of the sun.
     * @return a two-dimensional vector that represents the position of the sun.
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky) {
        float xValue = (float) (Math.cos(Math.toRadians(angleInSky)) *
                windowDimensions.x() / SUN_X_DIVISOR + windowDimensions.x() / SUN_X_DIVISOR);
        float yValue = (float) (Math.sin(Math.toRadians(Math.PI - angleInSky)) *
                windowDimensions.y() / Y_DIV + windowDimensions.y() / Y_DIVISOR);
        return new Vector2(xValue, yValue);
    }

    /**
     * Creates a new sun object.
     * @param gameObjects all game objects in the game.
     * @param layer the layer of this game object in the game.
     * @param windowDimensions the 2d dimensions of the screen.
     * @param cycleLength the number of seconds a "day" takes.
     * @return sun game object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(new Vector2(SUN_COORDS, SUN_COORDS), new Vector2(SUN_DIMENSIONS,
                SUN_DIMENSIONS),
                ovalRenderable);
        // initial size and coordinates
        gameObjects.addGameObject(sun, layer);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        addTransition(windowDimensions, cycleLength, sun);
        return sun;
    }

    /**
     * Adds a new transition to the sun.
     * @param windowDimensions the 2d dimensions of the screen.
     * @param cycleLength the number of seconds a "day" takes.
     * @param sun sun game object.
     */
    private static void addTransition(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        new Transition<>(sun, // the game object being changed
                (angle) -> sun.setCenter(calcSunPosition(windowDimensions, angle)), // the method to call
                -START_ANGLE, // initial transition value
                START_ANGLE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value
    }
}
