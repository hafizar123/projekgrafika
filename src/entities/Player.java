package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import algorithms.LineDrawer; 

public class Player {
    private int x, y;
    private int dx;
    private Image playerImg; 

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        playerImg = new ImageIcon("src/assets/images/player.png").getImage(); 
    }

    public void setDx(int dx) { this.dx = dx; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void update() {
        x += dx;
        if (x < 20) x = 20;
        if (x > 750) x = 750;
    }

    public boolean checkHit(int enemyX, int enemyY) {
        double distance = Math.hypot(x - enemyX, y - enemyY);
        return distance < 30; // 30  body peswat
    }

    public void draw(Graphics g) {
        LineDrawer.drawDDA(g, x, y, x, 0, new Color(150, 0, 0)); 
        
        if (playerImg != null) {
            g.drawImage(playerImg, x - 25, y - 25, 50, 50, null);
        }
    }
}