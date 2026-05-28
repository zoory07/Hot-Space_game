package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.util.Random;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.AlazarCactus;






public class GestionDePatronesDeEventos {
    
    private final Random random;
    private final AlazarCactus alazarCactus;
    
    // Dificultad
    private int dificultad;
    private static final int DIFICULTAD_MINIMA = 1;
    private static final int DIFICULTAD_MAXIMA = 3;
    private static final int SEGUNDOS_PARA_SUBIR_DIFICULTAD = 15;
    
    // Tiempos
    private long tiempoInicio;
    private long tiempoUltimaGeneracion;
    private long tiempoPausado;
    
    // Intervalos de generación
    private final int intervaloGeneracionInicial;
    private int intervaloGeneracion;
    
    // Límites de spawn
    private final int anchoEscena;
    private final int altoSpawn;
    
    private boolean enPausa = false;
    
    public GestionDePatronesDeEventos(AlazarCactus alazarCactus) {
        this(alazarCactus, 950, 4, 1000);
    }
    
    public GestionDePatronesDeEventos(AlazarCactus alazarCactus, int anchoEscena,int altoSpawn, int intervaloMs) {
        this.alazarCactus = alazarCactus;
        this.random = new Random();
        this.anchoEscena = anchoEscena;
        this.altoSpawn = altoSpawn;
        this.intervaloGeneracionInicial = intervaloMs;
        
        reiniciarEstado();
    }
    
    private void reiniciarEstado() {
        this.dificultad = DIFICULTAD_MINIMA;
        this.tiempoInicio = System.currentTimeMillis();
        this.tiempoUltimaGeneracion = tiempoInicio;
        this.intervaloGeneracion = intervaloGeneracionInicial;
        this.tiempoPausado = 0;
    }
    
    public void update() {
        if (enPausa) return;
        
        actualizarDificultadSegunTiempo();
        generarPatrones();
    }
    
    private void generarPatrones() {
        long tiempoActual = System.currentTimeMillis();
        
        if (tiempoActual - tiempoUltimaGeneracion < intervaloGeneracion) return;
        
        int cantidadCactus = calcularCantidadCactus();
        
        for (int i = 0; i < cantidadCactus; i++) {
            int posX = random.nextInt(anchoEscena);
            int posY = -random.nextInt(altoSpawn); // Negativo para que aparezcan arriba
            alazarCactus.generarCactus(posX, posY);
        }
        
        tiempoUltimaGeneracion = tiempoActual;
    }
    
    private int calcularCantidadCactus() {
        // Puede generar entre 1 y dificultad cactus
        return 1 + random.nextInt(dificultad);
    }
    
    private void actualizarDificultadSegunTiempo() {
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 1000);
        
        if (segundosTranscurridos >= SEGUNDOS_PARA_SUBIR_DIFICULTAD) {
            subirDificultad();
            tiempoInicio = tiempoActual;
        }
    }
    
    private void subirDificultad() {
        dificultad++;
        
        if (dificultad > DIFICULTAD_MAXIMA) {
            dificultad = DIFICULTAD_MINIMA; // Cicla la dificultad
        }
        
        // Reducir intervalo para más spawns
        intervaloGeneracion = Math.max(300, intervaloGeneracion - 100);
        
        alazarCactus.incrementarVelocidad();
    }
    
    public void setEnPausa(boolean enPausa) {
        if (this.enPausa == enPausa) return;
        
        long tiempoActual = System.currentTimeMillis();
        
        if (enPausa) {
            // Guardar tiempo al pausar
            tiempoPausado = tiempoActual;
        } else {
            // Compensar tiempo pausado
            long tiempoPausadoTotal = tiempoActual - tiempoPausado;
            tiempoInicio += tiempoPausadoTotal;
            tiempoUltimaGeneracion += tiempoPausadoTotal;
        }
        
        this.enPausa = enPausa;
    }
    
    public boolean isEnPausa() {
        return enPausa;
    }
    
    public void ReinicioDePatrones() {
        reiniciarEstado();
        alazarCactus.reiniciar();
    }
    
    // Reinicio sin tocar AlazarCactus (por si lo maneja otro lado)
    public void reiniciarSoloPatrones() {
        reiniciarEstado();
    }
    
    // Getters útiles
    public int getDificultad() { 
        return dificultad; 
    }
    
    public int getIntervaloGeneracion() { 
        return intervaloGeneracion; 
    }
    
    public long getTiempoTranscurrido() {
        if (enPausa) {
            return tiempoPausado - tiempoInicio;
        }
        return System.currentTimeMillis() - tiempoInicio;
    }
    
    public int getSegundosParaSiguienteDificultad() {
        int segundosTranscurridos = (int) (getTiempoTranscurrido() / 1000);
        return SEGUNDOS_PARA_SUBIR_DIFICULTAD - segundosTranscurridos;
    }
}
