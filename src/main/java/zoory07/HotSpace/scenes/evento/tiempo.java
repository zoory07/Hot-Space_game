package main.java.zoory07.HotSpace.scenes.evento;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;





public class tiempo {
    private long inicioTiempo;
    private long tiempoPausadoTotal;
    private long tiempoInicioPausa;
    private boolean enPausa;

    public void iniciar() {
        inicioTiempo = System.currentTimeMillis();
        tiempoPausadoTotal = 0;
        enPausa = false;
    }

    public void reiniciar() {
        inicioTiempo = System.currentTimeMillis();
        tiempoPausadoTotal = 0;
        enPausa = false;
    }

    public void pausar() {
        if (!enPausa) {
            tiempoInicioPausa = System.currentTimeMillis();
            enPausa = true;
            System.out.println("Temporizador pausado en: " + obtenerTiempo() + " ms");
        }
    }

    public void reanudar() {
        if (enPausa) {
            tiempoPausadoTotal += System.currentTimeMillis() - tiempoInicioPausa;
            enPausa = false;
            System.out.println("Temporizador reanudado. Tiempo total pausado: " + tiempoPausadoTotal + " ms");
        }
    }

    public long obtenerTiempo() {
        if (enPausa) {
            return tiempoInicioPausa - inicioTiempo - tiempoPausadoTotal;
        } else {
            return System.currentTimeMillis() - inicioTiempo - tiempoPausadoTotal;
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
