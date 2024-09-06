package main.java.zoory07.HotSpace.scenes.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.img_desierto;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.AlazarCactus;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.GestionDePatronesDeEventos;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.menus.menu_pausa;





public class level_00_desierto {
    private SpriteSheet spritesheet;
    private int x, y;
    public img_desierto img_desierto;
    private player player;
    public teclado teclado;
    private List<piedra> cactusList;
    private CollisionManager collisionManager;
    private AlazarCactus AlazarCactus;
    private GestionDePatronesDeEventos GestionDePatronesDeEventos;
    private tiempo tiempo;
    private EventoColision EventoColision;
    private boolean GameOver;
    private reinicio_level reinicioLevel;
    private menu_pausa pausa;
    private boolean enPausa;

    public level_00_desierto(SpriteSheet spritesheet, teclado teclado, tiempo tiempo) throws IOException {
        this.spritesheet = spritesheet;
        this.teclado = teclado;
        this.collisionManager = new CollisionManager();
        this.AlazarCactus = new AlazarCactus(spritesheet, 500, 30, 800, collisionManager);
        this.GestionDePatronesDeEventos = new GestionDePatronesDeEventos(AlazarCactus);
        img_desierto = new img_desierto(0, 0);
        this.tiempo = tiempo;

        this.reinicioLevel = new reinicio_level(spritesheet, teclado, tiempo);
        this.reinicioLevel.setNivel(this);

        this.pausa = new menu_pausa(0, 0); // Inicializar el menú de pausa
        this.enPausa = false;

        inicializarNivel();
    }

    private void inicializarNivel() throws IOException {
        // Inicializar la lista de cactus
        cactusList = new ArrayList<>();

        // Animación del player
        int frameWidth = 30;
        int frameHeight = 30;
        int startX = 0;
        int startY = 0;
        int numFrames = 4; // Número de frames en la fila

        List<BufferedImage> correrFrames = spritesheet.getAnimationFrames(startX, startY, frameWidth, frameHeight, numFrames);

        if (!correrFrames.isEmpty()) {
            long frameDuracion = 100;
            this.player = new player(440, 400, correrFrames, teclado, frameDuracion);
        }

        if (this.player != null && this.player.getHitbox() != null) {
            collisionManager.addHitbox(this.player.getHitbox());
        }

        EventoColision = new EventoColision(player, cactusList, tiempo);
        this.GameOver = false;
        System.out.println("Nivel inicializado"); // Mensaje de depuración
    }

    public void reiniciarNivel() throws IOException {
        inicializarNivel();
        tiempo.reiniciar();
        GameOver = false;
        System.out.println("Nivel reiniciado en level_00_desierto"); 
    }

    public void update() {
        teclado.update(); // Actualizar el estado del teclado siempre

        if (teclado.pausa) { 
            enPausa = !enPausa;
            teclado.pausa = false; // Resetear estado de la tecla de pausa
        }

        if (!enPausa && !GameOver) {
            AlazarCactus.update(player);
            AlazarCactus.checkCollisionsWithPlayer(player);
            player.update();
            collisionManager.checkCollisions();
            GestionDePatronesDeEventos.update();

            
            for (piedra c : AlazarCactus.getCactusList()) {
                if (!cactusList.contains(c)) {
                    cactusList.add(c);
                }
            }

            EventoColision.checkColision();

            if (EventoColision.isGameOver()) {
                GameOver = true;
                System.out.println("Game Over"); // Mensaje de depuración
            }
        } else if (GameOver) {
            // Si GameOver es verdadero, verificar si la tecla ENTER está presionada
            reinicioLevel.reiniciar();
        } else if (enPausa) {
            pausa.update(teclado); // Actualizar el menú de pausa
        }
    }

    public void render(Graphics g) {
        if (img_desierto != null) {
            img_desierto.render(g);
        }

        for (piedra c : cactusList) {
            if (c != null) {
                c.render(g);
            }
        }

        if (tiempo != null) {
            tiempo.render(g, 10, 20);
        }

        if (player != null) {
            player.render(g);
        }

        AlazarCactus.render(g);

        if (GameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 300, 300); // Ajustar la posición
            g.drawString("Reiniciar(Enter)", 250, 350);
        } else if (enPausa) {
            pausa.render(g); // Renderizar el menú de pausa
        }
    }

    public boolean isGameOver() {
        return GameOver;
    }

    public menu_pausa getPausa() {
        return pausa;
    }
}
