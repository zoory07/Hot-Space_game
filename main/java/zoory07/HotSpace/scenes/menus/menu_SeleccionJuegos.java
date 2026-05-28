package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.InputManager;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.Sound;



public class menu_SeleccionJuegos implements Escena {
    
    // Datos de niveles
    private final String[] niveles = {"Arcade", "Survival", "Contra Tiempo"};
    
    // Imágenes de los modos de juego
    private BufferedImage[] imagenesNivel;
    
    // Imágenes de flechas
    private BufferedImage flechaIzquierda;
    private BufferedImage flechaIzquierdaPresion;
    private BufferedImage flechaDerecha;
    private BufferedImage flechaDerechaPresion;
    
    private int nivelActual = 0;
    private int lastNivel = -1;
    
    // Animación zoom del panel
    private float zoomScale = 1.0f;
    private static final float ZOOM_MAX = 1.15f;
    private static final float ZOOM_SPEED = 0.03f;
    
    // Posiciones
    private static final int PANEL_X = 200;
    private static final int PANEL_Y = 150;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 280;
    
    private static final int FLECHA_WIDTH = 60;
    private static final int FLECHA_HEIGHT = 80;
    private static final int FLECHA_IZQ_X = 50;
    private static final int FLECHA_DER_X = 790;
    private static final int FLECHA_Y = 250;
    
    // Estado de las flechas (para animación de presión)
    private boolean flechaIzqPresionada = false;
    private boolean flechaDerPresionada = false;
    private long tiempoPresionFlecha = 0;
    private static final int DURACION_PRESION = 150;
    
    // Input
    private long lastMoveTime = System.currentTimeMillis();
    private static final int MOVE_DELAY = 200;
    
    private boolean confirm = false;
    private boolean volver = false;
    private boolean enterReady = true;
    
    private Sound sonidoMenu;
    private BufferedImage fondo;
    
