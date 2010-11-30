package uk.co.marmablue.gunboat.crates;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;

/**
 *
 * @author Tsuki
 */
public class PrimaryWeaponCrate extends Crate {

    private String weapon = "";

    public PrimaryWeaponCrate(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, String weapon) {
        super(renderer.getModelLoader().getModel("PrimaryWeaponCrate"), xPos, yPos, renderer, gl, glu);
        this.weapon = weapon;
    }

    @Override
    void doEffect() {
        renderer.getPlayerShip().addPrimary(weapon);
    }
}
