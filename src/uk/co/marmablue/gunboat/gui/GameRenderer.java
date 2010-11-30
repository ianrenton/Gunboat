package uk.co.marmablue.gunboat.gui;

import java.awt.event.MouseEvent;
import uk.co.marmablue.gunboat.miscobjects.Sea;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.ships.Type23Frigate;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.Overlay;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.marmablue.gunboat.bullets.Bullet;
import uk.co.marmablue.gunboat.crates.Crate;
import uk.co.marmablue.gunboat.miscobjects.HUD;
import uk.co.marmablue.gunboat.miscobjects.Terrain;
import uk.co.marmablue.gunboat.model.ModelLoader;
import uk.co.marmablue.gunboat.ships.MarineLandingCraft;
import uk.co.marmablue.gunboat.ships.HeavyFrigate;
import uk.co.marmablue.gunboat.ships.Battleship;
import uk.co.marmablue.gunboat.ships.Ship;
import uk.co.marmablue.gunboat.ships.PlayerShip;
import uk.co.marmablue.gunboat.texture.TextureLoader;

public class GameRenderer implements GLEventListener {

    private GL gl;
    private GLU glu = new GLU();
    private Animator animator = null;
    private TextureLoader textureLoader;
    private ModelLoader modelLoader;
    private Overlay hudOverlay;
    private HUD hud;
    private MouseEvent mouseEvent;
    private boolean firing = false;
    private int accelerating = 0;
    private int turning = 0;
    private int cameraView = 1;
    private TextRenderer explanationTextRenderer;
    private String currentExplanationText = "";
    private PlayerShip playerShip;
    private ArrayList<Ship> allyShips = new ArrayList<Ship>();
    private ArrayList<Ship> enemyShips = new ArrayList<Ship>();
    private ArrayList<Bullet> allyBullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
    private ArrayList<Crate> crates = new ArrayList<Crate>();
    private Terrain terrain;
    private Sea sea = null;
    Timer timer = new Timer();
    private int time = 0;

    /**
     * Reload timer thread
     */
    private class GameTimerTask extends TimerTask {

        int level = 0;

        public GameTimerTask(int level) {
            time = 0;
            this.level = level;
            cleanUpEverything();
        }

        @Override
        public void run() {
            switch (level) {
                case 0:
                    explanation(time);
                    break;
                case 1:
                    level1(time);
                    break;
                case 2:

            }
            time++;
        }
    }

    /** Called by the drawable immediately after the OpenGL context is
     * initialized for the first time. Can be used to perform one-time OpenGL
     * initialization such as setup of lights and display lists.
     * @param gLAutoDrawable The GLAutoDrawable object.
     */
    @Override
    public void init(GLAutoDrawable gLAutoDrawable) {
        gl = gLAutoDrawable.getGL();
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearColor(0.57F, 0.89F, 1.0F, 1.0F);
        gl.glClearDepth(5.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        explanationTextRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
        explanationTextRenderer.setColor(Color.BLACK);
        hudOverlay = new Overlay(gLAutoDrawable);
        gl.glEnable(GL.GL_TEXTURE_2D);

        textureLoader = new TextureLoader(gl, glu);
        modelLoader = new ModelLoader(this);

        sea = new Sea(this, gl, glu);
        terrain = new Terrain(getModelLoader().getModel("PortlandHarbour"));

        hud = new HUD(this, hudOverlay);

        startTimer(1);
    }

    /** Called when the display mode has been changed.
     * @param gLAutoDrawable The GLAutoDrawable object.
     * @param modeChanged Indicates if the video mode has changed.
     * @param deviceChanged Indicates if the video device has changed.
     */
    @Override
    public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean modeChanged, boolean deviceChanged) {
    }

