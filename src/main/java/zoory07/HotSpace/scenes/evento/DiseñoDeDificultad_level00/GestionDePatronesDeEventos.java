package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;

import java.util.Random;




public class GestionDePatronesDeEventos {
    private Random random;
    private AlazarCactus alazarCactus;
    private int dificultad;
    private long tiempoInicio;
    private long tiempoUltimaGeneracion;
    private int intervaloGeneracion;
    private int intervaloGeneracionInicial;

    public GestionDePatronesDeEventos(AlazarCactus alazarCactus) {
        this.alazarCactus = alazarCactus;
        this.random = new Random();
        this.dificultad = 1; // Nivel de dificultad inicial
        this.tiempoInicio = System.currentTimeMillis(); // Guardar el tiempo de inicio
        this.tiempoUltimaGeneracion = tiempoInicio; // Inicializar el tiempo de última generación
        this.intervaloGeneracionInicial = 1000; // Intervalo de generación inicial en milisegundos (1 segundo)
        this.intervaloGeneracion = intervaloGeneracionInicial;
    }

    public void incrementarDificultad() {
        this.dificultad++;
        if (dificultad > 3) {
            dificultad = 1; 
        }
        alazarCactus.incrementarVelocidad(); // Incrementar la velocidad del cactus
    }

    public void DibujoDePatrones(int dificultad) {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimaGeneracion >= intervaloGeneracion) {
            if (dificultad == 1) {
                DibujoDePatrones00();
            } else if (dificultad == 2) {
                DibujoDePatrones01();
            } else if (dificultad == 3) {
                DibujoDePatrones02();
            }
            tiempoUltimaGeneracion = tiempoActual;
        }
    }

    public void DibujoDePatrones00() {
        int numeroMaxDeCactus = 1;

        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(950);
            int posY = random.nextInt(4);
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void DibujoDePatrones01() {
        int numeroMaxDeCactus = 2;

        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(950);
            int posY = random.nextInt(4);
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void DibujoDePatrones02() {
        int numeroMaxDeCactus = 3;

        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(950);
            int posY = random.nextInt(4);
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void actualizarDificultadSegunTiempo() {
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 1000); // Dividir por 1000 para obtener segundos

        if (segundosTranscurridos >= 30) { // Incrementar dificultad cada 30 segundos
            incrementarDificultad();
            tiempoInicio = tiempoActual; // Reiniciar el tiempo de inicio
        }
    }

    public void update() {
        actualizarDificultadSegunTiempo();
        DibujoDePatrones(dificultad);
    }


    
  public void ReinicioDePatrones() {
     System.out.println("Reiniciando patrones y velocidad...");
     this.dificultad = 1;
     this.tiempoInicio = System.currentTimeMillis();
     this.tiempoUltimaGeneracion = tiempoInicio;
     this.intervaloGeneracion = intervaloGeneracionInicial;
      alazarCactus.reiniciar(); 
     System.out.println("Patrones y velocidad reiniciados.");
  }





}

