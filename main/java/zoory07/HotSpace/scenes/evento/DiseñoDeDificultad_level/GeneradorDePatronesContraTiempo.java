package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.util.Random;






public class GeneradorDePatronesContraTiempo {

    private final Random random;
    private final AlazarCactus alazarCactus;
    private final GeneradorDeZanahorias generadorZanahorias;

    // Dificultad
    private int dificultad;
    private static final int DIFICULTAD_MINIMA = 1;
    private static final int DIFICULTAD_MAXIMA = 3;
    private static final int SEGUNDOS_PARA_SUBIR_DIFICULTAD = 10; // más agresivo que arcade

    // Tiempos
    private long tiempoInicio;
    private long tiempoUltimaGeneracion;
    private long tiempoUltimaZanahoria;
    private long tiempoPausado;

    // Intervalos
    private final int intervaloGeneracionInicial;
    private int intervaloGeneracion;
    private int intervaloZanahoria = 5000; // cada 5 segundos una zanahoria

    // Límites de spawn
    private final int anchoEscena;
    private final int altoSpawn;

    private boolean enPausa = false;

    public GeneradorDePatronesContraTiempo(AlazarCactus alazarCactus,GeneradorDeZanahorias generadorZanahorias) {
        this(alazarCactus, generadorZanahorias, 950, 4, 1000);
    }

    public GeneradorDePatronesContraTiempo(AlazarCactus alazarCactus,GeneradorDeZanahorias generadorZanahorias,int anchoEscena, int altoSpawn, int intervaloMs) {
        this.alazarCactus = alazarCactus;
        this.generadorZanahorias  = generadorZanahorias;
        this.random  = new Random();
        this.anchoEscena = anchoEscena;
        this.altoSpawn   = altoSpawn;
        this.intervaloGeneracionInicial = intervaloMs;
        reiniciarEstado();
    }

    private void reiniciarEstado() {
        this.dificultad = DIFICULTAD_MINIMA;
        this.tiempoInicio  = System.currentTimeMillis();
        this.tiempoUltimaGeneracion  = tiempoInicio;
        this.tiempoUltimaZanahoria  = tiempoInicio;
        this.intervaloGeneracion  = intervaloGeneracionInicial;
        this.tiempoPausado   = 0;
    }

    public void update() {
        if (enPausa) return;

        actualizarDificultadSegunTiempo();
        generarPatrones();
        generarZanahorias();
    }

    private void generarPatrones() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimaGeneracion < intervaloGeneracion) return;

        int cantidad = 1 + random.nextInt(dificultad);
        for (int i = 0; i < cantidad; i++) {
            int posX = random.nextInt(anchoEscena);
            int posY = -random.nextInt(altoSpawn);
            alazarCactus.generarCactus(posX, posY);
        }

        tiempoUltimaGeneracion = tiempoActual;
    }

    private void generarZanahorias() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimaZanahoria < intervaloZanahoria) return;

        int posX = random.nextInt(anchoEscena);
        int posY = 100 + random.nextInt(500); // zona visible de pantalla
        generadorZanahorias.generarZanahoria(posX, posY);

        tiempoUltimaZanahoria = tiempoActual;
    }

    private void actualizarDificultadSegunTiempo() {
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int)((tiempoActual - tiempoInicio) / 1000);

        if (segundosTranscurridos >= SEGUNDOS_PARA_SUBIR_DIFICULTAD) {
            subirDificultad();
            tiempoInicio = tiempoActual;
        }
    }

    private void subirDificultad() {
        dificultad++;
        if (dificultad > DIFICULTAD_MAXIMA) {
            dificultad = DIFICULTAD_MINIMA;
        }

        // Más cactus, menos tiempo entre spawns, menos zanahorias
        intervaloGeneracion = Math.max(300, intervaloGeneracion - 100);
        intervaloZanahoria  = Math.max(3000, intervaloZanahoria - 500);

        alazarCactus.incrementarVelocidad();
    }

    public void setEnPausa(boolean enPausa) {
        if (this.enPausa == enPausa) return;

        long tiempoActual = System.currentTimeMillis();

        if (enPausa) {
            tiempoPausado = tiempoActual;
        } else {
            long tiempoPausadoTotal  = tiempoActual - tiempoPausado;
            tiempoInicio            += tiempoPausadoTotal;
            tiempoUltimaGeneracion  += tiempoPausadoTotal;
            tiempoUltimaZanahoria   += tiempoPausadoTotal;
        }

        this.enPausa = enPausa;
    }

    public void reiniciar() {
        reiniciarEstado();
        alazarCactus.reiniciar();
        generadorZanahorias.reiniciar();
    }

    public boolean isEnPausa(){ 
        return enPausa;
    }
    public int getDificultad() { 
        return dificultad;
    }
    public int getIntervaloGeneracion() { 
        return intervaloGeneracion; 
    }

    public long getTiempoTranscurrido() {
        if (enPausa) return tiempoPausado - tiempoInicio;
        return System.currentTimeMillis() - tiempoInicio;
    }

    public int getSegundosParaSiguienteDificultad() {
        int segundosTranscurridos = (int)(getTiempoTranscurrido() / 1000);
        return SEGUNDOS_PARA_SUBIR_DIFICULTAD - segundosTranscurridos;
    }
}
