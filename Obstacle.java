import java.awt.Rectangle;

public class Obstacle {
    
    private int x, y, width, height;

    public Obstacle(int xpos, int ypos, int w, int h) {
        x = xpos;
        y = ypos;
        width = w;
        height = h;
    }
    
    public void shiftX(int amount) {
        x -= amount;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return width; }
    public int getH() { return height; }


    // Setters
    public void setX(int xpos) { x = xpos; }
    public void setY(int ypos) { y = ypos; }
    public void setW(int w) { width = w; }
    public void setH(int h) { height = h; }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }



}
