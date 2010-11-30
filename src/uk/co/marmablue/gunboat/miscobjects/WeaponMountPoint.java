package uk.co.marmablue.gunboat.miscobjects;

import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;
import uk.co.marmablue.gunboat.weapons.TwinFifteenInch;
import uk.co.marmablue.gunboat.weapons.FourPointFive;
import uk.co.marmablue.gunboat.weapons.HarpoonLauncher;
import uk.co.marmablue.gunboat.weapons.PrototypeLaser;
import uk.co.marmablue.gunboat.weapons.SuperUltraMegaNukeLauncher;
import uk.co.marmablue.gunboat.weapons.TwinFiveInch;
import uk.co.marmablue.gunboat.weapons.Vickers303;
import uk.co.marmablue.gunboat.weapons.Weapon;

/**
 *
 * @author Ian
 */
public class WeaponMountPoint {
    private double xPos;
    private double yPos;
    private double zPos;
    private Weapon weapon = null;

    public WeaponMountPoint(double xPos, double yPos, double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    /**
     * @return the xPos
     */
    public double getXPos() {
        return xPos;
    }

    /**
     * @return the yPos
     */
    public double getYPos() {
        return yPos;
    }

    /**
     * @return the zPos
     */
    public double getZPos() {
        return zPos;
    }

    public double[] getPosition() {
        return new double[]{xPos, yPos, zPos};
    }

    /**
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * @param weapon the weapon to set
     */
    public void setWeapon(Ship host, GameRenderer renderer, String WeaponName, boolean allyControlled, boolean autonomous) {
        double prevWeaponYaw = 0;
        if (weapon != null) prevWeaponYaw = getWeapon().getYaw();

        if (WeaponName.equals("TwinFiveInch")) {
            weapon = new TwinFiveInch(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("TwinFifteenInch")) {
            weapon = new TwinFifteenInch(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("FourPointFive")) {
            weapon = new FourPointFive(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("PrototypeLaser")) {
            weapon = new PrototypeLaser(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("Vickers303")) {
            weapon = new Vickers303(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("HarpoonLauncher")) {
            weapon = new HarpoonLauncher(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        } else if (WeaponName.equals("SuperUltraMegaNukeLauncher")) {
            weapon = new SuperUltraMegaNukeLauncher(host, renderer, prevWeaponYaw, getPosition(), allyControlled, autonomous);
        }
    }
}
