package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

/**
 * Represents a tree object.
 */
public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAVES_COLOR = new Color(50, 200, 30);
    private static final int TREE_CHANCE = 15;
    private static final int TREE_HEIGHT_UPPER_BOUND = 14;
    private static final int TREE_HEIGHT_LOWER_BOUND = 6;
    private static final int LEAF_SQUARE = 2;
    private static final float LEAF_SIZE = 30;
    private final Random random;
    private final Function<Float, Float> heightFunction;
    private final GameObjectCollection gameObjects;
    private final int layer;

    /**
     * Constructor for Terrain.
     * @param heightFunction function that gets the ground height.
     * @param gameObjects all game objects in the game.
     * @param layer the layer of the ground blocks.
     */
    public Tree(Function<Float, Float> heightFunction, GameObjectCollection gameObjects, int layer,
                int seed) {
        this.heightFunction = heightFunction;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.random = new Random(seed);
    }

    /**
     * Creates trees in given range (the x range of the ground)
     * @param minX first x value in range.
     * @param maxX last x value in range.
     */
    public void createInRange(int minX, int maxX) {
        // for each block in range, plant trees randomly (1/10 chance)
        for (int xValue = minX; xValue < maxX; xValue += Block.SIZE) {
            float groundHeight = this.heightFunction.apply((float) xValue) - Block.SIZE;
            if (this.random.nextInt(TREE_CHANCE) == TREE_CHANCE - 1) {
                // now the tree height is also random
                int trunkHeight = this.random.nextInt(TREE_HEIGHT_UPPER_BOUND - TREE_HEIGHT_LOWER_BOUND);
                trunkHeight += TREE_HEIGHT_LOWER_BOUND;
                // add trunk blocks for each tree iteratively
                addTrunk(trunkHeight, xValue, groundHeight);
                // add leaves
                addLeaves(trunkHeight, xValue, groundHeight);
            }
        }
    }

    /**
     * Creates trunk for current tree.
     * @param trunkHeight height of current trunk.
     * @param xValue x coordinate of current trunk.
     * @param groundHeight ground height at given x coordinate.
     */

    private void addTrunk(int trunkHeight, int xValue, float groundHeight) {
        for (int currentTrunkY = 0; currentTrunkY < trunkHeight; currentTrunkY++) {
            Vector2 location = new Vector2(xValue, groundHeight - currentTrunkY * Block.SIZE);
            RectangleRenderable renderableTrunk = new RectangleRenderable(TRUNK_COLOR);
            Block trunk = new Block(location, renderableTrunk);
            gameObjects.addGameObject(trunk, this.layer);
        }
    }

    /**
     * Creates leaves for current tree.
     * @param trunkHeight height of current trunk.
     * @param xValue x coordinate of current trunk.
     * @param groundHeight ground height at given x coordinate.
     */
    private void addLeaves(int trunkHeight, int xValue, float groundHeight) {
        for (int leafHeight = trunkHeight - LEAF_SQUARE;
             leafHeight < trunkHeight + LEAF_SQUARE; leafHeight++) {
            for (int leafWidth = xValue - LEAF_SQUARE * Block.SIZE;
                 leafWidth <= xValue + LEAF_SQUARE * Block.SIZE; leafWidth += Block.SIZE) {
                Vector2 location = new Vector2(leafWidth, groundHeight - leafHeight * Block.SIZE);
                RectangleRenderable leafRenderable = new RectangleRenderable(
                        ColorSupplier.approximateColor(LEAVES_COLOR));
                Vector2 leafDim = new Vector2(LEAF_SIZE, LEAF_SIZE);
                Leaf leaf = new Leaf(location, leafDim, leafRenderable);
                gameObjects.addGameObject(leaf, this.layer);
            }
        }
    }

}
