package uk.co.marmablue.gunboat.ships;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class MarineLandingCraft extends Ship {

    public MarineLandingCraft(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, double heading, double speed, boolean allyControlled, boolean dropsHealth, boolean dropsWeapon) {
        super(renderer, renderer.getModelLoader().getModel("MarineLandingCraft"), xPos, yPos, 0, heading, speed, -10, 20, 50, 10, new WeaponMountPoint[]{new WeaponMountPoint(0, 0, 0.5)}, allyControlled, true, dropsHealth?5000:0, dropsWeapon?"Vickers303":"");
        getWeaponMounts()[0].setWeapon(this, renderer, "Vickers303", allyControlled, true);
    }
}