    public menu_SeleccionJuegos() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        cargarImagenes();
        sonidoMenu = new Sound("menu.wav", false);
    }
    
    private void cargarImagenes() {
        // Cargar fondo
        try {
            fondo = ImageIO.read(getClass().getResourceAsStream("/resources/fondo_seleccion.png"));
        } catch (Exception e) {
            fondo = null;
        }
        
        // Cargar imágenes de niveles
        imagenesNivel = new BufferedImage[3];
        try {
            imagenesNivel[0] = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/ModoArcade.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar ModoArcade.png");
            imagenesNivel[0] = null;
        }
        try {
            imagenesNivel[1] = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/ModoSurvival.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar ModoSurvival.png");
            imagenesNivel[1] = null;
        }
        try {
            imagenesNivel[2] = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/ContraTiempo.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar ContraTiempo.png");
            imagenesNivel[2] = null;
        }
        
        // Cargar flechas
        try {
            flechaIzquierda = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/izquierda.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar izquierda.png");
            flechaIzquierda = null;
        }
        try {
            flechaIzquierdaPresion = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/izquierda_presion.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar izquierda_presion.png");
            flechaIzquierdaPresion = null;
        }
        try {
            flechaDerecha = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/derecha.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar derecha.png");
            flechaDerecha = null;
        }
        try {
            flechaDerechaPresion = ImageIO.read(getClass().getResourceAsStream("/resources/MenuSelecionDeJuegos/derecha_presion.png"));
        } catch (Exception e) {
            System.err.println("No se pudo cargar derecha_presion.png");
            flechaDerechaPresion = null;
        }
    }
    
    private int[] getMouseLogico(InputManager input) {
        int mouseX = input.raton.mouseX;
        int mouseY = input.raton.mouseY;

        int actualWidth = Main.mainInstance.getWidth();
        int actualHeight = Main.mainInstance.getHeight();

        double scaleX = (double) actualWidth / Main.BASE_WIDTH;
        double scaleY = (double) actualHeight / Main.BASE_HEIGHT;

        return new int[]{
            (int) (mouseX / scaleX),
            (int) (mouseY / scaleY)
        };
    }
    
    private boolean mouseEnFlecha(int logicalX, int logicalY, boolean izquierda) {
        int x = izquierda ? FLECHA_IZQ_X : FLECHA_DER_X;
        return logicalX >= x && logicalX <= x + FLECHA_WIDTH && logicalY >= FLECHA_Y && logicalY <= FLECHA_Y + FLECHA_HEIGHT;
    }
    
    private boolean mouseEnPanel(int logicalX, int logicalY) {
        return logicalX >= PANEL_X && logicalX <= PANEL_X + PANEL_WIDTH && logicalY >= PANEL_Y && logicalY <= PANEL_Y + PANEL_HEIGHT;
    }
    
    @Override
    public void update() {
        InputManager input = Main.input;
        long now = System.currentTimeMillis();
        
        int[] mouseLogico = getMouseLogico(input);
        int logicalX = mouseLogico[0];
        int logicalY = mouseLogico[1];
        
        // Actualizar estado de presión de flechas
        if (now - tiempoPresionFlecha > DURACION_PRESION) {
            flechaIzqPresionada = false;
            flechaDerPresionada = false;
        }
        
        // Navegación con teclado
        if (now - lastMoveTime >= MOVE_DELAY) {
            if (input.teclado.izquierda) {
                if (cambiarNivel(-1)) {
                    flechaIzqPresionada = true;
                    tiempoPresionFlecha = now;
                }
                input.teclado.izquierda = false;
                lastMoveTime = now;
            } else if (input.teclado.derecha) {
                if (cambiarNivel(1)) {
                    flechaDerPresionada = true;
                    tiempoPresionFlecha = now;
                }
                input.teclado.derecha = false;
                lastMoveTime = now;
            }
        }
        
        // Clic en flechas o panel
        if (input.raton.clicIzquierdo) {
            if (enterReady) {
                if (mouseEnFlecha(logicalX, logicalY, true)) {
                    if (cambiarNivel(-1)) {
                        flechaIzqPresionada = true;
                        tiempoPresionFlecha = now;
                    }
                } else if (mouseEnFlecha(logicalX, logicalY, false)) {
                    if (cambiarNivel(1)) {
                        flechaDerPresionada = true;
                        tiempoPresionFlecha = now;
                    }
                } else if (mouseEnPanel(logicalX, logicalY)) {
                    confirm = true;
                    sonidoMenu.play();
                }
                enterReady = false;
            }
        } else {
            enterReady = true;
        }
        
        // Enter para seleccionar nivel
        if (input.teclado.enter) {
            if (enterReady) {
                confirm = true;
                sonidoMenu.play();
                enterReady = false;
            }
        }
        
        // ESC para volver
        if (input.teclado.pausa) {
            volver = true;
            sonidoMenu.play();
            input.teclado.pausa = false;
        }
        
        // Animación zoom cuando cambia nivel
        if (nivelActual != lastNivel) {
            zoomScale = ZOOM_MAX;
            lastNivel = nivelActual;
        }
        
        if (zoomScale > 1.0f) {
            zoomScale -= ZOOM_SPEED;
            if (zoomScale < 1.0f) {
                zoomScale = 1.0f;
            }
        }
    }
    
    private boolean cambiarNivel(int direccion) {
        int nuevoNivel = nivelActual + direccion;
        if (nuevoNivel >= 0 && nuevoNivel < niveles.length) {
            nivelActual = nuevoNivel;
            sonidoMenu.play();
            return true;
        }
        return false;
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Fondo
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, 900, 600, null);
        } else {
            GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 50, 120), 0, 600, new Color(0, 20, 80));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 900, 600);
        }
        
        // Flechas de navegación
        dibujarFlecha(g2d, FLECHA_IZQ_X, FLECHA_Y, true, nivelActual > 0);
        dibujarFlecha(g2d, FLECHA_DER_X, FLECHA_Y, false, nivelActual < niveles.length - 1);
        
        // Panel central con zoom
        dibujarPanelNivel(g2d);
        
        // Puntos indicadores
        dibujarPuntos(g2d);
        
        // Instrucciones
        g.setFont(new Font("Jersey 10", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString("SELECCION DE JUEGOS", +370, +45);
        g.drawString("< > Navegar  |  ENTER Jugar  |  ESC Volver", 290, 560);
    }
    
    private void dibujarFlecha(Graphics2D g, int x, int y, boolean izquierda, boolean activa) {
        BufferedImage imagen;
        
        if (izquierda) {
            // Flecha izquierda
            if (flechaIzqPresionada && flechaIzquierdaPresion != null) {
                imagen = flechaIzquierdaPresion;
            } else {
                imagen = flechaIzquierda;
            }
        } else {
            // Flecha derecha
            if (flechaDerPresionada && flechaDerechaPresion != null) {
                imagen = flechaDerechaPresion;
            } else {
                imagen = flechaDerecha;
            }
        }
        
        if (imagen != null) {
            if (!activa) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }
            g.drawImage(imagen, x, y, FLECHA_WIDTH, FLECHA_HEIGHT, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // Fallback: dibujar flecha simple si no hay imagen
            // dibujarFlechaSimple(g, x, y, izquierda, activa);
        }
    }
    
    /* private void dibujarFlechaSimple(Graphics2D g, int x, int y, boolean izquierda, boolean activa) {
        int[] xPoints, yPoints;
        int size = 50;
        
        if (izquierda) {
            xPoints = new int[]{x + size, x, x + size};
            yPoints = new int[]{y, y + size / 2, y + size};
        } else {
            xPoints = new int[]{x, x + size, x};
            yPoints = new int[]{y, y + size / 2, y + size};
        }
        
        g.setColor(activa ? Color.WHITE : new Color(100, 100, 100));
        g.fillPolygon(xPoints, yPoints, 3);
    } */
    
    private void dibujarPanelNivel(Graphics2D g) {
        // Calcular tamaño con zoom
        int panelW = (int) (PANEL_WIDTH * zoomScale);
        int panelH = (int) (PANEL_HEIGHT * zoomScale);
        int panelX = PANEL_X - (panelW - PANEL_WIDTH) / 2;
        int panelY = PANEL_Y - (panelH - PANEL_HEIGHT) / 2;
        
        // Panel con borde redondeado
        g.setColor(new Color(0, 30, 80, 220));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 30, 30);
        
        // Borde del panel
        g.setColor(new Color(100, 150, 255));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(panelX, panelY, panelW, panelH, 30, 30);
        
        // Imagen del modo de juego
        int iconSize = 100;
        int iconX = panelX + panelW / 2 - iconSize / 2;
        int iconY = panelY + 30;
        
        BufferedImage imagenNivel = imagenesNivel[nivelActual];
        if (imagenNivel != null) {
            g.drawImage(imagenNivel, iconX, iconY, iconSize, iconSize, null);
        } else {
            // Fallback: dibujar círculo con color si no hay imagen
            Color[] coloresNivel = {
                new Color(0, 200, 255),
                new Color(0, 255, 100),
                new Color(255, 200, 0)
            };
            g.setColor(coloresNivel[nivelActual]);
            g.fillOval(iconX, iconY, iconSize, iconSize);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(3));
            g.drawOval(iconX, iconY, iconSize, iconSize);
        }
        
        // Nombre del nivel
        g.setFont(new Font("Jersey 10", Font.BOLD, 45));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = panelX + (panelW - fm.stringWidth(niveles[nivelActual])) / 2;
        g.drawString(niveles[nivelActual], textX, panelY + 175);
        
        // Texto "Clic para jugar"
        g.setFont(new Font("Jersey 10", Font.PLAIN, 25));
        g.setColor(new Color(200, 200, 200));
        String texto = "Clic para jugar";
        fm = g.getFontMetrics();
        textX = panelX + (panelW - fm.stringWidth(texto)) / 2;
        g.drawString(texto, textX, panelY + 230);
    }
    
    private void dibujarPuntos(Graphics2D g) {
        int puntoSize = 12;
        int espaciado = 25;
        int totalWidth = niveles.length * espaciado;
        int startX = 450 - totalWidth / 2;
        int y = 480;
        
        for (int i = 0; i < niveles.length; i++) {
             int x = startX + i * espaciado;
            
            if (i == nivelActual) {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, puntoSize, puntoSize);
            } else {
                g.setColor(new Color(100, 100, 100));
                g.fillOval(x, y, puntoSize, puntoSize);
            }
        }
    }
    
    public boolean consumeConfirm() {
        if (confirm) {
            confirm = false;
            return true;
        }
        return false;
    }
    
    public boolean consumeVolver() {
        if (volver) {
            volver = false;
            return true;
        }
        return false;
    }
    
    public void reset() {
        confirm = false;
        volver = false;
        enterReady = true;
        zoomScale = 1.0f;
        flechaIzqPresionada = false;
        flechaDerPresionada = false;
    }
    
    public int getNivelActual() {
        return nivelActual;
    }
    
    public String getNombreNivel() {
        return niveles[nivelActual];
    }


}