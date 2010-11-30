package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.HarpoonMissile;
import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class HarpoonLauncher extends Weapon {

    public HarpoonLauncher(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("HarpoonLauncher"), host, renderer, mountPoint, heading, 20, 5000, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new HarpoonMissile(absPos[0], absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+30), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Harpoon Launcher";
    }
}
