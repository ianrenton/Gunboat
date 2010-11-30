package uk.co.marmablue.gunboat.model;

import javax.media.opengl.GL;

/**
 *
 * @author Ian
 */
public class Quad {

    private Vertex[] vertices = new Vertex[4];
    private int texture;

    public Quad(Vertex[] vertices, int texture) {
        this.vertices = vertices;
        this.texture = texture;
    }

    public void render(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glColor3f(1f, 1f, 1f);

        gl.glBegin(GL.GL_QUADS);
        for (int i = 0; i < 4; i++) {
            gl.glTexCoord2d(vertices[i].u, vertices[i].v);
            gl.glVertex3d(vertices[i].x, vertices[i].y, vertices[i].z);
        }
        gl.glEnd();

        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    public double[] bottomLeftPos() {
        return new double[]{vertices[0].x, vertices[1].y};
    }

    Vertex[] getVertices() {
        return vertices;
    }
}
