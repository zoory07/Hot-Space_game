package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;







public class VelocidadDeObjecto {
    private int velocidadBase;
    private int incrementoDificultad;
    private long tiempoInicio;
    private int incrementoTiempo; // en segundos ZzZz
    private boolean enPausa = false;
    private long tiempoAlPausar;
    
    public VelocidadDeObjecto(int velocidadBase, int incrementoDificultad, int incrementoTiempo){
        this.velocidadBase = velocidadBase;
        this.incrementoDificultad = incrementoDificultad;
        this.incrementoTiempo = incrementoTiempo;
        this.tiempoInicio = System.currentTimeMillis();
    }

    // Calcula la velocidad actual según el tiempo transcurrido
    public int calcularVelocidadActual(){
        if (enPausa) return velocidadBase; 
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 1000);
        int velocidadActual = velocidadBase + (segundosTranscurridos  / incrementoTiempo) * incrementoDificultad;
        return velocidadActual;
   }

    // Resetea el tiempo al momento actual
    public void resetTiempo(){
        this.tiempoInicio = System.currentTimeMillis();
    }

    // Opcional: resetea todo junto (para reinicios de nivel)
    public void reset(int nuevaVelocidadBase) {
        this.velocidadBase = nuevaVelocidadBase;
        resetTiempo();
    }
    public void pausar() {
       if (!enPausa) {
        enPausa = true;
        tiempoAlPausar = System.currentTimeMillis();
    }
  }

    public void reanudar() {
     if (enPausa) {
        enPausa = false;
        // Ajusta el tiempo de inicio para que no avance durante la pausa
        tiempoInicio += (System.currentTimeMillis() - tiempoAlPausar);
     }
   } 
    
    
    public void setVelocidadBase(int nuevaVelocidadBase) {
        this.velocidadBase = nuevaVelocidadBase;
    }

    public void setIncrementoDificultad(int nuevoIncrementoDificultad) {
        this.incrementoDificultad = nuevoIncrementoDificultad;
    }

    public void setIncrementoTiempo(int nuevoIncrementoTiempo) {
        this.incrementoTiempo = nuevoIncrementoTiempo;
    }
}
