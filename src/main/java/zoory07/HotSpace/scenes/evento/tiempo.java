package main.java.zoory07.HotSpace.scenes.evento;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;





public class tiempo {
    private long inicioTiempo;
    private long pausaTiempo;
    private boolean enPausa;

    public void iniciar() {
        inicioTiempo = System.currentTimeMillis();
        enPausa = false;
    }

    public void reiniciar() {
        inicioTiempo = System.currentTimeMillis();
        enPausa = false;
    }

    public void pausar() {
        if (!enPausa) {
            pausaTiempo = System.currentTimeMillis();
            enPausa = true;
        }
    }

    public void reanudar() {
        if (enPausa) {
            inicioTiempo += System.currentTimeMillis() - pausaTiempo;
            enPausa = false;
        }
    }

    public long obtenerTiempo() {
        if (enPausa) {
            return pausaTiempo - inicioTiempo;
        } else {
            return System.currentTimeMillis() - inicioTiempo;
        }
    }

    public void render(Graphics g, int x, int y) {
        long tiempo = obtenerTiempo();
        int segundos = (int) (tiempo / 1000) % 60;
        int minutos = (int) ((tiempo / (1000 * 60)) % 60);
        int horas = (int) ((tiempo / (1000 * 60 * 60)) % 24);

        String tiempoString = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        Font myFont = new Font("Arial", Font.BOLD, 20);
        g.setFont(myFont);
        g.setColor(Color.WHITE);
        g.drawString(tiempoString, x, y);
    }
}
