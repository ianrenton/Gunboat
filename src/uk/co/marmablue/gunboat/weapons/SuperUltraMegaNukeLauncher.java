package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import uk.co.marmablue.gunboat.bullets.SuperUltraMegaNuke;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class SuperUltraMegaNukeLauncher extends Weapon {

    public SuperUltraMegaNukeLauncher(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("PrototypeLaser"), host, renderer, mountPoint, heading, 5, 100000, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new SuperUltraMegaNuke(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+50), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Super Ultra Mega Nuke Launcher";
    }
}
