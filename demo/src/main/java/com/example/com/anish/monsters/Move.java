package com.example.com.anish.monsters;

import java.awt.Color;

public class Move {
    World world;

    Move(World _world) {
        world = _world;
    }

    public synchronized int move(Creature c, int x, int y, int xPos, int yPos, int t) {
        if (world.mg.maze[xPos][yPos] != 0 && world.mg.pointOnGrid(xPos, yPos)) {
            world.put(new Floor(world), x, y);
            world.put(c, xPos, yPos);
            world.mg.maze[x][y] = 1;
            if (world.mg.maze[xPos][yPos] == 2) {
                world.mg.maze[xPos][yPos] = t;
                return 1;
            }
        }
        return 0;
    }

}
