package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the bricks that make up the ground of the game.
 * It extends GameObject.
 */
public class Block extends GameObject {
    public static final int SIZE = 30;

    /**
     * Constructor for Block.
     * It extends the super's GameObject constructor, and also saves the strategy given.
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param renderable the image object to display on the screen.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);

        //ensures that no object passes through the block
        physics().preventIntersectionsFromDirection(Vector2.ZERO);

        //makes sure the block doesn't moves if gets collided
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}