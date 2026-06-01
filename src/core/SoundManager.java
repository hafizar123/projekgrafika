package core;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    public static void playSound(String soundFilePath) {
        try {
            File soundPath = new File(soundFilePath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start(); 
            } else {
                System.out.println("File suara tidak ada!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
