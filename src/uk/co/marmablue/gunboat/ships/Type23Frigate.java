package uk.co.marmablue.gunboat.ships;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class Type23Frigate extends Ship {

    public Type23Frigate(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, double heading, double speed, boolean allyControlled, boolean dropsHealth, boolean dropsWeapon) {
        super(renderer, renderer.getModelLoader().getModel("EnemyType23Frigate"), xPos, yPos, 0, heading, speed, -3, 15, 200, 100, new WeaponMountPoint[]{new WeaponMountPoint(0, 15, 5)}, allyControlled, true, dropsHealth?500:0, dropsWeapon?"FourPointFive":"");
        getWeaponMounts()[0].setWeapon(this, renderer, "FourPointFive", allyControlled, true);
    }
}