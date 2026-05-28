package main.java.zoory07.HotSpace.scenes;








public class MusicaManager {

    private static Sound musicaActual;

    private static Sound arcade;
    private static Sound survival;
    private static Sound contratiempo;
    private static Sound menu;

    public static void inicializar() {
    try {
        arcade       = new Sound("musica/Brisa de Pixel.wav",   true);
        survival     = new Sound("musica/Neblina de Pixel.wav", true);
        contratiempo = new Sound("musica/Pixel Drift.wav",      true);
        menu         = new Sound("musica/Pulso Suave.wav",      true);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error al cargar músicas");
    }
   }

    public static void reproducir(String nombre) {
        if (musicaActual != null) musicaActual.stop();

        switch (nombre) {
            case "arcade":       musicaActual = arcade;       break;
            case "survival":     musicaActual = survival;     break;
            case "contratiempo": musicaActual = contratiempo; break;
            case "menu":         musicaActual = menu;         break;
            default: return;
        }

        musicaActual.loop();
    }

    public static void detener() {
        if (musicaActual != null) musicaActual.stop();
    }

    public static void actualizarVolumen() {
        if (musicaActual != null) musicaActual.actualizarVolumen();
    }
}
