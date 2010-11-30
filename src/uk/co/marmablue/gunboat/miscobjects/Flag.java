package uk.co.marmablue.gunboat.miscobjects;

import uk.co.marmablue.gunboat.model.Model;

public class Flag extends RenderableGameObject {

    public Flag(Model model, RenderableGameObject host) {
        super(model, host, 0, 0, 0.2, host.getYaw(), 0);
    }

    @Override
    public void iterate() {
        // Don't iterate a flag automatically, the host will do that.  This
        // makes sure the two positions update together, so there's no jitter.
    }

    public void iterateFromHost() {
        super.iterate();
    }

    @Override
    void updateHostPosition() {
        super.updateHostPosition();
        setHostPitch(0);
        setHostRoll(0);
    }

    @Override
    public boolean hasExpired() {
        return getHost().hasExpired();
    }
}
