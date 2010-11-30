package uk.co.marmablue.gunboat.gui;

import uk.co.marmablue.gunboat.gui.GLDisplay;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class KeyboardHandler extends KeyAdapter {

    private GameRenderer renderer;

    public KeyboardHandler(GameRenderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                renderer.setAccelerating(1);
                break;
            case KeyEvent.VK_S:
                renderer.setAccelerating(-1);
                break;
            case KeyEvent.VK_A:
                renderer.setTurning(1);
                break;
            case KeyEvent.VK_D:
                renderer.setTurning(-1);
                break;
            case KeyEvent.VK_Z:
                renderer.setFiring(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                renderer.setAccelerating(0);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                renderer.setTurning(0);
                break;
            case KeyEvent.VK_Z:
                renderer.setFiring(false);
                break;
            case KeyEvent.VK_Q:
                renderer.getPlayerShip().switchPrimary();
                break;
            case KeyEvent.VK_R:
                renderer.getHUD().changeRadarMode();
                break;
            case KeyEvent.VK_C:
                renderer.changeCameraView();
                break;
            case KeyEvent.VK_H:
                renderer.getHUD().toggle();
                break;
            case KeyEvent.VK_N:
                renderer.getPlayerShip().addPrimary("SuperUltraMegaNukeLauncher");
                break;
        }
    }
}
