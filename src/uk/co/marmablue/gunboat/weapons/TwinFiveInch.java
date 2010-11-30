package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import uk.co.marmablue.gunboat.bullets.FourPointFiveRound;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class TwinFiveInch extends Weapon {

    public TwinFiveInch(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("TwinFiveInch"), host, renderer, mountPoint, heading, 3, 250, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new FourPointFiveRound(absPos[0]-0.035, absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+50), getRange(), renderer, isAllyControlled()));
        bulletList.add(new FourPointFiveRound(absPos[0]+0.035, absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+50), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Twin QF 5.25-inch Mark 1";
    }
}
