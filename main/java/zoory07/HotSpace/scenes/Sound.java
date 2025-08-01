package main.java.zoory07.HotSpace.scenes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes.Name;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



public class Sound {
      
   private Clip clip; 

   public Sound(String filaNombre) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
     try{
       InputStream audioSrc = getClass().getResourceAsStream("/resources/sonido/" + filaNombre);
        if (audioSrc == null) {
        System.err.println("No se encontro el archivo de sonido: " + filaNombre);
        return; 
     }
        InputStream bufferedIn = new BufferedInputStream(audioSrc);

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        clip = AudioSystem.getClip();
        clip.open(audioStream);  
    }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
        e.printStackTrace();
        System.err.println("Error al cargar el sonido: " + filaNombre);           
    }
   }
   
   public void play(){
     if (clip != null) {
     if (clip.isRunning()) {
         clip.stop(); // Detener si ya se está reproduciendo
        }
        clip.setFramePosition(0); // Reiniciar al principio
        clip.start();
   }
   
  }
  
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Repetir indefinidamente
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }

    public void setVolume(float volumen) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volumen); // El volumen va de -80.0f a 6.0f
        }
    } 

}