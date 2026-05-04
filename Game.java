import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {

    private BufferedImage back;
    private int level, key;
    private char screen;

    private User player;
    private ArrayList<Platform> platforms;
    private ArrayList<Obstacle> obstacles;

    private int cameraX = 0;
    private FinishLine finishLine;

    private Image bg1, bg2, bg3;

    public Game() {
        new Thread(this).start();
        this.addKeyListener(this);
        setFocusable(true);

        key = 0;
        level = 1;
        screen = 'S';   // start on start screen

        player = new User(100, 100, 40, 40, .5, 5, .5);
        platforms = new ArrayList<>();
        obstacles = new ArrayList<>();

        bg1 = new ImageIcon("level1bg.jpg").getImage();
        bg2 = new ImageIcon("level2bg.jpg").getImage();
        bg3 = new ImageIcon("level3bg.jpg").getImage();
    }

    // -------------------------
    // CAMERA (WORLD STAYS STILL)
    // -------------------------
    public void updateCamera() {
        cameraX = player.getx() - 200;
    }

    // -------------------------
    // COLLISIONS (WORLD SPACE)
    // -------------------------
    public void checkCollisions() {

        player.setOnGround(false);

        for (Platform p : platforms) {
            Rectangle pr = player.getBounds();
            Rectangle plat = p.getBounds();

            if (pr.intersects(plat)) {

                // top
                // Landing on top of platform
if (player.gety() + player.geth() <= p.getY() + player.getdy()) {

    // Snap player to platform
    player.setY(p.getY() - player.geth());
    player.setOnGround(true);

    // --- PLATFORM RIDING FIX ---
    if (p.isMoving()) {
        player.setx(player.getx() + p.getLastMovement());
    }
}


                // bottom
                else if (player.gety() >= p.getY() + p.getHeight() - 5) {
                    player.setDy(0);
                    player.setY(p.getY() + p.getHeight());
                }

                // left
                else if (player.getx() + player.getw() <= p.getX() + 10) {
                    player.setx(p.getX() - player.getw());
                }

                // right
                else if (player.getx() >= p.getX() + p.getWidth() - 10) {
                    player.setx(p.getX() + p.getWidth());
                }
            }
        }

        // obstacles
        for (Obstacle o : obstacles) {
            if (player.getBounds().intersects(o.getBounds())) {
                screen = 'L';
            }
        }

        // finish line
        if (finishLine != null && player.getBounds().intersects(finishLine.getBounds())) {

            if (level == 3) {
                screen = 'W';
                return;
            }

            loadLevel(level + 1);

            switch (level) {
                case 2: buildLevel2(); break;
                case 3: buildLevel3(); break;
            }

            spawnPlayerOnFirstPlatform();
        }

        // fall off map (use a fixed world Y instead of getHeight(), which can be 0 early)
        if (player.gety() > 800) {   // adjust if your world is taller
            screen = 'L';
        }
    }

    // -------------------------
    // MAIN GAME LOOP FOR 'G'
    // -------------------------
    public void runLevel(Graphics g2d) {

        if (platforms.isEmpty()) {
            switch (level) {
                case 1: buildLevel1(); break;
                case 2: buildLevel2(); break;
                case 3: buildLevel3(); break;
            }
            spawnPlayerOnFirstPlatform();
        }

        player.applyGravity();

        if (player.movingLeft)  player.setDx(player.getdx() - player.getSpeed());
        if (player.movingRight) player.setDx(player.getdx() + player.getSpeed());

        for (Platform p : platforms) p.updateMovement();
        for (Obstacle o : obstacles) o.updateMovement();

        player.updatePosition();

        updateCamera();

        checkCollisions();

        switch (level) {
            case 1: drawLevel1(g2d); break;
            case 2: drawLevel2(g2d); break;
            case 3: drawLevel3(g2d); break;
        }

        drawPlayer(g2d);
    }

    // -------------------------
    // SCREEN SWITCH (S/G/W/L)
    // -------------------------
    public void screen(Graphics g2d) {
        switch (screen) {
            case 'S':
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 40));
                g2d.drawString("Press ENTER to Start", 200, 200);
                g2d.setFont(new Font("Arial", Font.PLAIN, 30));
                g2d.drawString("Press R to Reset", 200, 260);
                break;

            case 'G':
                runLevel(g2d);
                break;

            case 'W':
                g2d.setColor(Color.GREEN);
                g2d.setFont(new Font("Broadway", Font.BOLD, 50));
                g2d.drawString("YOU WIN!", 350, 300);
                g2d.setFont(new Font("Arial", Font.PLAIN, 30));
                g2d.drawString("Press R to Restart", 340, 360);
                break;

            case 'L':
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Broadway", Font.BOLD, 50));
                g2d.drawString("YOU LOSE!", 350, 300);
                g2d.setFont(new Font("Arial", Font.PLAIN, 30));
                g2d.drawString("Press R to Try Again", 330, 360);
                break;
        }
    }

    // -------------------------
    // DRAWING WITH CAMERA OFFSET
    // -------------------------
    public void drawPlayer(Graphics g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(player.getx() - cameraX, player.gety(), player.getw(), player.geth());
    }

    public void drawLevel1(Graphics g2d) {
        g2d.drawImage(bg1, -cameraX - 100, 0, 3000, 2000, null);

        g2d.setColor(Color.YELLOW);
        for (Platform p : platforms)
            g2d.fillRect(p.getX() - cameraX, p.getY(), p.getWidth(), p.getHeight());

        g2d.setColor(Color.RED);
        for (Obstacle o : obstacles)
            g2d.fillRect(o.getX() - cameraX, o.getY(), o.getW(), o.getH());

        if (finishLine != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(finishLine.getX() - cameraX, finishLine.getY(),
                         finishLine.getWidth(), finishLine.getHeight());
        }
    }

    public void drawLevel2(Graphics g2d) {
        g2d.drawImage(bg2, -cameraX - 100, 0, 3000, 2000, null);

        g2d.setColor(Color.BLACK);
        for (Platform p : platforms)
            g2d.fillRect(p.getX() - cameraX, p.getY(), p.getWidth(), p.getHeight());

        g2d.setColor(Color.RED);
        for (Obstacle o : obstacles)
            g2d.fillRect(o.getX() - cameraX, o.getY(), o.getW(), o.getH());

        if (finishLine != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(finishLine.getX() - cameraX, finishLine.getY(),
                         finishLine.getWidth(), finishLine.getHeight());
        }
    }

    public void drawLevel3(Graphics g2d) {
        g2d.drawImage(bg3, -cameraX - 100, 0, 3000, 2000, null);

        g2d.setColor(Color.WHITE);
        for (Platform p : platforms)
            g2d.fillRect(p.getX() - cameraX, p.getY(), p.getWidth(), p.getHeight());

        g2d.setColor(Color.RED);
        for (Obstacle o : obstacles)
            g2d.fillRect(o.getX() - cameraX, o.getY(), o.getW(), o.getH());

        if (finishLine != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(finishLine.getX() - cameraX, finishLine.getY(),
                         finishLine.getWidth(), finishLine.getHeight());
        }
    }

    // -------------------------
    // LEVEL BUILDERS
    // -------------------------
    private void buildLevel1() {
    if (!platforms.isEmpty()) return;

    // Ground platforms
    platforms.add(new Platform(0, 500, 600, 40));
    platforms.add(new Platform(650, 500, 600, 40));
    platforms.add(new Platform(1300, 500, 600, 40));
    platforms.add(new Platform(1950, 500, 600, 40));

    // Rising staircase
    platforms.add(new Platform(300, 430, 120, 30));
    platforms.add(new Platform(450, 380, 120, 30));
    platforms.add(new Platform(600, 330, 120, 30));
    platforms.add(new Platform(750, 280, 120, 30));

    // Mid‑air platforms
    platforms.add(new Platform(1000, 350, 150, 30));
    platforms.add(new Platform(1200, 300, 150, 30));
    platforms.add(new Platform(1450, 250, 150, 30));
    platforms.add(new Platform(1700, 200, 150, 30));

    // Moving platform
    Platform movingPlat = new Platform(900, 420, 120, 30);
    movingPlat.enableMovement(2, 900, 1200);
    platforms.add(movingPlat);

    // Obstacles
    obstacles.add(new Obstacle(700, 460, 40, 40));
    obstacles.add(new Obstacle(1300, 460, 40, 40));
    obstacles.add(new Obstacle(1600, 460, 40, 40));

    // Moving obstacle
    Obstacle movingSpike = new Obstacle(1500, 460, 40, 40);
    movingSpike.enableMovement(3, 1400, 1700);
    obstacles.add(movingSpike);

	Obstacle movingSpike2 = new Obstacle(2100, 460, 40, 40);
    movingSpike2.enableMovement(3, 2000, 2300);
    obstacles.add(movingSpike2);

    // Finish line
    finishLine = new FinishLine(2400, 300, 40, 200);
}


    private void buildLevel2() {
    if (!platforms.isEmpty()) return;

    // ============================
    // SECTION 1 — Ground Run
    // ============================
    platforms.add(new Platform(0, 500, 600, 40));
    platforms.add(new Platform(650, 500, 600, 40));
    platforms.add(new Platform(1300, 500, 700, 40));

    obstacles.add(new Obstacle(300, 480, 20, 20));
    obstacles.add(new Obstacle(900, 460, 40, 40));
    obstacles.add(new Obstacle(1500, 460, 40, 40));

    // ============================
    // SECTION 2 — Tall Staircase
    // ============================
    platforms.add(new Platform(400, 430, 120, 30));
    platforms.add(new Platform(550, 380, 120, 30));
    platforms.add(new Platform(700, 330, 120, 30));
    platforms.add(new Platform(850, 280, 120, 30));
    platforms.add(new Platform(1000, 230, 120, 30));

    obstacles.add(new Obstacle(750, 460, 40, 40));

    // ============================
    // SECTION 3 — Mid‑Air Zig‑Zag
    // ============================
    platforms.add(new Platform(1300, 350, 150, 30));
    platforms.add(new Platform(1500, 300, 150, 30));
    platforms.add(new Platform(1700, 250, 150, 30));
    platforms.add(new Platform(1900, 200, 150, 30));

    obstacles.add(new Obstacle(1600, 460, 40, 40));

    // ============================
    // SECTION 4 — Moving Platforms Gauntlet
    // ============================
    Platform moverA = new Platform(2100, 420, 150, 30);
    moverA.enableMovement(3, 2100, 2500);
    platforms.add(moverA);

    Platform moverB = new Platform(2400, 350, 150, 30);
    moverB.enableMovement(2, 2400, 2800);
    platforms.add(moverB);

    Platform moverC = new Platform(2700, 300, 150, 30);
    moverC.enableMovement(4, 2700, 3100);
    platforms.add(moverC);

    // Moving obstacles
    Obstacle spikeA = new Obstacle(2200, 460, 40, 40);
    spikeA.enableMovement(3, 2100, 2400);
    obstacles.add(spikeA);

    Obstacle spikeB = new Obstacle(2600, 460, 40, 40);
    spikeB.enableMovement(2, 2500, 2800);
    obstacles.add(spikeB);

    // ============================
    // SECTION 5 — Final Corridor
    // ============================
    platforms.add(new Platform(3000, 500, 800, 40));

    obstacles.add(new Obstacle(3100, 480, 20, 20));
    obstacles.add(new Obstacle(3300, 470, 30, 30));
    obstacles.add(new Obstacle(3500, 480, 20, 20));

    finishLine = new FinishLine(3700, 300, 40, 200);
}



    private void buildLevel3() {
    if (!platforms.isEmpty()) return;

    // ============================
    // SECTION 1 — Ground Intro
    // ============================
    platforms.add(new Platform(0, 500, 500, 40));
    platforms.add(new Platform(550, 500, 500, 40));
    platforms.add(new Platform(1100, 500, 600, 40));

    obstacles.add(new Obstacle(300, 460, 40, 40));
    obstacles.add(new Obstacle(900, 460, 40, 40));

    // ============================
    // SECTION 2 — Upper Route (High Jumps)
    // ============================
    platforms.add(new Platform(300, 350, 150, 30));
    platforms.add(new Platform(600, 300, 150, 30));
    platforms.add(new Platform(900, 250, 150, 30));
    platforms.add(new Platform(1200, 200, 150, 30));
    platforms.add(new Platform(1500, 150, 150, 30));

    obstacles.add(new Obstacle(1000, 460, 40, 40));

    // ============================
    // SECTION 3 — Middle Route (Moving Platforms)
    // ============================
    Platform mover1 = new Platform(700, 400, 150, 30);
    mover1.enableMovement(3, 700, 1100);
    platforms.add(mover1);

    Platform mover2 = new Platform(1100, 350, 150, 30);
    mover2.enableMovement(2, 1100, 1500);
    platforms.add(mover2);

    Platform mover3 = new Platform(1500, 300, 150, 30);
    mover3.enableMovement(4, 1500, 1900);
    platforms.add(mover3);

    obstacles.add(new Obstacle(1300, 460, 40, 40));
    obstacles.add(new Obstacle(1600, 460, 40, 40));

    // ============================
    // SECTION 4 — Long Moving Bridge
    // ============================
    Platform bridge1 = new Platform(2000, 420, 200, 30);
    bridge1.enableMovement(3, 2000, 2600);
    platforms.add(bridge1);

    Platform bridge2 = new Platform(2400, 380, 200, 30);
    bridge2.enableMovement(2, 2400, 3000);
    platforms.add(bridge2);

    Platform bridge3 = new Platform(2800, 340, 200, 30);
    bridge3.enableMovement(4, 2800, 3400);
    platforms.add(bridge3);

    obstacles.add(new Obstacle(2200, 460, 40, 40));
    obstacles.add(new Obstacle(2600, 460, 40, 40));
    obstacles.add(new Obstacle(3000, 460, 40, 40));

    // ============================
    // SECTION 5 — Final Vertical Climb
    // ============================
    platforms.add(new Platform(3300, 450, 150, 30));
    platforms.add(new Platform(3500, 400, 150, 30));
    platforms.add(new Platform(3700, 350, 150, 30));
    platforms.add(new Platform(3900, 300, 150, 30));

    obstacles.add(new Obstacle(3600, 460, 40, 40));

    finishLine = new FinishLine(4100, 200, 40, 200);
}


    public void loadLevel(int newLevel) {
        level = newLevel;
        for (Platform p : platforms) p.resetMovement();
        for (Obstacle o : obstacles) o.resetMovement();
        platforms.clear();
        obstacles.clear();
        finishLine = null;
    }

    private Platform getStartPlatform() {
        if (platforms.isEmpty()) return null;
        Platform best = platforms.get(0);
        for (Platform p : platforms) {
            if (p.getX() < best.getX()) best = p;
        }
        return best;
    }

    public void spawnPlayerOnFirstPlatform() {
        Platform start = getStartPlatform();
        if (start != null) {
            int spawnX = start.getX() + start.getWidth() / 4;
            int spawnY = start.getY() - player.geth();
            player.reset(spawnX, spawnY);
            cameraX = player.getx() - 200;
        }
    }

    // -------------------------
    // THREAD LOOP
    // -------------------------
    public void run() {
        try {
            while (true) {
                Thread.sleep(5);
                repaint();
            }
        } catch (Exception e) {}
    }

    // -------------------------
    // PAINT
    // -------------------------
    public void paint(Graphics g) {
        Graphics2D twoDgraph = (Graphics2D) g;
        if (back == null)
            back = (BufferedImage)(createImage(getWidth(), getHeight()));

        Graphics g2d = back.createGraphics();
        g2d.clearRect(0, 0, getWidth(), getHeight());

        screen(g2d);

        twoDgraph.drawImage(back, null, 0, 0);
    }

    // -------------------------
    // INPUT
    // -------------------------
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (screen == 'S' && key == KeyEvent.VK_ENTER) {
            screen = 'G';
            level = 1;
            loadLevel(1);
            buildLevel1();
            spawnPlayerOnFirstPlatform();
        }

		if (screen == 'G' && key == KeyEvent.VK_P) {
    screen = 'G';
    loadLevel(level + 1);

    switch (level) {
        case 1: buildLevel1(); break;
        case 2: buildLevel2(); break;
        case 3: buildLevel3(); break;
    }

    spawnPlayerOnFirstPlatform();
}


        if (key == KeyEvent.VK_R) {
    // Restart the CURRENT level, not level 1
    screen = 'G';

    // Clear old level data
    platforms.clear();
    obstacles.clear();
    finishLine = null;

    // Reload the same level
    loadLevel(level);

    switch (level) {
        case 1: buildLevel1(); break;
        case 2: buildLevel2(); break;
        case 3: buildLevel3(); break;
    }

    // Respawn player on first platform
    spawnPlayerOnFirstPlatform();
}


        if (screen == 'G') {
            if (key == KeyEvent.VK_LEFT)  player.movingLeft = true;
            if (key == KeyEvent.VK_RIGHT) player.movingRight = true;
            if (key == KeyEvent.VK_SPACE && player.getOnGround()) {
                player.setDy(-15);
                player.setOnGround(false);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)  player.movingLeft = false;
        if (key == KeyEvent.VK_RIGHT) player.movingRight = false;
    }

    public void keyTyped(KeyEvent e) {}
}
