package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Represents a leaf object.
 */
public class Leaf extends GameObject {
    private static final int MOVE_LOWER_BOUND = 3;
    private static final int MOVE_UPPER_BOUND = 5;
    private static final int FALL_LOWER_BOUND = 10;
    private static final int FALL_UPPER_BOUND = 30;
    private static final int FADE_LOWER_BOUND = 2;
    private static final int FADE_UPPER_BOUND = 8;
    private static final float FADE_OUT_TIME = 15;
    private static final float FALL_VELOCITY = 25;
    private static final Float HORIZONTAL_INITIAL_VALUE = 40f;
    private static final Float HORIZONTAL_FINAL_VALUE = -40f;
    private static final Float MOVE_INITIAL_VALUE = -8f;
    private static final Float MOVE_FINAL_VALUE = 8f;
    private static final float MOVE_TRANSITION_TIME = 3f;
    private static final float MOVE_TRANSITION_MULTIPLIER_X = 1.2f;
    private static final float MOVE_TRANSITION_MULTIPLIER_Y = 0.8f;
    private static final float MOVE_DIMENSION_TIME = 4;
    private static final float FALL_TRANSITION_TIME = 2;


    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final Random random;
    private Transition<Float> horizontalTransition;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object.
     *                      Can be null, in which case the GameObject will not be rendered.
     */

    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.dimensions = dimensions;
        this.topLeftCorner = topLeftCorner;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        random = new Random();
        scheduleLeafLife();

    }

    /**
     * Starts a new life for the leaves that were fallen.
     */
    private void leafNewLife() {
        setDimensions(dimensions);
        setTopLeftCorner(topLeftCorner);
        renderer().setRenderableAngle(0);
        renderer().fadeIn(0);
        scheduleLeafLife();
    }

    /**
     * Sets the lifetime of the leaves and their movement time.
     */
    private void scheduleLeafLife() {
        int randMove = random.nextInt(MOVE_UPPER_BOUND - MOVE_LOWER_BOUND);
        // allow generating numbers within a range and doesn't accept 2 params, only 1 param (upper bound)
        // so this is our way of mimicking two param behaviour.
        randMove += MOVE_LOWER_BOUND;
        new ScheduledTask(this, randMove, false, this::leafMove);

        int randFall = random.nextInt(FALL_UPPER_BOUND - FALL_LOWER_BOUND) + FALL_LOWER_BOUND;
        new ScheduledTask(this, randFall, false, this::leafFall);
    }

    /**
     * Sets the transition for the leaf's fall.
     */
    private void leafFall() {
        this.renderer().fadeOut(FADE_OUT_TIME, this::leafFadeOut);
        this.transform().setVelocityY(FALL_VELOCITY);
        this.horizontalTransition = new Transition<>(this, transform()::setVelocityX,
                HORIZONTAL_INITIAL_VALUE, HORIZONTAL_FINAL_VALUE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                FALL_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Sets the transitions for the leaf's movement.
     */
    private void leafMove() {
        new Transition<>(this, this.renderer()::setRenderableAngle, MOVE_INITIAL_VALUE, MOVE_FINAL_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, MOVE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new Transition<>(this, this::setDimensions, dimensions,
                new Vector2(dimensions.x() * MOVE_TRANSITION_MULTIPLIER_X,
                        dimensions.y() * MOVE_TRANSITION_MULTIPLIER_Y),
                Transition.CUBIC_INTERPOLATOR_VECTOR, MOVE_DIMENSION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Sets the time it takes for the leaf to fade out (gradually).
     */
    private void leafFadeOut() {
        int fadeOutRand = random.nextInt(FADE_UPPER_BOUND - FADE_LOWER_BOUND) + FADE_LOWER_BOUND;
        new ScheduledTask(this, fadeOutRand, false, this::leafNewLife);
    }


    /**
     * Called on every frame of a collision with a given object, including the first.
     *
     * @param other     the other GameObject that is collided.
     * @param collision stores information regarding a given collision between two GameObjects.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        transform().setVelocityX(0);
        removeComponent(horizontalTransition);
    }
}