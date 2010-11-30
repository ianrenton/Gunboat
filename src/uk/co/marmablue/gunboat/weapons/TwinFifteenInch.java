package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import uk.co.marmablue.gunboat.bullets.FifteenInchRound;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class TwinFifteenInch extends Weapon {

    public TwinFifteenInch(Ship host, GameRenderer renderer, double heading, double[] mountPoint, boolean allyControlled, boolean autonomous) {
        super(renderer.getModelLoader().getModel("TwinFifteenInch"), host, renderer, mountPoint, heading, 15, 350, allyControlled, autonomous);
    }

    @Override
    public void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer) {
        double[] absPos = getAbsPosition();
        bulletList.add(new FifteenInchRound(absPos[0]-1.75, absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+25), getRange(), renderer, isAllyControlled()));
        bulletList.add(new FifteenInchRound(absPos[0]+1.75, absPos[1], absPos[2], (getYaw()+getHostYaw()), (getSpeed()+getHostSpeed()+25), getRange(), renderer, isAllyControlled()));
    }

    @Override
    public String getName() {
        return "Twin BL 15-inch Mark 1";
    }
}
