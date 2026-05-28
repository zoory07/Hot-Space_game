package main.java.zoory07.HotSpace.game;








public class InputManager {
    public teclado teclado;
    public Raton raton;

    public InputManager() {
        teclado = new teclado();
        raton = new Raton();
    }

    public void update() {
       teclado.update();
       raton.update();
    }
    public void clear() {
      teclado.clear();
      raton.clear();
    }
}
