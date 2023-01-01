package agh.ics.oop.grassGenerators;

import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.mapElements.Plant;

import java.util.ArrayList;
import java.util.HashMap;

public class ToxicGrassGenerator implements IGrassGenerator{
    private final int width;
    private final int height;
    private int minDied = 0;
    public ToxicGrassGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Vector2d generateGrass(HashMap<Vector2d, Plant> plantPositions, int[][] deadAnimals, ArrayList<Vector2d> availblePositions, ArrayList<Vector2d> notPrePositions){
        int y;
        int x;
        Vector2d newGrass;
        if (availblePositions.isEmpty()) {
            changeAvailablePositions(deadAnimals, availblePositions, notPrePositions);
        }
        if (Math.random() < 0.2 && !notPrePositions.isEmpty()) {
            for (int i = 0; i < 78; i++) {
                int index = (int) (Math.random() * (notPrePositions.size()));
                y = notPrePositions.get(index).getY();
                x = notPrePositions.get(index).getX();
                newGrass = new Vector2d(x, y);
                if (!plantPositions.containsKey(newGrass)) {
                    return newGrass;
                }
            }
        }
        for (int i = 0; i < 78; i++) {
            int index = (int) (Math.random() * (availblePositions.size()));
            y = availblePositions.get(index).getY();
            x = availblePositions.get(index).getX();
            newGrass = new Vector2d(x, y);
            if (!plantPositions.containsKey(newGrass)) {
                return newGrass;
            }
        }

        return null;
    }

    private void changeAvailablePositions(int[][] deadAnimals, ArrayList<Vector2d> availablePositions, ArrayList<Vector2d> notPrePositions) {
        minDied += 1;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (deadAnimals[i][j] == minDied) {
                    availablePositions.add(new Vector2d(i, j));
                    notPrePositions.remove(new Vector2d(i, j));
                }
            }
        }
    }
}
