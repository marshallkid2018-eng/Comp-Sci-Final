import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.*; 


public class Game  extends JPanel implements Runnable, KeyListener{

	
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
		key = 0;
		level = 1; 
		screen = 'G';
		player = new User(100, 100, 40, 40, .5, 5, .5);
		platforms = new ArrayList<Platform>();
		obstacles = new ArrayList<Obstacle>();
		bg1 = new ImageIcon("level1bg.jpg").getImage();
		bg2 = new ImageIcon("level2bg.jpg").getImage();
		bg3 = new ImageIcon("level3bg.jpg").getImage();

		

		
		
	
	}

	public void screen( Graphics g2d){
		switch(screen){
			case 'S':
				 g2d.setColor(Color.BLACK);
				    g2d.drawString("Press ENTER to Start", 200, 200);
				    g2d.drawString("Press R to Reset", 200, 260);
				break;
			case 'G':
				
				

					runLevel(g2d);				
			break;
			case 'W':
			
			    g2d.setColor(Color.GREEN);
			    g2d.drawString("YOU WIN!", 400, 300);
			    g2d.setFont(new Font("Arial", Font.PLAIN, 30));
			    g2d.drawString("Press R to Restart", 420, 360);
			    
				break;
			case 'L':
				g2d.setColor(Color.RED);
			    g2d.drawString("YOU LOSE!", 400, 300);
			    g2d.setFont(new Font("Arial", Font.PLAIN, 30));
			    g2d.drawString("Press R to Try Again", 420, 360);
				break;
		}

	}

	
	
	public void run()
	   {
	   	try
	   	{
	   		while(true)
	   		{
	   		   Thread.currentThread().sleep(5);
	            repaint();
	         }
	      }
	   		catch(Exception e)
	      {
	      }
	  	}
	

	
	
	
	public void paint(Graphics g){
		
		Graphics2D twoDgraph = (Graphics2D) g; 
		if( back ==null)
			back=(BufferedImage)( (createImage(getWidth(), getHeight()))); 
		

		Graphics g2d = back.createGraphics();
	
		g2d.clearRect(0,0,getSize().width, getSize().height);
		
		g2d.setFont( new Font("Broadway", Font.BOLD, 50));

		screen(g2d);
		
		
	
		twoDgraph.drawImage(back, null, 0, 0);

	}

	public void drawPlayer(Graphics g2d) {
	    g2d.setColor(Color.BLUE); 
	    g2d.fillRect(player.getx(), player.gety(), player.getw(), player.geth());
	}

	
	public void checkCollisions() {
	    player.setOnGround(false);

	    // PLATFORM COLLISION
	    for (Platform p : platforms) {
	        Rectangle pr = player.getBounds();
	        Rectangle plat = p.getBounds();

	        if (pr.intersects(plat)) {

	            // Landing on top of platform
	            if (player.gety() + player.geth() <= p.getY() + player.getdy()) {
	                player.setY(p.getY() - player.geth());
	                player.setOnGround(true);
	            }

	            // Hitting platform from below
	            else if (player.gety() >= p.getY() + p.getHeight() - 5) {
	                player.setdy(0);
	                player.setY(p.getY() + p.getHeight());
	            }

	            // Running into platform from left
	            else if (player.getx() + player.getw() <= p.getX() + 10) {
	                player.setx(p.getX() - player.getw());
	            }

	            // Running into platform from right
	            else if (player.getx() >= p.getX() + p.getWidth() - 10) {
	                player.setx(p.getX() + p.getWidth());
	            }
	        }
	    }

	    // OBSTACLE COLLISION
	    for (Obstacle o : obstacles) {
	        if (player.getBounds().intersects(o.getBounds())) {
	            screen = 'L'; // lose screen
	        }
	    }
	    
	    if (finishLine != null && player.getBounds().intersects(finishLine.getBounds())) {

	        if (level == 3) {
	            screen = 'W';   // WIN SCREEN
	            return;
	        }

	        loadLevel(level + 1);

	        switch (level) {
	            case 2: buildLevel2(); break;
	            case 3: buildLevel3(); break;
	        }

	        spawnPlayerOnFirstPlatform();
	    }
	    
	 // FALLING OFF THE LEVEL
	    if (player.gety() > getHeight()) {
	        screen = 'L';
	    }




	}
	
	private Platform getStartPlatform() {
	    if (platforms.isEmpty()) return null;

	    Platform best = platforms.get(0);
	    for (Platform p : platforms) {
	        if (p.getX() < best.getX()) {
	            best = p;
	        }
	    }
	    return best;
	}

	
	public void updateCamera() {

	    // Lock camera to player instantly
	    int targetX = player.getx() - 200; // adjust offset as you like
	    int scrollAmount = targetX - cameraX;
	    cameraX = targetX;

	    // Shift world
	    for (Platform p : platforms) p.shiftX(scrollAmount);
	    for (Obstacle o : obstacles) o.shiftX(scrollAmount);
	    if (finishLine != null) finishLine.shiftX(scrollAmount);
	}



	
	public void runLevel(Graphics g2d) {

	    player.applyGravity();
	    if (player.movingLeft) {
	        player.setDx(player.getdx() - player.getSpeed());
	    }
	    if (player.movingRight) {
	        player.setDx(player.getdx() + player.getSpeed());
	    }

	    player.updatePosition();
	    updateCamera();
	    checkCollisions();

	    switch (level) {
	        case 1:
	            drawLevel1(g2d);
	            break;

	        case 2:
	            drawLevel2(g2d);
	            break;

	        case 3:
	            drawLevel3(g2d);
	            break;
	    }

	    drawPlayer(g2d);
	}
	
	private void buildLevel1() {
	    if (!platforms.isEmpty()) return;

	    platforms.add(new Platform(0, 500, 600, 40));
	    platforms.add(new Platform(650, 480, 500, 40));
	    platforms.add(new Platform(1200, 520, 600, 40));
	    platforms.add(new Platform(1850, 470, 700, 40));

	    platforms.add(new Platform(300, 450, 120, 30));
	    platforms.add(new Platform(420, 400, 120, 30));
	    platforms.add(new Platform(540, 350, 120, 30));

	    platforms.add(new Platform(900, 350, 150, 30));
	    platforms.add(new Platform(1150, 300, 150, 30));
	    platforms.add(new Platform(1400, 250, 150, 30));
	    platforms.add(new Platform(1670, 250, 150, 30));

	    obstacles.add(new Obstacle(700, 460, 40, 40));
	    obstacles.add(new Obstacle(1300, 500, 40, 40));
	    obstacles.add(new Obstacle(1430, 225, 40, 40));
	    obstacles.add(new Obstacle(1730, 500, 40, 40));

	    finishLine = new FinishLine(2300, 300, 40, 200);
	}

	private void buildLevel2() {
	    if (!platforms.isEmpty()) return;

	    platforms.add(new Platform(0, 500, 400, 40));
	    platforms.add(new Platform(500, 500, 300, 40));
	    platforms.add(new Platform(900, 500, 400, 40));
	    platforms.add(new Platform(1400, 500, 500, 40));

	    platforms.add(new Platform(600, 420, 120, 30));
	    platforms.add(new Platform(700, 350, 120, 30));
	    platforms.add(new Platform(800, 280, 120, 30));
	    platforms.add(new Platform(900, 210, 120, 30));

	    platforms.add(new Platform(1300, 350, 200, 30));
	    platforms.add(new Platform(1600, 300, 200, 30));
	    platforms.add(new Platform(1900, 250, 200, 30));

	    obstacles.add(new Obstacle(450, 460, 40, 40));
	    obstacles.add(new Obstacle(1000, 460, 40, 40));
	    obstacles.add(new Obstacle(1500, 460, 40, 40));

	    finishLine = new FinishLine(2300, 300, 40, 200);
	}

	private void buildLevel3() {
	    if (!platforms.isEmpty()) return;

	    platforms.add(new Platform(0, 500, 300, 40));
	    platforms.add(new Platform(400, 500, 250, 40));
	    platforms.add(new Platform(750, 500, 300, 40));
	    platforms.add(new Platform(1150, 500, 350, 40));
	    platforms.add(new Platform(1600, 500, 400, 40));

	    platforms.add(new Platform(500, 420, 120, 30));
	    platforms.add(new Platform(515, 350, 120, 30));
	    platforms.add(new Platform(530, 280, 120, 30));
	    platforms.add(new Platform(545, 210, 120, 30));

	    platforms.add(new Platform(900, 350, 200, 30));
	    platforms.add(new Platform(1200, 300, 200, 30));
	    platforms.add(new Platform(1500, 250, 200, 30));
	    platforms.add(new Platform(1600, 250, 200, 30));
	    platforms.add(new Platform(1900, 150, 150, 30));
	    platforms.add(new Platform(2100, 100, 200, 30));
	    platforms.add(new Platform(2100, 420, 200, 30));

	    obstacles.add(new Obstacle(350, 460, 40, 40));
	    obstacles.add(new Obstacle(800, 470, 40, 40));
	    obstacles.add(new Obstacle(1400, 460, 40, 40));
	    obstacles.add(new Obstacle(1800, 460, 40, 40));
	    obstacles.add(new Obstacle(1640, 230, 40, 40));
	    obstacles.add(new Obstacle(2140, 400, 40, 40));

	    finishLine = new FinishLine(2500, 300, 40, 200);
	}


	public void drawLevel1(Graphics g2d) {
		
		g2d.drawImage(bg1, -cameraX - 100, 0, 3000, 2000, null);
		System.out.println(new java.io.File("level1bg.jpg").exists());


	    if (platforms.isEmpty()) {
	        buildLevel1();
	        spawnPlayerOnFirstPlatform();
	    }

	    g2d.setColor(Color.YELLOW);
	    for (Platform p : platforms)
	        g2d.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());

	    g2d.setColor(Color.RED);
	    for (Obstacle o : obstacles)
	        g2d.fillRect(o.getX(), o.getY(), o.getW(), o.getH());

	    g2d.setColor(Color.GREEN);
	    g2d.fillRect(finishLine.getX(), finishLine.getY(),
	                 finishLine.getWidth(), finishLine.getHeight());
	}

	
	public void drawLevel2(Graphics g2d) {
		g2d.drawImage(bg2, -cameraX - 100, 0, 3000, 2000, null);

		
	    if (platforms.isEmpty()) {
	        buildLevel2();
	        spawnPlayerOnFirstPlatform();
	    }

	    g2d.setColor(Color.BLACK);
	    for (Platform p : platforms)
	        g2d.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());

	    g2d.setColor(Color.RED);
	    for (Obstacle o : obstacles)
	        g2d.fillRect(o.getX(), o.getY(), o.getW(), o.getH());

	    g2d.setColor(Color.GREEN);
	    g2d.fillRect(finishLine.getX(), finishLine.getY(),
	                 finishLine.getWidth(), finishLine.getHeight());
	}
	
	public void drawLevel3(Graphics g2d) {
		g2d.drawImage(bg3, -cameraX - 100, 0, 3000, 2000, null);

		
	    if (platforms.isEmpty()) {
	        buildLevel3();
	        spawnPlayerOnFirstPlatform();
	    }

	    g2d.setColor(Color.WHITE);
	    for (Platform p : platforms)
	        g2d.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());

	    g2d.setColor(Color.RED);
	    for (Obstacle o : obstacles)
	        g2d.fillRect(o.getX(), o.getY(), o.getW(), o.getH());

	    g2d.setColor(Color.GREEN);
	    g2d.fillRect(finishLine.getX(), finishLine.getY(),
	                 finishLine.getWidth(), finishLine.getHeight());
	}



	
	public void loadLevel(int newLevel) {
	    level = newLevel;
	    platforms.clear();
	    obstacles.clear();
	}
	
	public void spawnPlayerOnFirstPlatform() {
	    Platform start = getStartPlatform();
	    if (start != null) {
	        int spawnX = start.getX() + start.getWidth() / 4;   // a bit inside the platform
	        int spawnY = start.getY() - player.geth();          // on top of it
	        player.reset(spawnX, spawnY);
	        cameraX = player.getx() - 200;                      // align camera to player
	    }
	}


	
	public void resetGame() {
	    // Reset screen to Start
	    screen = 'S';

	    // Reset level
	    level = 1;

	    // Reset player
	    player.reset(100, 100);

	    // Clear world
	    platforms.clear();
	    obstacles.clear();

	    // Reset camera
	    cameraX = 0;
	}
	
	public void startGame() {
	    screen = 'G';   // switch to game play
	    level = 1;

	    player.reset(100, 100);
	    platforms.clear();
	    obstacles.clear();
	}











	



	//DO NOT DELETE
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}




//DO NOT DELETE
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		key= e.getKeyCode();
		System.out.println(key);
		
		int key = e.getKeyCode();
		if (screen == 'S' && key == KeyEvent.VK_ENTER) {
	        startGame();
	    }
		if (screen == 'G' && key == KeyEvent.VK_P) {
	        level++;
	        loadLevel(level);
	    }
		
		if(key == KeyEvent.VK_0) {
			screen = 'W';
		}

	    // Reset game any time
	    if (key == KeyEvent.VK_R) {
	        resetGame();
	    }

	    // Movement keys only work in game play
	    if (screen == 'G') {
	        if (key == KeyEvent.VK_LEFT) player.movingLeft = true;
	        if (key == KeyEvent.VK_RIGHT) player.movingRight = true;
	        if (key == KeyEvent.VK_SPACE && player.getOnGround()) {
	            player.setDy(-15);
	            player.setOnGround(false);
	        }
	    }
		
		
		
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		 int key = e.getKeyCode();

		    if (key == KeyEvent.VK_LEFT) {
		        player.movingLeft = false;
		    }
		    if (key == KeyEvent.VK_RIGHT) {
		        player.movingRight = false;
		    }
		
		
		
		
	}
	
	
	

	
}
