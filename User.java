import java.awt.Rectangle;

public class User {

    private int x, y, width, height;
    private double dx, dy;
    private boolean onGround;

    private double speed;
    private double maxSpeed;
    private double friction;

    public boolean movingLeft = false;
    public boolean movingRight = false;

    public User(int xpos, int ypos, int w, int h,
                double speed, double maxSpeed, double friction) {

        x = xpos;
        y = ypos;
        width = w;
        height = h;

        dx = 0;
        dy = 0;
        onGround = false;

        this.speed = speed;
        this.maxSpeed = maxSpeed;
        this.friction = friction;
    }

    // -------------------------
    // GETTERS / SETTERS
    // -------------------------
    public int getx() { return x; }
    public int gety() { return y; }
    public int getw() { return width; }
    public int geth() { return height; }

    public void setx(int xpos) { x = xpos; }
    public void setY(int ypos) { y = ypos; }
    public void setWidth(int w) { width = w; }
    public void setHeight(int h) { height = h; }

    public double getdx() { return dx; }
    public double getdy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    public boolean getOnGround() { return onGround; }
    public void setOnGround(boolean grounded) {
        onGround = grounded;
        if (grounded) dy = 0;
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double s) { speed = s; }

    public double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(double m) { maxSpeed = m; }

    public double getFriction() { return friction; }
    public void setFriction(double f) { friction = f; }

    // -------------------------
    // MOVEMENT
    // -------------------------
    public void applyGravity() {
        dy += 1;
        if (dy > 20) dy = 20;
    }

    public void updatePosition() {

    // --- Horizontal movement (ground + air) ---
    if (movingLeft)  dx -= speed;
    if (movingRight) dx += speed;

    // Clamp horizontal speed
    if (dx > maxSpeed) dx = maxSpeed;
    if (dx < -maxSpeed) dx = -maxSpeed;

    // --- Friction only on ground ---
    if (onGround && !movingLeft && !movingRight) {
        if (dx > 0) dx -= friction;
        if (dx < 0) dx += friction;
        if (Math.abs(dx) < 0.1) dx = 0;
    }

    // --- Apply movement ---
    x += dx;
    y += dy;
}


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void reset(int startX, int startY) {
        x = startX;
        y = startY;
        dx = 0;
        dy = 0;
        onGround = false;
        movingLeft = false;
        movingRight = false;
    }
}
