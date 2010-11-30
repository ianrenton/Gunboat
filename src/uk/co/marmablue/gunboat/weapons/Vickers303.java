package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import java.util.Random;
import uk.co.marmablue.gunboat.bullets.Point303Round;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class Vickers303 extends Weapon {

    Random random = new Random();

    public Vickers303(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("Vickers303"), host, renderer, mountPoint, heading, 1, 100, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new Point303Round(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()+(random.nextFloat()*40-20)), (getSpeed()+getHostSpeed()+75), getRange(), renderer, isAllyControlled()));
        bulletList.add(new Point303Round(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()+(random.nextFloat()*40-20)), (getSpeed()+getHostSpeed()+75), getRange(), renderer, isAllyControlled()));
        bulletList.add(new Point303Round(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()+(random.nextFloat()*40-20)), (getSpeed()+getHostSpeed()+75), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Vickers .303 Machinegun";
    }
}