    /** Called by the drawable during the first repaint after the component has
     * been resized. The client can update the viewport and view volume of the
     * window appropriately, for example by a call to
     * GL.glViewport(int, int, int, int); note that for convenience the component
     * has already called GL.glViewport(int, int, int, int)(x, y, width, height)
     * when this method is called, so the client may not have to do anything in
     * this method.
     * @param gLAutoDrawable The GLAutoDrawable object.
     * @param x The X Coordinate of the viewport rectangle.
     * @param y The Y coordinate of the viewport rectanble.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void reshape(GLAutoDrawable gLAutoDrawable, int x, int y, int width, int height) {
        gl = gLAutoDrawable.getGL();
        if (height <= 0) {
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0F, h, 1, 20000);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        hud = new HUD(this, hudOverlay);
    }

    /** Called by the drawable to initiate OpenGL rendering by the client.
     * After all GLEventListeners have been notified of a display event, the
     * drawable will swap its buffers if necessary.
     * @param gLAutoDrawable The GLDrawable object.
     */
    @Override
    public void display(GLAutoDrawable gLAutoDrawable) {
        gl = gLAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

        cleanUpExpiredObjects();

        moveCamera();

        renderAllObjects(gl, gLAutoDrawable);

        gl.glLoadIdentity();

        if (mouseEvent != null) {
            int viewport[] = new int[4];
            double mvmatrix[] = new double[16];
            double projmatrix[] = new double[16];
            int realy = 0;// GL y coord pos
            double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords

            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
            gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, mvmatrix, 0);
            gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projmatrix, 0);
            // note viewport[3] is height of window in pixels
            realy = viewport[3] - (int) y - 1;
            glu.gluUnProject((double) x, (double) realy, 0,
                    mvmatrix, 0,
                    projmatrix, 0,
                    viewport, 0,
                    wcoord, 0);
            try {
                float turretToMouseX = (float) (wcoord[0] - playerShip.getWeaponMounts()[0].getWeapon().getXPos());
                float turretToMouseY = (float) (wcoord[1] - playerShip.getWeaponMounts()[0].getWeapon().getYPos());
                // Fudge Y distance depending on camera angle
                switch (cameraView) {
                    case 0:
                        turretToMouseY += 15.414;
                        break;
                    case 1:
                        turretToMouseY += 15.211;
                        break;
                    case 2:
                        turretToMouseY += 15.111;
                        break;
                    case 3:
                        turretToMouseY += 15.277;
                        break;
                }
                float turretToMouseTheta = (float) java.lang.Math.atan(turretToMouseX / turretToMouseY);
                if (turretToMouseY < 0) {
                    turretToMouseTheta += java.lang.Math.PI;
                }
                playerShip.rotatePrimary((float) -(turretToMouseTheta / 2 / java.lang.Math.PI * 360));
            } catch (NullPointerException e) {/* No primary weapon */

            }

