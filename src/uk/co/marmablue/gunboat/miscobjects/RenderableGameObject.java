package uk.co.marmablue.gunboat.miscobjects;

import java.util.Timer;
import java.util.TimerTask;
import javax.media.opengl.GL;
import uk.co.marmablue.gunboat.model.Model;

/**
 *
 * @author Ian
 */
public abstract class RenderableGameObject {

    public final double iterationRateHz = 20;
    private double xPos = 0;
    private double yPos = 0;
    private double zPos = 0;
    private double pitch = 0;
    private double roll = 0;
    private double yaw = 0;
    private double speed = 0;
    private Model model;
    private double length = 0;
    private double width = 0;
    private RenderableGameObject host = null;
    private double hostXPos = 0;
    private double hostYPos = 0;
    private double hostZPos = 0;
    private double hostPitch = 0;
    private double hostRoll = 0;
    private double hostYaw = 0;
    private double hostSpeed = 0;
    Timer timer = new Timer();
    public int iteration;

    /**
     * @return the host
     */
    public RenderableGameObject getHost() {
        return host;
    }

    /**
     * Reload timer thread
     */
    private class iterationTimerTask extends TimerTask {

        int count = 0;

        @Override
        public void run() {
            iterate();
        }
    }

    public RenderableGameObject(Model model, RenderableGameObject host, double xPos, double yPos, double zPos, double yaw, double speed) {
        this.model = model;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.yaw = yaw;
        this.speed = speed;
        this.host = host;
        this.length = model.getLength();
        this.width = model.getWidth();

        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new iterationTimerTask(), 0, (int) (1000.0 / iterationRateHz));
    }

    /**
     * Get the X, Y and Z positions as an array.
     * @return
     */
    public double[] getPosition() {
        return new double[]{getXPos(), getYPos(), getZPos()};
    }

    /**
     * Get the pitch, roll and yaw as an array.
     * @return
     */
    public double[] getAngles() {
        return new double[]{getPitch(), getRoll(), getYaw()};
    }

    public void render(GL gl) {
        gl.glTranslated(getHostXPos(), getHostYPos(), getHostZPos());
        gl.glRotated(getHostPitch(), 1.0, 0.0, 0.0);
        gl.glRotated(getHostRoll(), 0.0, 1.0f, 0.0);
        gl.glRotated(getHostYaw(), 0.0, 0.0, 1.0);
        gl.glTranslated(xPos, yPos, zPos);
        gl.glRotated(pitch, 1.0, 0.0, 0.0);
        gl.glRotated(roll, 0.0, 1.0, 0.0);
        gl.glRotated(yaw, 0.0, 0.0, 1.0);
        model.render(gl);
        gl.glRotated(-yaw, 0.0, 0.0, 1.0);
        gl.glRotated(-roll, 0.0, 1.0, 0.0);
        gl.glRotated(-pitch, 1.0, 0.0, 0.0);
        gl.glTranslated(-xPos, -yPos, -zPos);
        gl.glRotated(-getHostYaw(), 0.0, 0.0, 1.0);
        gl.glRotated(-getHostRoll(), 0.0, 1.0, 0.0);
        gl.glRotated(-getHostPitch(), 1.0, 0.0, 0.0);
        gl.glTranslated(-getHostXPos(), -getHostYPos(), -getHostZPos());
    }

    public boolean isOffScreen() {
        return (getZPos() < -10);
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

    /**
     * @return the pitch
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * @return the roll
     */
    public double getRoll() {
        return roll;
    }

    /**
     * @return the yaw
     */
    public double getYaw() {
        return yaw;
    }

    public double getYawRadians() {
        return yaw / 180 * Math.PI;
    }

    public double getHostYawRadians() {
        return hostYaw / 180 * Math.PI;
    }

    public double getAbsYawRadians() {
        return (yaw + hostYaw) / 180 * Math.PI;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param xPos the xPos to set
     */
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    /**
     * @param yPos the yPos to set
     */
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    /**
     * @param zPos the zPos to set
     */
    public void setZPos(double zPos) {
        this.zPos = zPos;
    }

    /**
     * @param pitch the pitch to set
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    /**
     * @param roll the roll to set
     */
    public void setRoll(double roll) {
        this.roll = roll;
    }

    /**
     * @param yaw the yaw to set
     */
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    /**
     * @param yaw the yaw to set
     */
    public void setYawRadians(double yaw) {
        this.yaw = yaw * 180 / Math.PI;
    }

    public double turn(double change) {
        double newYaw = getYaw() + change;
        if (newYaw >= 360) {
            newYaw -= 360;
        } else if (newYaw < 0) {
            newYaw += 360;
        }
        setYaw(newYaw);
        return getYaw();
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    void setHostPitch(double i) {
        hostPitch = i;
    }

    void setHostRoll(double i) {
        hostRoll = i;
    }

    public void iterate() {
        double xMove = getSpeed() / iterationRateHz * (double) java.lang.Math.sin(-getYawRadians());
        double yMove = getSpeed() / iterationRateHz * (double) java.lang.Math.cos(-getYawRadians());
        setXPos(getXPos() + xMove);
        setYPos(getYPos() + yMove);
        updateHostPosition();
        iteration++;
    }

    void updateHostPosition() {
        if (getHost() != null) {
            hostXPos = getHost().getXPos();
            hostYPos = getHost().getYPos();
            hostZPos = getHost().getZPos();
            hostPitch = getHost().getPitch();
            hostRoll = getHost().getRoll();
            hostYaw = getHost().getYaw();
            hostSpeed = getHost().getSpeed();
        }
    }

    /**
     * @return the hostXPos
     */
    public double getHostXPos() {
        return hostXPos;
    }

    /**
     * @return the hostYPos
     */
    public double getHostYPos() {
        return hostYPos;
    }

    /**
     * @return the hostZPos
     */
    public double getHostZPos() {
        return hostZPos;
    }

    /**
     * @return the hostXPos
     */
    public double[] getAbsPosition() {
        double newX = (xPos * Math.cos(getHostYawRadians()) - yPos * Math.sin(getHostYawRadians()));
        double newY = (-xPos * Math.sin(getHostYawRadians()) + yPos * Math.cos(getHostYawRadians()));
        return new double[]{hostXPos + newX, hostYPos + newY, hostZPos + zPos};
    }

    /**
     * @return the hostPitch
     */
    public double getHostPitch() {
        return hostPitch;
    }

    /**
     * @return the hostRoll
     */
    public double getHostRoll() {
        return hostRoll;
    }

    /**
     * @return the hostYaw
     */
    public double getHostYaw() {
        return hostYaw;
    }

    public double[] getRelativePositionOf(RenderableGameObject other) {
        double realX = other.getXPos() - this.getAbsPosition()[0];
        double realY = -(other.getYPos() - this.getAbsPosition()[1]);
        double[] relPos = new double[]{(realX * Math.cos(getAbsYawRadians() + Math.PI / 2) - realY * Math.sin(getAbsYawRadians() + Math.PI / 2)), (realX * Math.sin(getAbsYawRadians() + Math.PI / 2) + realY * Math.cos(getAbsYawRadians() + Math.PI / 2))};
        return relPos;
    }

    public double getRelativeAngleTo(RenderableGameObject other) {
        double[] relPos = getRelativePositionOf(other);
        double angle = Math.atan2(relPos[1], relPos[0]);
        //System.out.println("y:" + (other.getYPos() - this.getAbsPosition()[1]) + "  x:" + (other.getXPos() - this.getAbsPosition()[0]) + "  currYaw:" + getAbsYawRadians()/Math.PI*180);
        //System.out.println("y:" + relPos[1] + "  x:" + relPos[0] + "  angle:" + angle/Math.PI*180);
        /*if (relPos[1] > 0) {
        angle = angle + Math.PI;
        }
        angle = angle + (Math.PI/2 * (-Math.cos(2*getHostYawRadians()) + 1));*/
        return angle;
    }

    public double getDistanceTo(RenderableGameObject other) {
        double[] relPos = getRelativePositionOf(other);
        return Math.sqrt((relPos[0] * relPos[0]) + (relPos[1] * relPos[1]));
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the hostSpeed
     */
    public double getHostSpeed() {
        return hostSpeed;
    }

    public void stop() {
        timer.cancel();
    }

    public abstract boolean hasExpired();
}
