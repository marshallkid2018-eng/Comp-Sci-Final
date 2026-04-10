import java.awt.Rectangle;

public class User {
    private int x, y, width, height;
    private double dx, dy;
    private boolean onGround;
    private double speed;      // acceleration
    private double maxSpeed;   // max horizontal speed
    private double friction;   // friction / deceleration
   // slows player when no key is pressed
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
    
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }


    

    public void setx(int xpos) {
        x = xpos;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public int getw() {
        return width;
    }

    public int geth() {
        return height;
    }

    
    public double getdx() {
        return dx;
    }

    public double getdy() {
        return dy;
    }
    

    public void setdx(double dx2) {
        dx = dx2;
    }

    public void setdy(double dy2) {
        dy = dy2;
    }

    public void applyGravity() {
    dy += 1; // gravity strength
    if (dy > 20) dy =20;
    }
    
    public void updatePosition() {
        // Apply friction when no movement keys are pressed
        if (!movingLeft && !movingRight) {
            if (dx > 0) dx -= friction;
            if (dx < 0) dx += friction;

            // Stop tiny leftover movement
            if (Math.abs(dx) < 0.1) dx = 0;
        }

        // Clamp speed
        if (dx > maxSpeed) dx = maxSpeed;
        if (dx < -maxSpeed) dx = -maxSpeed;

        x += dx;
        y += dy;
    }


    public void setOnGround(boolean grounded) {
        onGround = grounded;
        if (grounded) dy = 0;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean getOnGround() {
    	return onGround;
    }
    
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;

        this.dx = 0;
        this.dy = 0;

        this.onGround = false;

        this.movingLeft = false;
        this.movingRight = false;
    }


    
}

