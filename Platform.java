import java.awt.Rectangle;

public class Platform {

    private int x, y, width, height;

    // Movement
    private boolean moving = false;
    private int speed = 0;
    private int startX, endX;
    private boolean movingRight = true;
    private int originalX;
    private int lastMovement = 0;
public int getLastMovement() { return lastMovement; }
public boolean isMoving() { return moving; }


    public Platform(int xpos, int ypos, int w, int h) {
        x = xpos;
        y = ypos;
        width = w;
        height = h;
        originalX = xpos;
    }

    // WORLD COORDINATES ONLY
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Enable back‑and‑forth movement
    public void enableMovement(int speed, int startX, int endX) {
        this.moving = true;
        this.speed = speed;
        this.startX = startX;
        this.endX = endX;
    }

    public void updateMovement() {
    lastMovement = 0;

    if (!moving) return;

    if (movingRight) {
        x += speed;
        lastMovement = speed;
        if (x >= endX) movingRight = false;
    } else {
        x -= speed;
        lastMovement = -speed;
        if (x <= startX) movingRight = true;
    }
}


    public void resetMovement() {
        x = originalX;
        movingRight = true;
    }
}
