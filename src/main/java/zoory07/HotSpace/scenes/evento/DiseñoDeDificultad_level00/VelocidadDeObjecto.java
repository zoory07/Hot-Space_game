package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;








public class VelocidadDeObjecto {
    private int velocidadBase;
    private int incrementoDificultad;
    private long tiempoInicio;
    private int incrementoTiempo;
    
    
    public VelocidadDeObjecto(int velocidadBase, int incrementoDificultad, int incrementoTiempo){
        this.velocidadBase = velocidadBase;
        this.incrementoDificultad = incrementoDificultad;
        this.incrementoTiempo = incrementoTiempo;
        this.tiempoInicio = System.currentTimeMillis();
    }
    
    
    public int calcularVelocidadActual(){
        long tiempoActual = System.currentTimeMillis();
        int segundosTranscurridos = (int) ((tiempoActual - tiempoInicio) / 1000);
        
        int velocidadActual = velocidadBase + (segundosTranscurridos  / incrementoTiempo) * incrementoDificultad;
        return velocidadActual;
    
    }
    
    
    public void resetTiempo(int nuevaVelocidadBase){
       this.tiempoInicio = nuevaVelocidadBase;
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