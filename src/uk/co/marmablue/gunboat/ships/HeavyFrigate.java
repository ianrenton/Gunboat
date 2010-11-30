package uk.co.marmablue.gunboat.ships;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class HeavyFrigate extends Ship {

    public HeavyFrigate(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, double heading, double speed, boolean allyControlled, boolean dropsHealth, boolean dropsWeapon) {
        super(renderer, renderer.getModelLoader().getModel("Type23Frigate"), xPos, yPos, 0, heading, speed, -3, 15, 1000, 10, new WeaponMountPoint[]{new WeaponMountPoint(0, 15, 5)}, allyControlled, true, dropsHealth?1000:0, dropsWeapon?"TwinFifteenInch":"");
        getWeaponMounts()[0].setWeapon(this, renderer, "TwinFifteenInch", allyControlled, true);
    }
}