package com.example.com.anish.monsters;

import java.awt.Color;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public Player player;

    private Tile<Thing>[][] tiles;
    MazeGenerator mg;

    public World() {

        player = new Player(Color.RED, this);

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        // set the maze including floor and wall
        mg = new MazeGenerator(WIDTH);
        mg.generateMaze();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
                if (mg.maze[i][j] == 0) {
                    tiles[i][j].setThing(new Wall(this));
                } else if (mg.maze[i][j] == 2) {
                    tiles[i][j].setThing(new Fruit(Color.GREEN, this));
                }
            }
        }

        tiles[0][0].setThing(player);

        // mg.dfsMaze(0, 0, mg.visited);
    }

    public void execute(int dir) {
        // if (COUNT < mg.node.size()) {
        // Node tp = mg.node.elementAt(COUNT);
        // tiles[tp.x][tp.y].setThing(new Monster(new Color(255, 0, 0), this));
        // COUNT++;
        // }
        player.playerMove(dir);
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

}
