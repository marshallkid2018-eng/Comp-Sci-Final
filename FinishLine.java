import java.awt.Rectangle;

public class FinishLine {
    private int x, y, width, height;

    public FinishLine(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void shiftX(int amount) {
        x -= amount;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
