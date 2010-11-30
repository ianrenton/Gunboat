package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import uk.co.marmablue.gunboat.bullets.LaserBeam;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class PrototypeLaser extends Weapon {

    public PrototypeLaser(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("PrototypeLaser"), host, renderer, mountPoint, heading, 1, 5000, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new LaserBeam(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+100), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Naval Laser Prototype";
    }
}
