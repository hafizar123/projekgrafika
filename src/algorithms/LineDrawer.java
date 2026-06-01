package algorithms;

import java.awt.Color;
import java.awt.Graphics;

public class LineDrawer {
    
    // Algoritma Garis DDA 
    public static void drawDDA(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        
        int dx = x2 - x1;
        int dy = y2 - y1;
        
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        
        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;
        
        float x = x1;
        float y = y1;
        
        for (int i = 0; i <= steps; i++) {
            // Gambar pixel 
            g.fillRect(Math.round(x), Math.round(y), 2, 2); 
            x += xInc;
            y += yInc;
        }
    }
}