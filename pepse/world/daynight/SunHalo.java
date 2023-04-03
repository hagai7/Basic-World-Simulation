package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun halo of the game.
 */
public class SunHalo {

    private static final float HALO_DIMENSION = 26;

    /**
     * Creates a new sun halo object.
     * @param gameObjects all game objects in the game.
     * @param layer the layer of this game object in the game.
     * @param sun sun game object.
     * @param color the color of the sun halo.
     * @return sun halo game object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        OvalRenderable yellowOval = new OvalRenderable(color);
        Vector2 haloDim = new Vector2(sun.getDimensions().x() + HALO_DIMENSION,
                sun.getDimensions().y() + HALO_DIMENSION);
        Vector2 haloCoordinates = new Vector2(sun.getTopLeftCorner().x() - HALO_DIMENSION/2,
                sun.getTopLeftCorner().y() - HALO_DIMENSION/2);
        GameObject halo = new GameObject(haloCoordinates, haloDim, yellowOval);
        gameObjects.addGameObject(halo, layer);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return halo;
    }


}
