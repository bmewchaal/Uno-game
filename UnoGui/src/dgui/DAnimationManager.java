package dgui;

import javax.swing.Timer;
import java.awt.Component;
import java.awt.Point;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A utility class for managing animations of components.
 */
public class DAnimationManager {
    /**
     * Animation types for different effects
     */
    public enum AnimationType {
        LINEAR,     // Linear motion
        EASE_IN,    // Starts slow, ends fast
        EASE_OUT,   // Starts fast, ends slow
        EASE_IN_OUT // Starts and ends slow, fast in the middle
    }
    
    /**
     * Animate a component moving from its current position to a new position.
     * 
     * @param component the component to animate
     * @param targetX the target x-coordinate
     * @param targetY the target y-coordinate
     * @param durationMs the animation duration in milliseconds
     * @param type the animation type
     * @return a CompletableFuture that completes when the animation finishes
     */
    public static CompletableFuture<Void> animateMove(
            Component component, 
            int targetX, 
            int targetY, 
            int durationMs,
            AnimationType type) {
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        int startX = component.getX();
        int startY = component.getY();
        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        
        // Calculate time per frame (assuming 60 FPS)
        int frameMs = 16; // ~60 FPS
        int frameCount = durationMs / frameMs;
        
        // Animation frame counter
        int[] currentFrame = {0};
        
        // Create and start the timer
        Timer timer = new Timer(frameMs, e -> {
            // Increment the frame counter
            currentFrame[0]++;
            
            // Calculate the progress (0.0 to 1.0)
            float progress = (float) currentFrame[0] / frameCount;
            
            // Apply easing function based on animation type
            float easedProgress = applyEasing(progress, type);
            
            // Calculate the new position
            int newX = startX + (int) (deltaX * easedProgress);
            int newY = startY + (int) (deltaY * easedProgress);
            
            // Update the component position
            component.setLocation(newX, newY);
            
            // Check if the animation is complete
            if (currentFrame[0] >= frameCount) {
                ((Timer) e.getSource()).stop();
                component.setLocation(targetX, targetY);
                future.complete(null);
            }
        });
        
        // Start the timer
        timer.start();
        
        return future;
    }
    
    /**
     * Animate a component along a path defined by a function.
     * 
     * @param component the component to animate
     * @param pathFunction a function that takes a progress value (0.0 to 1.0) and returns a Point
     * @param durationMs the animation duration in milliseconds
     * @param type the animation type
     * @return a CompletableFuture that completes when the animation finishes
     */
    public static CompletableFuture<Void> animatePath(
            Component component, 
            java.util.function.Function<Float, Point> pathFunction, 
            int durationMs,
            AnimationType type) {
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        // Calculate time per frame (assuming 60 FPS)
        int frameMs = 16; // ~60 FPS
        int frameCount = durationMs / frameMs;
        
        // Animation frame counter
        int[] currentFrame = {0};
        
        // Create and start the timer
        Timer timer = new Timer(frameMs, e -> {
            // Increment the frame counter
            currentFrame[0]++;
            
            // Calculate the progress (0.0 to 1.0)
            float progress = (float) currentFrame[0] / frameCount;
            
            // Apply easing function based on animation type
            float easedProgress = applyEasing(progress, type);
            
            // Get the new position from the path function
            Point newPosition = pathFunction.apply(easedProgress);
            
            // Update the component position
            component.setLocation(newPosition.x, newPosition.y);
            
            // Check if the animation is complete
            if (currentFrame[0] >= frameCount) {
                ((Timer) e.getSource()).stop();
                Point finalPosition = pathFunction.apply(1.0f);
                component.setLocation(finalPosition.x, finalPosition.y);
                future.complete(null);
            }
        });
        
        // Start the timer
        timer.start();
        
        return future;
    }
    
    /**
     * Animate a property of an object over time.
     * 
     * @param <T> the type of the animated object
     * @param object the object to animate
     * @param setter a function that sets the property on the object
     * @param startValue the starting value
     * @param endValue the ending value
     * @param durationMs the animation duration in milliseconds
     * @param type the animation type
     * @return a CompletableFuture that completes when the animation finishes
     */
    public static <T> CompletableFuture<Void> animateProperty(
            T object,
            Consumer<Float> setter,
            float startValue,
            float endValue,
            int durationMs,
            AnimationType type) {
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        float deltaValue = endValue - startValue;
        
        // Calculate time per frame (assuming 60 FPS)
        int frameMs = 16; // ~60 FPS
        int frameCount = durationMs / frameMs;
        
        // Animation frame counter
        int[] currentFrame = {0};
        
        // Create and start the timer
        Timer timer = new Timer(frameMs, e -> {
            // Increment the frame counter
            currentFrame[0]++;
            
            // Calculate the progress (0.0 to 1.0)
            float progress = (float) currentFrame[0] / frameCount;
            
            // Apply easing function based on animation type
            float easedProgress = applyEasing(progress, type);
            
            // Calculate the new value
            float newValue = startValue + deltaValue * easedProgress;
            
            // Update the property
            setter.accept(newValue);
            
            // Check if the animation is complete
            if (currentFrame[0] >= frameCount) {
                ((Timer) e.getSource()).stop();
                setter.accept(endValue);
                future.complete(null);
            }
        });
        
        // Start the timer
        timer.start();
        
        return future;
    }
    
    /**
     * Apply an easing function to a progress value.
     * 
     * @param progress the progress value (0.0 to 1.0)
     * @param type the animation type
     * @return the eased progress value
     */
    private static float applyEasing(float progress, AnimationType type) {
        switch (type) {
            case LINEAR:
                return progress;
            case EASE_IN:
                return progress * progress;
            case EASE_OUT:
                return 1 - (1 - progress) * (1 - progress);
            case EASE_IN_OUT:
                return progress < 0.5f 
                        ? 2 * progress * progress 
                        : 1 - (float)Math.pow(-2 * progress + 2, 2) / 2;
            default:
                return progress;
        }
    }
    
    /**
     * Create an arc path function for animating a component along an arc.
     * 
     * @param startX the starting x-coordinate
     * @param startY the starting y-coordinate
     * @param endX the ending x-coordinate
     * @param endY the ending y-coordinate
     * @param arcHeight the height of the arc
     * @return a function that returns a Point for a given progress value
     */
    public static java.util.function.Function<Float, Point> createArcPath(
            int startX, int startY, int endX, int endY, int arcHeight) {
        
        return progress -> {
            int x = startX + (int)((endX - startX) * progress);
            
            // Calculate y using a parabola to create an arc
            // y = a*x^2 + b*x + c where x is the progress (0-1)
            // We want y(0) = 0, y(0.5) = arcHeight, y(1) = 0
            // This gives us: y = -4*arcHeight*x^2 + 4*arcHeight*x
            float normalizedX = progress;
            float normalizedY = -4 * arcHeight * normalizedX * normalizedX 
                              + 4 * arcHeight * normalizedX;
            
            // Interpolate between start and end y, and add the arc offset
            int y = startY + (int)((endY - startY) * progress) - (int)normalizedY;
            
            return new Point(x, y);
        };
    }
}