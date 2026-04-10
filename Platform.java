import java.awt.Rectangle;

public class Platform {
    
    private int x, y, width, height;

    public Platform(int xpos, int ypos, int w, int h) {
        x = xpos;
        y = ypos;
        width = w;
        height = h;
    }
    
 // Move platform when camera scrolls
    public void shiftX(int amount) {
        x -= amount;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Setters
    public void setX(int xpos) { x = xpos; }
    public void setY(int ypos) { y = ypos; }
    public void setWidth(int w) { width = w; }
    public void setHeight(int h) { height = h; }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}