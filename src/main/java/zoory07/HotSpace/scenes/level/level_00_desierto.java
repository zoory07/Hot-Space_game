package main.java.zoory07.HotSpace.scenes.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.animacion.Escena.Level_00_Decierto_Animacion;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.Sound;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.AlazarCactus;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.GestionDePatronesDeEventos;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.menus.menu_pausa;





public class level_00_desierto {
    private SpriteSheet spritesheet;
    private int x, y;
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
    private EscenaLimite EscenaLimite;
    
    // Animaciones de escena
    private Level_00_Decierto_Animacion Decierto_Animacion;  
    
    private Sound sonido;
    
    public level_00_desierto(SpriteSheet spritesheet, teclado teclado, tiempo tiempo) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.spritesheet = spritesheet;
        this.teclado = teclado;
        this.collisionManager = new CollisionManager();
        this.AlazarCactus = new AlazarCactus(spritesheet, 500, 30, 800, collisionManager);
        this.GestionDePatronesDeEventos = new GestionDePatronesDeEventos(AlazarCactus);
        
        long intereacionAnimacion = 50;
        Decierto_Animacion = new Level_00_Decierto_Animacion(intereacionAnimacion);
        
        this.tiempo = tiempo;

        this.reinicioLevel = new reinicio_level(spritesheet, teclado, tiempo);
        this.reinicioLevel.setNivel(this);
        
        this.pausa = new menu_pausa(0, 0); // Inicializar el menú de pausa
        this.enPausa = false;
        inicializarNivel();
    
        sonido = new Sound("game_over.wav");
    }

    private void inicializarNivel() throws IOException {
     // Inicializar Escena Limite
      int escenaX = 0;  
      int escenaY = 0;  
      int LimitadoX = 850;
      int LimitadoY = 900;
      
      EscenaLimite = new EscenaLimite(escenaX, escenaY, LimitadoX, LimitadoY);
     
     // Inicializar la lista de cactus
     cactusList = new ArrayList<>();

     // Configuración para la animacion del player (correr)
     int frameWidth = 30;
     int frameHeight = 30;
     int startX = 0;
     int startYCorrer = 0;  // Coordenada Y para la animacion de "correr"
     int numCorrerFrames = 5; // Número de frames para "correr"

     // Configuración para la animacion de (Game Over)
     int startXGameOver = frameWidth * 5;  
     int startYGameOver = 0;  
     int numGameOverFrames = 1;  

     // Cargar los frames de "correr"
     List<BufferedImage> correrFrames = spritesheet.getAnimationFrames(startX, startYCorrer, frameWidth, frameHeight, numCorrerFrames);
    
     // Cargar el frame de "Game Over"
     List<BufferedImage> gameOverFrames = spritesheet.getAnimationFrames(startXGameOver, startYGameOver, frameWidth, frameHeight, numGameOverFrames);
    
     if (!correrFrames.isEmpty()) {
        long frameDuracion = 100;
        this.player = new player(440, 400, correrFrames, teclado, frameDuracion, gameOverFrames, EscenaLimite);
     }
     
     // Añadir el hitbox del jugador al CollisionManager
     if (this.player != null && this.player.getHitbox() != null) {
        collisionManager.addHitbox(this.player.getHitbox());
     }

     EventoColision = new EventoColision(player, cactusList, tiempo);
     this.GameOver = false;
     System.out.println("Nivel inicializado"); // Mensaje de depuracion
   }


    public void reiniciarNivel() throws IOException {
        cactusList.clear();
        AlazarCactus.reiniciar();
        GestionDePatronesDeEventos.ReinicioDePatrones();
        inicializarNivel();
        tiempo.reiniciar();
        GameOver = false;
        System.out.println("Nivel reiniciado en level_00_desierto"); 
        Decierto_Animacion.reiniciarAnimacion();
    }

    public void update() {
        if (!enPausa && !GameOver) {
            //Actualizar los elementos del juego
            player.update();
            AlazarCactus.update(player);
            AlazarCactus.checkCollisionsWithPlayer(player);
            collisionManager.checkCollisions();
            GestionDePatronesDeEventos.update();
            Decierto_Animacion.update();
            for (piedra c : AlazarCactus.getCactusList()) {
                if (!cactusList.contains(c)) {
                    cactusList.add(c);
                }
            }           
            EventoColision.checkColision();
            if (EventoColision.isGameOver()) {
                GameOver = true;
                player.setGameOver();
                sonido.play();
                System.out.println("Game Over");
            }
        
        } else if (GameOver) {
            Decierto_Animacion.detenerAnimacion();
            reinicioLevel.reiniciar();
        } else if (enPausa) {
            pausa.update(teclado);
        }
        
        
        Decierto_Animacion.update();
        EscenaLimite.RestricionDeLimite(player);
    }

    public void render(Graphics g) {

           
        Decierto_Animacion.render(g);
        
        for (piedra c : cactusList) {
            if (c != null) {
                c.render(g);
            }
        }

        if (!enPausa && tiempo != null) {
            tiempo.render(g, 10, 20);
        }

        if (player != null) {
            player.render(g);
        }

        AlazarCactus.render(g);

        if (GameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 300, 300);
            g.drawString("Reiniciar(Enter)", 250, 350);
        } else if (enPausa) {
            pausa.render(g); // Renderizar el menú de pausa
        }
    }


    
    
    public void setEnPausa(boolean enPausa) {
        this.enPausa = enPausa;
    }
    public boolean isGameOver() {
        return GameOver;
    }

    public menu_pausa getPausa() {
        return pausa;
    }
    
    public player getPlayer() {
        return this.player;
    }

    


}