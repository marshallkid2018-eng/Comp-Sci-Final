import java.awt.Rectangle;

public class Obstacle {

    private int x, y, width, height;

    // Movement
    private boolean moving = false;
    private int speed = 0;
    private int startX, endX;
    private boolean movingRight = true;
    private int originalX;

    public Obstacle(int xpos, int ypos, int w, int h) {
        x = xpos;
        y = ypos;
        width = w;
        height = h;
        originalX = xpos;
    }

    // WORLD COORDINATES ONLY
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return width; }
    public int getH() { return height; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void enableMovement(int speed, int startX, int endX) {
        this.moving = true;
        this.speed = speed;
        this.startX = startX;
        this.endX = endX;
    }

    public void updateMovement() {
        if (!moving) return;

        if (movingRight) {
            x += speed;
            if (x >= endX) movingRight = false;
        } else {
            x -= speed;
            if (x <= startX) movingRight = true;
        }
    }

    public void resetMovement() {
        x = originalX;
        movingRight = true;
    }
}
