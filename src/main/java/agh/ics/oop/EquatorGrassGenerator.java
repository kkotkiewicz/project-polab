package agh.ics.oop;

import java.util.HashMap;

public class EquatorGrassGenerator implements IGrassGenerator{
    private final int width;
    private final int height;
    EquatorGrassGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector2d generateGrass(HashMap<Vector2d, Plant> plantPositions) {
        int y;
        int x;
        Vector2d newGrass;
        if (Math.random() < 0.2) {
            for (int i = 0; i < 10; i++) {
                if (Math.random() < 0.5) {
                    y = (int) (Math.random() * (0.4 * height) + 0.6 * height);
                }
                else {
                    y = (int) (Math.random() * (0.4 * height));
                }
                x = (int) (Math.random() * width);
                newGrass = new Vector2d(x, y);
                if (!plantPositions.containsKey(newGrass)) {
                    return newGrass;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            y = (int) (Math.random() * (0.2 * height + 1) + 0.4 * height);
            x = (int) (Math.random() * width);
            newGrass = new Vector2d(x, y);
            if (!plantPositions.containsKey(newGrass)) {
                return newGrass;
            }
        }
        return null;
    }
}