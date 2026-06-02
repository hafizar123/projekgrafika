package core;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

import entities.Player;
import entities.Laser;
import entities.Enemy;

public class SpaceCanvas extends JPanel implements Runnable, KeyListener, MouseListener {
    private Thread gameThread;
    private boolean isRunning = false;
    private boolean isGameStarted = false; 
    private boolean isPaused = false; 
    private boolean isGameOver = false; 
    
    // Variabel buat logika Countdown 3, 2, 1
    private boolean isCountingDown = false;
    private long countdownStart = 0;
    
    private Player player;
    private ArrayList<Laser> lasers;
    private ArrayList<Enemy> enemies; 
    
    private int score = 0;
    private int lives = 3; 
    
    private Image bgImage;
    private Image lifeImg; 
    private Font pixelFont; 

    public SpaceCanvas() {
        setBackground(new Color(10, 10, 25)); 
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this); 
        
        bgImage = new ImageIcon("src/assets/images/bg.png").getImage();
        lifeImg = new ImageIcon("src/assets/images/life.png").getImage();
        
        pixelFont = new Font("Monospaced", Font.BOLD, 36); 
    }

    // Fungsi baru buat jalanin hitung mundur tiap start/restart
    private void startWithCountdown() {
        initGame(); 
        isGameStarted = true;
        isGameOver = false;
        isPaused = false;
        isCountingDown = true;
        countdownStart = System.currentTimeMillis(); // Mulai nyatet waktu
    }

    private void initGame() {
        player = new Player(375, 480); 
        lasers = new ArrayList<>();
        enemies = new ArrayList<>();
        score = 0;
        lives = 3;
        
        spawnEnemy(400, 0);
        spawnEnemy(200, 300); 
        spawnEnemy(600, 150);
    }

    private void spawnEnemy(int startX, int offsetDelayY) {
        int[] p0 = {startX, -50 - offsetDelayY}; 
        int[] p1 = {startX - 250, 200 - offsetDelayY}; 
        int[] p2 = {startX + 250, 400 - offsetDelayY}; 
        int[] p3 = {startX, 650}; 
        enemies.add(new Enemy(p0, p1, p2, p3));
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            updateGameLogic();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void updateGameLogic() {
        if (!isGameStarted || isGameOver || isPaused) return; 
        
        // Kalo lagi hitung mundur, game freeze
        if (isCountingDown) {
            long elapsed = System.currentTimeMillis() - countdownStart;
            if (elapsed > 3000) { // timer 3000ms
                isCountingDown = false;
            }
            return;
        }
        
        player.update();
        
        for (Enemy e : enemies) {
            e.update();
            if (!e.getDead() && player.checkHit(e.getX(), e.getY())) {
                e.setDead(true); 
                lives--; 
                SoundManager.playSound("src/assets/sounds/explosion.wav"); 
                
                if (lives <= 0) {
                    isGameOver = true;
                }
            }
        }
        
        Iterator<Laser> iter = lasers.iterator();
        while (iter.hasNext()) {
            Laser l = iter.next();
            l.update();
            
            boolean hit = false;
            for (Enemy e : enemies) {
                if (!e.getDead() && e.checkHit(l.getX(), l.getY())) {
                    e.setDead(true);
                    score += 100;
                    hit = true;
                    SoundManager.playSound("src/assets/sounds/explosion.wav"); 
                    break; 
                }
            }
            
            if (hit || l.isOffScreen()) {
                iter.remove();
            }
        }
        
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.getDead() && e.getScale() <= 0) {
                int randomX = 100 + (int)(Math.random() * 600);
                int[] p0 = {randomX, -50}; 
                int[] p1 = {randomX - 250, 200}; 
                int[] p2 = {randomX + 250, 400}; 
                int[] p3 = {randomX, 650}; 
                enemies.set(i, new Enemy(p0, p1, p2, p3));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bgImage != null) g.drawImage(bgImage, 0, 0, 800, 600, this);
        
        // --- LAYAR START MENU ---
        if (!isGameStarted) {
            g.setFont(pixelFont);
            g.setColor(new Color(0, 255, 255)); 
            g.drawString("PRESS SPACE TO START", 150, 280); 
            return;
        }
        
        for (Enemy e : enemies) e.draw(g);
        for (Laser l : lasers) l.draw(g);
        player.draw(g); 
        
        g.setColor(new Color(0, 255, 255)); 
        g.drawString("SCORE: " + score, 20, 30);
        
        for (int i = 0; i < lives; i++) {
            if (lifeImg != null) {
                g.drawImage(lifeImg, 20 + (i * 35), 45, 25, 25, null);
            }
        }

        // Icon Pause 
        g.setColor(Color.WHITE);
        g.drawRect(730, 15, 40, 40); 
        if (isPaused) {
            int[] xPts = {745, 745, 760};
            int[] yPts = {22, 48, 35};
            g.fillPolygon(xPts, yPts, 3);
        } else {
            g.fillRect(742, 25, 6, 20);
            g.fillRect(754, 25, 6, 20);
        }
        
        // Teks hitung mundur
        if (isCountingDown) {
            long elapsed = System.currentTimeMillis() - countdownStart;
            String countText = "";
            if (elapsed < 1000) countText = "3";
            else if (elapsed < 2000) countText = "2";
            else if (elapsed < 3000) countText = "1";
            
            if (!countText.isEmpty()) {
                g.setColor(new Color(0, 0, 0, 150)); 
                g.fillRect(0, 0, 800, 600); 
                g.setFont(new Font("Monospaced", Font.BOLD, 80));
                g.setColor(Color.YELLOW);
                g.drawString(countText, 355, 320);
            }
        }
        
        // --- LAYAR PAUSE ---
        if (isPaused && !isGameOver && !isCountingDown) {
            g.setColor(new Color(0, 0, 0, 150)); 
            g.fillRect(0, 0, 800, 600); 
            
            g.setFont(pixelFont);
            g.setColor(Color.YELLOW); 
            g.drawString("PAUSED", 315, 270); 
            
            g.setFont(new Font("Monospaced", Font.PLAIN, 18));
            g.setColor(Color.WHITE);
            g.drawString("Press SPACE to Resume", 265, 310); 
        }
        
        // --- LAYAR GAME OVER ---
        if (isGameOver) {
            g.setColor(new Color(0, 0, 0, 200)); 
            g.fillRect(0, 0, 800, 600); 
            
            g.setColor(new Color(200, 0, 0)); 
            g.fillRect(250, 220, 300, 120); 
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 18)); 
            g.drawString("GAME OVER", 345, 260); 
            g.drawString("FINAL SCORE: " + score, 325, 290); 
            g.drawString("Press SPACE to Restart", 285, 320); 
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    
        if (!isGameStarted || isGameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                startWithCountdown();
            }
            return; 
        }

        if (isCountingDown) return; 
        
        //  ESC utk PAUSE
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            isPaused = true;
        }
        
        //  SPACE utk RESUME dari Pause
        if (isPaused) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                isPaused = false;
            }
            return; 
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.setDx(-5);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.setDx(5);
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            lasers.add(new Laser(player.getX(), player.getY() - 20));
            SoundManager.playSound("src/assets/sounds/laser.wav"); 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        
        // Kalo klik pas belom mulai atau game over, langsung start
        if (!isGameStarted || isGameOver) {
            startWithCountdown();
            return;
        }

        if (isPaused) {
            isPaused = false;
            return;
        }
        
        // Logika mencet tombol Pause kanan atas
        if (!isCountingDown) {
            if (mx >= 730 && mx <= 770 && my >= 15 && my <= 55) {
                isPaused = !isPaused;
            }
        }
    }
    
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}