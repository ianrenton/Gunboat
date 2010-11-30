package uk.co.marmablue.gunboat.ships;

import java.util.ArrayList;
import javax.media.opengl.GL;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.crates.HealthCrate;
import uk.co.marmablue.gunboat.crates.PrimaryWeaponCrate;
import uk.co.marmablue.gunboat.miscobjects.Flag;
import uk.co.marmablue.gunboat.miscobjects.RenderableGameObject;
import uk.co.marmablue.gunboat.miscobjects.ShipHealthBar;
import uk.co.marmablue.gunboat.miscobjects.WeaponMountPoint;
import uk.co.marmablue.gunboat.model.Model;

/**
 * @author Ian
 */
public abstract class Ship extends RenderableGameObject {
    // This scales down motion to produce sensible speeds when iterate() is
    // called at 25ms intervals by the game panel

    boolean destroyed = false;
    private double health = 1000;
    double maxHealth = 1000;
    private double maxSpeed = 20;
    private double minSpeed = -3;
    private ShipHealthBar healthbar;
    private Flag flag;
    private boolean removable = false;
    GameRenderer renderer;
    private WeaponMountPoint[] weaponMounts;
    private boolean allyControlled;
    private boolean autonomous;
    private ArrayList<Ship> targets;
    private double safetyDistance;
    private boolean droppedCrateAlready = false;
    private int healthDrop;
    private String weaponDrop;

    /**
     * Creates a new generic vessel.  Subclasses should call this from their
     * own constructors
     * @param startPos Initial position
     * @param yaw Initial yaw
     * @param speed Initial speed
     * @param sonarMode Sonar mode (0=None, 1=Passive, 2=Active)
     * @param sonarRange Range of the sonar, if present
     * @param hasNoiseMaker Does the vehicle have a noisemaker?
     * @param hasSweep Does the vehicle have a sweep?
     */
    public Ship(GameRenderer renderer, Model model, double xPos, double yPos, double zPos, double heading, double speed, double minSpeed, double maxSpeed, double maxHealth, double safetyDistance, WeaponMountPoint[] weaponMounts, boolean allyControlled, boolean autonomous, int healthDrop, String weaponDrop) {
        super(model, null, xPos, yPos, zPos, 0, 0);
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.renderer = renderer;
        this.safetyDistance = safetyDistance;
        this.weaponMounts = weaponMounts;
        this.allyControlled = allyControlled;
        this.autonomous = autonomous;
        this.healthDrop = healthDrop;
        this.weaponDrop = weaponDrop;
        healthbar = new ShipHealthBar(this);
        if (allyControlled) {
            flag = new Flag(renderer.getModelLoader().getModel("Flag_uk"), this);
        } else {
            flag = new Flag(renderer.getModelLoader().getModel("Flag_china"), this);
        }
        turn(heading);
        changeSpeed(speed);
    }

    public void fire() {
        if (weaponMounts != null) {
            for (int i = 0; i < weaponMounts.length; i++) {
                if ((weaponMounts[i] != null) && (weaponMounts[i].getWeapon() != null)) {
                    getWeaponMounts()[i].getWeapon().fire(renderer);
                }
            }
        }
    }

    public void fireIndividual(int i) {
        if ((weaponMounts != null) && (weaponMounts[i] != null) && (weaponMounts[i].getWeapon() != null)) {
            getWeaponMounts()[i].getWeapon().fire(renderer);
        }
    }

    public void rotatePrimary(double newAngle) {
        getWeaponMounts()[0].getWeapon().setYaw(newAngle);
    }

    public boolean checkForCollision(RenderableGameObject other) {
        if (!destroyed) {
            double distanceToOtherVessel = getDistanceTo(other);
            double angleToOtherVessel = getRelativeAngleTo(other);
            double longDist = java.lang.Math.abs(distanceToOtherVessel * java.lang.Math.cos(angleToOtherVessel));
            double athwDist = java.lang.Math.abs(distanceToOtherVessel * java.lang.Math.sin(angleToOtherVessel));
            return ((athwDist <= getWidth() / 2) && (longDist <= getLength() / 2));
        } else {
            return false;
        }
    }

    public double changeSpeed(double change) {
        double newSpeed = getSpeed() + change;
        if (newSpeed < getMinSpeed()) {
            newSpeed = getMinSpeed();
        } else if (newSpeed > getMaxSpeed()) {
            newSpeed = getMaxSpeed();
        }
        setSpeed(newSpeed);
        return getSpeed();
    }

