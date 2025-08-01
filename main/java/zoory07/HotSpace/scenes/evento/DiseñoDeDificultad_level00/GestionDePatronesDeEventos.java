package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;

import java.util.Random;






public class GestionDePatronesDeEventos {
    private final Random random;
    private final AlazarCactus alazarCactus;
    private int dificultad;
    private long tiempoInicio;
    private long tiempoUltimaGeneracion;
    private final int intervaloGeneracionInicial;
    private int intervaloGeneracion;
    private boolean enPausa = false;

    public GestionDePatronesDeEventos(AlazarCactus alazarCactus) {
        this.alazarCactus = alazarCactus;
        this.random = new Random();
        this.dificultad = 1;
        this.tiempoInicio = System.currentTimeMillis();
        this.tiempoUltimaGeneracion = tiempoInicio;
        this.intervaloGeneracionInicial = 1000; // 1 segundo
        this.intervaloGeneracion = intervaloGeneracionInicial;
    }

    public void setEnPausa(boolean enPausa) {
        this.enPausa = enPausa;
    }

    private void generarPatrones(int dificultad) {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimaGeneracion < intervaloGeneracion) return;

        int numeroMaxDeCactus = dificultad; // 1, 2 o 3
        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(950);
            int posY = random.nextInt(4);
            // Llamada al método de pool en AlazarCactus
            alazarCactus.generarCactus(posX, posY);
        }
        tiempoUltimaGeneracion = tiempoActual;
    }

    public void actualizarDificultadSegunTiempo() {
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 1000);
        if (segundosTranscurridos >= 30) {
            dificultad++;
            if (dificultad > 3) dificultad = 1;
            alazarCactus.incrementarVelocidad();
            tiempoInicio = tiempoActual;
        }
    }

    public void update() {
        if (enPausa) return;
        actualizarDificultadSegunTiempo();
        generarPatrones(dificultad);
    }

    public void ReinicioDePatrones() {
        this.dificultad = 1;
        this.tiempoInicio = System.currentTimeMillis();
        this.tiempoUltimaGeneracion = tiempoInicio;
        this.intervaloGeneracion = intervaloGeneracionInicial;
        alazarCactus.reiniciar();
    }
}


