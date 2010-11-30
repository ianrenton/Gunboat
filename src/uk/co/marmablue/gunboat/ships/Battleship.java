package uk.co.marmablue.gunboat.ships;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class Battleship extends Ship {

    public Battleship(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu, double heading, double speed, boolean allyControlled, boolean dropsHealth, boolean dropsWeapon) {
        super(renderer, renderer.getModelLoader().getModel("EnemyBattleship"), xPos, yPos, 0, heading, speed, -3, 10, 50000, 200, new WeaponMountPoint[]{new WeaponMountPoint(0, 75, 5), new WeaponMountPoint(0, 62, 7.5), new WeaponMountPoint(0, -62, 7.5), new WeaponMountPoint(0, -75, 5), new WeaponMountPoint(7.5, 50, 5), new WeaponMountPoint(7.5, 25, 5), new WeaponMountPoint(7.5, 0, 5), new WeaponMountPoint(7.5, -50, 5), new WeaponMountPoint(-7.5, 50, 5), new WeaponMountPoint(-7.5, 25, 5), new WeaponMountPoint(-7.5, 0, 5), new WeaponMountPoint(-7.5, -50, 5)}, allyControlled, true, dropsHealth?10000:0, dropsWeapon?"TwinFifteenInch":"");
        getWeaponMounts()[0].setWeapon(this, renderer, "TwinFifteenInch", allyControlled, true);
        getWeaponMounts()[1].setWeapon(this, renderer, "TwinFifteenInch", allyControlled, true);
        getWeaponMounts()[2].setWeapon(this, renderer, "TwinFifteenInch", allyControlled, true);
        getWeaponMounts()[3].setWeapon(this, renderer, "TwinFifteenInch", allyControlled, true);
        getWeaponMounts()[4].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[5].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[6].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[7].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[8].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[9].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[10].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
        getWeaponMounts()[11].setWeapon(this, renderer, "TwinFiveInch", allyControlled, true);
    }
}