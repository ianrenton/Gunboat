package uk.co.marmablue.gunboat.miscobjects;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import uk.co.marmablue.gunboat.gui.GameRenderer;

/**
 *
 * @author Ian
 */
public class Sea extends RenderableGameObject {

    private GameRenderer renderer;

    public Sea(GameRenderer renderer, GL gl, GLU glu) {
        super(renderer.getModelLoader().getModel("Waves"), null, 0, 0, 0, 0, 0);
        this.renderer = renderer;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }
}
