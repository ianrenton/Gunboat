package uk.co.marmablue.gunboat.crates;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;

/**
 *
 * @author Tsuki
 */
public class HealthCrate extends Crate {

    private int healAmount;

    public HealthCrate(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, int healAmount) {
        super(renderer.getModelLoader().getModel("HealthCrate"), xPos, yPos, renderer, gl, glu);
        this.healAmount = healAmount;
    }

    @Override
    void doEffect() {
        renderer.getPlayerShip().damage(-healAmount);
    }
}
