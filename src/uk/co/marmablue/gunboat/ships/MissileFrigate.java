package uk.co.marmablue.gunboat.ships;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class MissileFrigate extends Ship {

    public MissileFrigate(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, double heading, double speed, boolean allyControlled, boolean dropsHealth, boolean dropsWeapon) {
        super(renderer, renderer.getModelLoader().getModel("EnemyType23Frigate"), xPos, yPos, 0, heading, speed, -3, 15, 200, 200, new WeaponMountPoint[]{new WeaponMountPoint(0, 15, 5)}, allyControlled, true, dropsHealth?1000:0, dropsWeapon?"HarpoonLauncher":"");
        getWeaponMounts()[0].setWeapon(this, renderer, "HarpoonLauncher", allyControlled, true);
    }
}