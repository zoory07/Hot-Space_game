package main.java.zoory07.HotSpace.scenes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    private Clip clip;
    private boolean esMusica;  // true = música, false = efecto
    
    // Volúmenes globales separados (0.0 a 1.0)
    private static float volumenSonido = 0.8f;  // Efectos
    private static float volumenMusica = 0.8f;  // Música

    public Sound(String filaNombre) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this(filaNombre, false);  // Por defecto es efecto de sonido
    }
    
    public Sound(String filaNombre, boolean esMusica) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.esMusica = esMusica;
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/resources/sonido/" + filaNombre);
            if (audioSrc == null) {
                System.err.println("No se encontro el archivo de sonido: " + filaNombre);
                return;
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el sonido: " + filaNombre);
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            aplicarVolumen();
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            aplicarVolumen();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
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

    /**
     * Aplica el volumen correspondiente (música o sonido)
     */
    private void aplicarVolumen() {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            float vol = esMusica ? volumenMusica : volumenSonido;
            
            float dB;
            if (vol <= 0.0f) {
                dB = gainControl.getMinimum();
            } else {
                dB = (float) (Math.log10(vol) * 20.0);
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
            }
            gainControl.setValue(dB);
        }
    }
    
    /**
     * Actualiza el volumen en tiempo real (para música que está sonando)
     */
    public void actualizarVolumen() {
        aplicarVolumen();
    }

    /**
     * Establece el volumen de una instancia específica (en decibeles)
     */
    public void setVolume(float volumen) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volumen);
        }
    }

    // === MÉTODOS ESTÁTICOS PARA MENÚ DE OPCIONES ===

    /**
     * Establece el volumen de efectos de sonido (0.0 a 1.0)
     */
    public static void setVolumenSonido(float volumen) {
        volumenSonido = Math.max(0.0f, Math.min(1.0f, volumen));
    }

    /**
     * Obtiene el volumen de efectos de sonido
     */
    public static float getVolumenSonido() {
        return volumenSonido;
    }

    /**
     * Establece el volumen de música (0.0 a 1.0)
     */
    public static void setVolumenMusica(float volumen) {
        volumenMusica = Math.max(0.0f, Math.min(1.0f, volumen));
    }

    /**
     * Obtiene el volumen de música
     */
    public static float getVolumenMusica() {
        return volumenMusica;
    }
}