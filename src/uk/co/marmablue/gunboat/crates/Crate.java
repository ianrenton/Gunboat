package uk.co.marmablue.gunboat.crates;

import uk.co.marmablue.gunboat.miscobjects.*;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.model.Model;

/**
 *
 * @author Tsuki
 */
public abstract class Crate extends RenderableGameObject {

    GameRenderer renderer;
    private boolean readyForCollisionCheck = false;
    private boolean removable = false;

    public Crate(Model model, double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu) {
        super(model, null, xPos, yPos, 0, 0, 0);
        this.renderer = renderer;
        readyForCollisionCheck = true;
    }

    @Override
    public void iterate() {
        super.iterate();
        if (readyForCollisionCheck) {
            if (renderer.getPlayerShip().checkForCollision(this)) {
                doEffect();
                removable = true;
            }
        }
    }

    @Override
    public boolean hasExpired() {
        if (removable || isOffScreen()) {
            this.stop();
            return true;
        } else return false;
    }

    abstract void doEffect();
}
