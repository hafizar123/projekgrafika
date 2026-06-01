package entities;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Laser {
    private int x, y;
    private int speed = 10; 
    
    private Image laserImg = new ImageIcon("src/assets/images/laser.png").getImage();

    public Laser(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void update() {
        y -= speed; 
    }

    public void draw(Graphics g) {
        if (laserImg != null) {
            g.drawImage(laserImg, x - 5, y - 15, 10, 30, null);
        }
    }

    public boolean isOffScreen() { return y < 0; }
    public int getX() { return x; }
    public int getY() { return y; }
}