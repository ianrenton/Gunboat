package uk.co.marmablue.gunboat.gui;

import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.gui.GLDisplay;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseHandler extends MouseAdapter {
    private GameRenderer renderer;

    public MouseHandler(GameRenderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            renderer.setFiring(true);
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            renderer.setFiring(false);
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            renderer.newMouseMotionEvent(me);
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            renderer.newMouseMotionEvent(me);
            renderer.setFiring(true);
        }
}
