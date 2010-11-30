/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.marmablue.gunboat.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import uk.co.marmablue.gunboat.gui.GameRenderer;

/**
 *
 * @author Ian
 */
public class ModelLoader {

    private HashMap models = new HashMap();
    private GameRenderer renderer;

    public ModelLoader(GameRenderer renderer) {
        this.renderer = renderer;
        loadModels();
    }

    public void loadModels() {
        File dir = new File("models/");
        String[] modelFiles = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return (name.endsWith(".mod"));
            }
        });
        for (int i = 0; i < modelFiles.length; i++) {
            models.put(modelFiles[i], new Model(new File("models/"+modelFiles[i]), renderer));
        }

    }

    public Model getModel(String modelName) {
        return ((Model) models.get(modelName+".mod")).clone();
    }
}
