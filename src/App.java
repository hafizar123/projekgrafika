import javax.swing.JFrame;

import core.SpaceCanvas;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Retro Space Invader - Proyek Grafika");
        
        SpaceCanvas gameCanvas = new SpaceCanvas();
        frame.add(gameCanvas);
        
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setResizable(false); 
        frame.setVisible(true);
        
        gameCanvas.startGame();
    }
}