            mouseEvent = null;
        }

        if (firing) {
            getPlayerShip().fireIndividual(0);
        }
        if (accelerating != 0) {
            getPlayerShip().changeSpeed(accelerating);
        }
        if (turning != 0) {
            getPlayerShip().turn(turning);
        }

        gl.glFlush();
    }

    private void startTimer(int level) {
        time = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new GameTimerTask(level), 0, 10);
    }

    public void stopAnimator() {
        animator.stop();
    }

    private void cleanUpEverything() {
        for (int i = 0; i < getAllyShips().size(); i++) {
            getAllyShips().get(i).stop();
        }
        for (int i = 0; i < getEnemyShips().size(); i++) {
            getEnemyShips().get(i).stop();
        }
        for (int i = 0; i < allyBullets.size(); i++) {
            allyBullets.get(i).stop();
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).stop();
        }
        for (int i = 0; i < crates.size(); i++) {
            crates.get(i).stop();
        }

        allyShips.clear();
        enemyShips.clear();
        allyBullets.clear();
        enemyBullets.clear();
        crates.clear();

        playerShip = new PlayerShip(2000, -2350, this, gl, glu);
        playerShip.turn(-70);
        allyShips.add(playerShip);
    }

    private void cleanUpExpiredObjects() {

        for (int i = 0; i < getAllyShips().size(); i++) {
            if (getAllyShips().get(i).hasExpired()) {
                getAllyShips().remove(i);
            }
        }

        for (int i = 0; i < getEnemyShips().size(); i++) {
            if (getEnemyShips().get(i).hasExpired()) {
                getEnemyShips().remove(i);
            }
        }

        for (int i = 0; i < allyBullets.size(); i++) {
            if (allyBullets.get(i).hasExpired()) {
                allyBullets.remove(i);
            }
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            if (enemyBullets.get(i).hasExpired()) {
                enemyBullets.remove(i);
            }
        }

        for (int i = 0; i < crates.size(); i++) {
            if (crates.get(i).hasExpired()) {
                crates.remove(i);
            }
        }
    }

    void changeCameraView() {
        cameraView++;
        if (cameraView > 3) {
            cameraView = 0;
        }
    }

    private void moveCamera() {
        double cameraXPos = 0;
        double cameraYPos = 0;
        double cameraZPos = 0;
        double viewCentreXPos = 0;
        double viewCentreYPos = 0;
        double viewCentreZPos = 0;

        switch (cameraView) {
            case 0:
                cameraXPos = playerShip.getXPos();// + (-10 * Math.sin(playerShip.getYawRadians()));
                cameraYPos = playerShip.getYPos();// + (10 * Math.cos(playerShip.getYawRadians()));
                cameraZPos = 12;
                viewCentreXPos = playerShip.getXPos() + (-400 * Math.sin(playerShip.getYawRadians()));
                viewCentreYPos = playerShip.getYPos() + (400 * Math.cos(playerShip.getYawRadians()));
                viewCentreZPos = 0;
                break;
            case 1:
                cameraXPos = playerShip.getXPos() + (100 * Math.sin(playerShip.getYawRadians()));
                cameraYPos = playerShip.getYPos() + (-100 * Math.cos(playerShip.getYawRadians()));
                cameraZPos = (time < 700.0) ? (7060 - time * 10) : 60;
                viewCentreXPos = playerShip.getXPos() + (-100 * Math.sin(playerShip.getYawRadians()));
                viewCentreYPos = playerShip.getYPos() + (100 * Math.cos(playerShip.getYawRadians()));
                viewCentreZPos = playerShip.getZPos();
                break;
            case 2:
                cameraXPos = playerShip.getXPos() + (100 * Math.sin(playerShip.getYawRadians()));
                cameraYPos = playerShip.getYPos() + (-100 * Math.cos(playerShip.getYawRadians()));
                cameraZPos = 150 + Math.abs(playerShip.getSpeed() * 5);
                viewCentreXPos = playerShip.getXPos() + (-50 * Math.sin(playerShip.getYawRadians()));
                viewCentreYPos = playerShip.getYPos() + (50 * Math.cos(playerShip.getYawRadians()));
                viewCentreZPos = playerShip.getZPos();
                break;
            case 3:
                cameraXPos = playerShip.getXPos() + (200 * Math.sin(playerShip.getYawRadians()));
                cameraYPos = playerShip.getYPos() + (-200 * Math.cos(playerShip.getYawRadians()));
                cameraZPos = 500;
                viewCentreXPos = playerShip.getXPos() + (-200 * Math.sin(playerShip.getYawRadians()));
                viewCentreYPos = playerShip.getYPos() + (200 * Math.cos(playerShip.getYawRadians()));
                viewCentreZPos = playerShip.getZPos();
        }


        glu.gluLookAt(cameraXPos, cameraYPos, cameraZPos, viewCentreXPos, viewCentreYPos, viewCentreZPos, 0, 0, 1);
    }

    private void renderAllObjects(GL gl, GLAutoDrawable gLAutoDrawable) {
        sea.render(gl);
        terrain.render(gl);

        for (int i = 0; i < allyShips.size(); i++) {
            allyShips.get(i).render(gl);
        }

        for (int i = 0; i < enemyShips.size(); i++) {
            enemyShips.get(i).render(gl);
        }

        for (int i = 0; i < allyBullets.size(); i++) {
            allyBullets.get(i).render(gl);
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).render(gl);
        }

        for (int i = 0; i < crates.size(); i++) {
            crates.get(i).render(gl);
        }

        drawGLText(gLAutoDrawable, explanationTextRenderer, 250, -250, currentExplanationText);

        hud.render(gLAutoDrawable.getWidth());
    }

    private void drawGLText(GLAutoDrawable gLAutoDrawable, TextRenderer textRenderer, int x, int y, String text) {
        if (x < 0) {
            x = gLAutoDrawable.getWidth() + x;
        }
        if (y < 0) {
            y = gLAutoDrawable.getHeight() + y;
        }
        textRenderer.beginRendering(gLAutoDrawable.getWidth(), gLAutoDrawable.getHeight());
        textRenderer.draw(text, x, y);
        textRenderer.endRendering();
    }

    public GL getGL() {
        return gl;
    }

    public GLU getGLU() {
        return glu;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    public ModelLoader getModelLoader() {
        return modelLoader;
    }

    public void newMouseMotionEvent(MouseEvent me) {
        mouseEvent = me;
    }

    public void setFiring(boolean b) {
        firing = b;
    }

    public void setAccelerating(int i) {
        accelerating = i;
    }

    public void setTurning(int i) {
        turning = i;
    }

    public HUD getHUD() {
        return hud;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public ArrayList<Ship> getAllyShips() {
        return allyShips;
    }

    public ArrayList<Ship> getEnemyShips() {
        return enemyShips;
    }

    public ArrayList<Bullet> getAllyBullets() {
        return allyBullets;
    }

    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public ArrayList<Crate> getCrates() {
        return crates;
    }

    private void explanation(int i) {
        if (i == 100) {
            currentExplanationText = "This is your ship.\n\rIt's a Type 23 Frigate.";
        } else if (i == 200) {
            getAllyShips().add(new HeavyFrigate(30, -40, this, gl, glu, 0, 4, true, false, false));
        } else if (i == 650) {
            currentExplanationText = "";
            getAllyShips().get(1).changeSpeed(-0.4);
        } else if (i == 1000) {
            timer.cancel();
            startTimer(1);
        }
    }

    private void level1(int i) {
        if (i == 10) {
            currentExplanationText = "Level 1";
            getAllyShips().add(new MarineLandingCraft(1950, -2300, this, gl, glu, 0, 0, true, false, false));
            getAllyShips().add(new MarineLandingCraft(1900, -2300, this, gl, glu, 0, 4, true, false, false));
            getAllyShips().add(new HeavyFrigate(1800, -2300, this, gl, glu, 0, 4, true, false, false));
            getAllyShips().add(new HeavyFrigate(1800, -2200, this, gl, glu, 0, 4, true, false, false));
        } else if (i == 300) {
            currentExplanationText = "";
        } else if (i == 1000) {
            getEnemyShips().add(new MarineLandingCraft(4000, -320, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(4000, -350, this, gl, glu, 90, 4, false, false, true));
            getEnemyShips().add(new MarineLandingCraft(4000, -380, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(3520, -2650, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(3550, -2650, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(3580, -2650, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 3000) {
            getEnemyShips().add(new Type23Frigate(4000, -320, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new Type23Frigate(4000, -380, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 6000) {
            getEnemyShips().add(new HeavyFrigate(4000, -330, this, gl, glu, 90, 4, false, true, false));
            getEnemyShips().add(new HeavyFrigate(4000, -370, this, gl, glu, 90, 4, false, false, true));
        } else if (i == 10000) {
            getEnemyShips().add(new Type23Frigate(4000, -320, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new Type23Frigate(4000, -380, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 10500) {
            getEnemyShips().add(new MarineLandingCraft(3520, -2650, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(3550, -2650, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new MarineLandingCraft(3580, -2650, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 11000) {
            getEnemyShips().add(new Type23Frigate(3520, -2650, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new Type23Frigate(3580, -2650, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 11500) {
            getEnemyShips().add(new HeavyFrigate(3520, -2650, this, gl, glu, 90, 4, false, true, false));
            getEnemyShips().add(new HeavyFrigate(3580, -2650, this, gl, glu, 90, 4, false, true, false));
        } else if (i == 13000) {
            getEnemyShips().add(new Type23Frigate(4000, -320, this, gl, glu, 90, 4, false, false, false));
            getEnemyShips().add(new Type23Frigate(4000, -380, this, gl, glu, 90, 4, false, false, false));
        } else if (i == 15000) {
            getEnemyShips().add(new Battleship(4000, -350, this, gl, glu, 90, 4, false, true, true));
        }
    }
}
