package core;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

import entities.Player;
import entities.Laser;
import entities.Enemy;

public class SpaceCanvas extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean isRunning = false;
    
    private Player player;
    private ArrayList<Laser> lasers;
    private ArrayList<Enemy> enemies; 
    
    private int score = 0;
    private int lives = 3; 
    private boolean isGameOver = false; 
    
    private Image bgImage;
    private Image lifeImg; 

    public SpaceCanvas() {
        setBackground(new Color(10, 10, 25)); 
        setFocusable(true);
        addKeyListener(this);
        
        bgImage = new ImageIcon("src/assets/images/bg.png").getImage();
        lifeImg = new ImageIcon("src/assets/images/life.png").getImage();
        
        initGame(); 
    }

    private void initGame() {
        player = new Player(375, 480); 
        lasers = new ArrayList<>();
        enemies = new ArrayList<>();
        score = 0;
        lives = 3;
        isGameOver = false;
        
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
        if (isGameOver) return; 
        
        player.update();
        
        // Update Musuh & Cek nabrak Pesawat
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
        
        // Update Peluru & Cek kena Musuh
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
        
        // Respawn musuh jika udah selesai animasi meledak
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
        
        for (Enemy e : enemies) e.draw(g);
        for (Laser l : lasers) l.draw(g);
        player.draw(g); 
        
        // UI Skor
        g.setColor(new Color(0, 255, 255)); 
        g.drawString("SCORE: " + score, 20, 30);
        
        // UI Nyawa
        for (int i = 0; i < lives; i++) {
            if (lifeImg != null) {
                g.drawImage(lifeImg, 20 + (i * 35), 45, 25, 25, null);
            }
        }
        
        // Pop-up Game Over 
        if (isGameOver) {
            g.setColor(new Color(0, 0, 0, 200)); 
            g.fillRect(0, 0, 800, 600); 
            
            g.setColor(new Color(200, 0, 0)); 
            g.fillRect(250, 220, 300, 120); 
            
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 360, 260);
            g.drawString("FINAL SCORE: " + score, 340, 290);
            g.drawString("Press ENTER to Restart", 330, 320);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                initGame(); 
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
}