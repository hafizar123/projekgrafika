package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import algorithms.CurveMaker; 

public class Enemy {
    private int x, y;
    private float t = 0; 
    private float speed = 0.005f; 
    private int[] p0, p1, p2, p3; 
    private double angle = 0; 
    private double scale = 1.0; 
    private boolean isDead = false;

    private Image enemyImg = new ImageIcon("src/assets/images/enemy.png").getImage();

    public Enemy(int[] p0, int[] p1, int[] p2, int[] p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public boolean checkHit(int targetX, int targetY) {
        if (isDead) return false; 
        double distance = Math.hypot(x - targetX, y - targetY);
        return distance < 20; 
    }

    public void setDead(boolean dead) { this.isDead = dead; }
    public boolean getDead() { return isDead; }
    public double getScale() { return scale; }
    
    public int getX() { return x; }
    public int getY() { return y; }

    public void update() {
        if (isDead) {
            scale -= 0.05;
            angle += 0.3;
            if (scale < 0) scale = 0;
            return; 
        }

        if (t <= 1.0f) {
            int[] point = CurveMaker.getBezierPoint(t, p0, p1, p2, p3);
            x = point[0];
            y = point[1];
            t += speed;
        } else {
            t = 0; 
        }
    }

    public void draw(Graphics g) {
        if (scale <= 0) return; 
        
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform(); 
        
        g2d.translate(x, y);
        g2d.rotate(angle);
        g2d.scale(scale, scale); 
        
        if (isDead) {
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.5f));
        }
        
        if (enemyImg != null) {
            g2d.drawImage(enemyImg, -20, -20, 40, 40, null);
        } else {
            g2d.setColor(new Color(255, 50, 50));
            g2d.fillRect(-15, -15, 30, 30);
        }
        
        g2d.setTransform(old); 
    }
}