    @Override
    public void iterate() {

        // Autonomous movement for non-player-controlled ships
        if (autonomous) {
            if (allyControlled) {
                // Find enemies
                targets = renderer.getEnemyShips();
                if (targets.size() == 0) {
                    // If no enemies, flock together
                    targets = renderer.getAllyShips();
                }
            } else {
                // Find enemies
                targets = renderer.getAllyShips();
                if (targets.size() == 0) {
                    // If no enemies, flock together
                    targets = renderer.getEnemyShips();
                }
            }

            try {
                int closest = 999999;
                double closestDistance = 999999;
                for (int i = 0; i < targets.size(); i++) {
                    // If target is closer than previous ones, and the target isn't this ship itself, and the target isn't destroyed already...
                    if ((getDistanceTo(targets.get(i)) < closestDistance) && (getDistanceTo(targets.get(i)) > 5) && (!targets.get(i).isDestroyed())) {
                        closestDistance = getDistanceTo(targets.get(i));
                        closest = i;
                    }
                }

                if (closestDistance != 999999) {
                    double angle = -getRelativeAngleTo(targets.get(closest))/Math.PI*180;
                    if (angle < -1) {
                        turn(-1);
                    } else if (angle > 1) {
                        turn(1);
                    }

                    double desiredSpeed = 0;
                    if (closestDistance > 500) {
                        desiredSpeed = maxSpeed;
                    } else if (closestDistance < safetyDistance) {
                        desiredSpeed = minSpeed;
                    } else {
                        desiredSpeed = (closestDistance - safetyDistance) / (500-safetyDistance) * maxSpeed;
                    }
                    setSpeed(desiredSpeed);
                }
            } catch (Exception e) {
            }
        }

        super.iterate();

        if (weaponMounts != null) {
            for (int i = 0; i < weaponMounts.length; i++) {
                if ((weaponMounts[i] != null) && (weaponMounts[i].getWeapon() != null)) {
                    weaponMounts[i].getWeapon().iterateFromHost();
                }
            }
        }
        if (flag != null) {
            flag.iterateFromHost();
        }

        if (health <= 0) {
            destroy();
        }

        if (isDestroyed()) {
            if (getPitch() < 45) {
                setPitch(getPitch() + 1);
            } else if (getZPos() > -50) {
                setZPos(getZPos() - 1);
            } else {
                removable = true;
            }
        }
    }

    /**
     * Stops the vessel and marks it as destroyed if it takes damage.
     */
    void destroy() {
        setSpeed(0);
        destroyed = true;
    }

    /**
     * Is the vessel destroyed?
     * @return
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void render(GL gl) {
        super.render(gl);

        if (weaponMounts != null) {
            for (int i = 0; i < weaponMounts.length; i++) {
                if ((weaponMounts[i] != null) && (weaponMounts[i].getWeapon() != null)) {
                    weaponMounts[i].getWeapon().render(gl);
                }
            }
        }

        if (!destroyed) {
            healthbar.render(gl);
            flag.render(gl);
        }
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return the removable
     */
    @Override
    public boolean hasExpired() {
        if (removable || isOffScreen()) {
            for (int i = 0; i < weaponMounts.length; i++) {
                weaponMounts[i].getWeapon().stop();
            }
            if (!droppedCrateAlready) {
                if (healthDrop > 0) {
                    renderer.getCrates().add(new HealthCrate(getXPos(), getYPos(), renderer, renderer.getGL(), renderer.getGLU(), healthDrop));
                    droppedCrateAlready = true;
                }
                if (!weaponDrop.equals("")) {
                    renderer.getCrates().add(new PrimaryWeaponCrate(getXPos(), getYPos(), renderer, renderer.getGL(), renderer.getGLU(), weaponDrop));
                    droppedCrateAlready = true;
                }
            }
            this.stop();
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param health health points by which to damage the ship
     */
    public void damage(double health) {
        this.health = this.health - health;
    }

    /**
     * @return the maxSpeed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @return the minSpeed
     */
    public double getMinSpeed() {
        return minSpeed;
    }

    public double getCompassHeading() {
        if (getYaw() == 0) {
            return 0;
        } else {
            return 360 - getYaw();
        }
    }

    /**
     * @return the weaponMounts
     */
    public WeaponMountPoint[] getWeaponMounts() {
        return weaponMounts;
    }

    public void setAutonomous(boolean autonomous) {
        this.autonomous = autonomous;
    }
}
