package com.example.com.anish.monsters;

import java.awt.Color;

public class Move {
    World world;
    boolean flag;

    Move(World _world) {
        world = _world;
        flag = true;
    }

    public synchronized int move(Creature c, int x, int y, int dir, int t) {
        int xPos = x, yPos = y;
        flag = !flag;
        switch (dir) {
            case 0:
                yPos = y - 1;
                break;
            case 1:
                yPos = y + 1;
                break;
            case 2:
                xPos = x - 1;
                break;
            case 3:
                xPos = x + 1;
                break;
        }
        if (world.mg.pointOnGrid(xPos, yPos) && world.mg.maze[xPos][yPos] != 0 && world.mg.maze[xPos][yPos] != t) {
            c.setPosition(xPos, yPos);
            world.put(new Floor(world), x, y);
            world.put(c, xPos, yPos);
            if (t * world.mg.maze[xPos][yPos] == 12) {
                world.state = false;
                return 0;
            }
            world.mg.maze[x][y] = 1;
            if (world.mg.maze[xPos][yPos] == 2) {
                world.mg.maze[xPos][yPos] = t;
                return 1;
            }
        } else if (t == 4) {
            int tp = flag ? 1 : 2;
            move(c, x, y, (dir + tp) % 4, t);
        }
        return 0;
    }

}
