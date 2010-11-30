package uk.co.marmablue.gunboat.ships;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;

public class PlayerShip extends Ship {

    private ArrayList<String> primaryWeapons = new ArrayList<String>();
    private int currentPrimary = 0;

    public PlayerShip(double xPos, double yPos, GameRenderer renderer, GL gl, GLU glu) {
        super(renderer, renderer.getModelLoader().getModel("Type23Frigate"), xPos, yPos, 0, 0, 0, -3, 20, 10000, 0, new WeaponMountPoint[]{new WeaponMountPoint(0, 15, 5)}, true, false, 0, "");
        primaryWeapons.add("FourPointFive");
        switchPrimary();
    }

    public ArrayList<String> getPrimaryWeapons() {
        return primaryWeapons;
    }

    public int getPrimaryWeaponSelected() {
        return currentPrimary;
    }

    public void switchPrimary() {
        if (++currentPrimary >= primaryWeapons.size()) {
            currentPrimary = 0;
        }
        getWeaponMounts()[0].setWeapon(this, renderer, primaryWeapons.get(currentPrimary), true, false);
    }

    public void addPrimary(String name) {
        boolean alreadyExists = false;
        // Check to see if this weapon is already in the roster.  If it is,
        // replace it with the new one.  (This will give the player the impression
        // of an ammo refill.)
        for (int i = 0; i < primaryWeapons.size(); i++) {
            if (name.equals(primaryWeapons.get(i))) {
                primaryWeapons.remove(i);
                primaryWeapons.add(i, name);
                alreadyExists = true;
            }
        }
        // If it doesn't already exist, we can add a new one.
        if (!alreadyExists) {
            // If we don't currently have 5, add it to the end.
            if (primaryWeapons.size() < 5) {
                primaryWeapons.add(name);
            // If we do have 5, replace the current weapon.
            } else {
                primaryWeapons.remove(currentPrimary);
                primaryWeapons.add(currentPrimary, name);
            }
        }
    }
}