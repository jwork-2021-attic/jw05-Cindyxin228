package com.example.com.anish.monsters;

import java.awt.Color;

public class World {

    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private static int COUNT = 0;

    private Tile<Thing>[][] tiles;
    MazeGenerator mg;

    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        mg = new MazeGenerator(WIDTH);
        mg.generateMaze();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
                if (mg.maze[i][j] == 0) {
                    tiles[i][j].setThing(new Wall(this));
                }
            }
        }
        mg.dfsMaze(0, 0, mg.visited);
    }

    public void execute() {
        if (COUNT < mg.node.size()) {
            Node tp = mg.node.elementAt(COUNT);
            tiles[tp.x][tp.y].setThing(new Monster(new Color(255, 0, 0), this));
            COUNT++;
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    // public void putMatrix(Matrix matrix, int size) {
    // int rowStart = (WIDTH - size) / 2 - 1;
    // int colStart = (HEIGHT - size) / 2 - 1;
    // for (int i = 0; i < size; i++) {
    // for (int j = 0; j < size; j++) {
    // put(matrix.monsters[i][j], rowStart + i, colStart + j);
    // }
    // }
    // }

}
