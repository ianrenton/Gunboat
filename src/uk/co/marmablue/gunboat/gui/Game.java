package uk.co.marmablue.gunboat.gui;

import uk.co.marmablue.gunboat.gui.GLDisplay;

/** Game window for Gunboat.
 * @author Ian Renton
 */
public class Game {

    /** Main method.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        GLDisplay gameGLDisplay = null;
        boolean windowed = false;
        for (int i=0; i<args.length; i++) {
            if (args[i].equals("-windowed")) {
                windowed = true;
            }
        }

        if (windowed) {
            gameGLDisplay = GLDisplay.createGLDisplay("Gunboat");
        } else {
            gameGLDisplay = GLDisplay.createFullScreenGLDisplay("Gunboat");
        }

        GameRenderer renderer = new GameRenderer();
        KeyboardHandler keyboardHandler = new KeyboardHandler(renderer, gameGLDisplay);
        MouseHandler mouseHandler = new MouseHandler(renderer, gameGLDisplay);
        gameGLDisplay.addGLEventListener(renderer);
        gameGLDisplay.addKeyListener(keyboardHandler);
        gameGLDisplay.addMouseMotionListener(mouseHandler);
        gameGLDisplay.addMouseListener(mouseHandler);
        gameGLDisplay.start();
    }
}
