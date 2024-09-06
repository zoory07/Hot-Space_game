package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;



import java.util.Random;




public class GestionDePatronesDeEventos {
    private Random random;
    private AlazarCactus alazarCactus;
    private int dificultad;
    private long tiempoInicio;
    private long tiempoUltimaGeneracion;
    private int intervaloGeneracion;

    
    public GestionDePatronesDeEventos(AlazarCactus alazarCactus) {
        this.alazarCactus = alazarCactus;
        this.random = new Random();
        this.dificultad = 1; // Nivel de dificultad inicial
        this.tiempoInicio = System.currentTimeMillis(); // Guardar el tiempo de inicio
        this.tiempoUltimaGeneracion = tiempoInicio; // Inicializar el tiempo de última generación
        this.intervaloGeneracion = 1000; // Intervalo de generación en milisegundos (2 segundos)
    }

    public void incrementarDificultad() {
        this.dificultad++;
        alazarCactus.resetGemeracion(); // Reiniciar la generación de cactus para el nuevo nivel de dificultad
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
            int posX = random.nextInt(800); // Generar posición X aleatoria dentro del ancho de la pantalla
            int posY = random.nextInt(600); // Generar posición Y aleatoria dentro de la altura de la pantalla
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void DibujoDePatrones01() {
        int numeroMaxDeCactus = 2;

        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(800); // Generar posición X aleatoria dentro del ancho de la pantalla
            int posY = random.nextInt(600); // Generar posición Y aleatoria dentro de la altura de la pantalla
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void DibujoDePatrones02() {
        int numeroMaxDeCactus = 3;

        for (int i = 0; i < numeroMaxDeCactus; i++) {
            int posX = random.nextInt(800); // Generar posición X aleatoria dentro del ancho de la pantalla
            int posY = random.nextInt(600); // Generar posición Y aleatoria dentro de la altura de la pantalla
            alazarCactus.CactusImagen(posX, posY);
        }
    }

    public void actualizarDificultadSegunTiempo() {
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 2000);

        if (segundosTranscurridos >= 30 && dificultad < 3) { // Incrementar dificultad cada 30 segundos
            incrementarDificultad();
            tiempoInicio = tiempoActual; // Reiniciar el tiempo de inicio
        }
    }

    public void update() {
        
        actualizarDificultadSegunTiempo();
        DibujoDePatrones(dificultad);
    }





}
