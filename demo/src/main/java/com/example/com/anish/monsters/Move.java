package com.example.com.anish.monsters;

import java.awt.Color;

import javax.swing.border.BevelBorder;

public class Move {
    World world;

    Move(World _world) {
        world = _world;
    }

    private boolean judge(int xPos, int yPos, int t) {
        if (world.mg.pointOnGrid(xPos, yPos) && world.mg.maze[xPos][yPos] != 0 && world.mg.maze[xPos][yPos] != t)
            return true;
        return false;
    }

    public synchronized int move(Creature c, int x, int y, int dir, int t) {
        int xPos = x, yPos = y;
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
        if (judge(xPos, yPos, t)) {
            c.setPosition(xPos, yPos);
            world.put(new Floor(world), x, y);
            world.put(c, xPos, yPos);
            if (t * world.mg.maze[xPos][yPos] == 12) {
                world.state = false;
                return 0;
            }
            int ttp = world.mg.maze[xPos][yPos];
            world.mg.maze[x][y] = 1;
            world.mg.maze[xPos][yPos] = t;
            if (ttp == 2) {
                return 1;
            }
        } else if (t == 4) {
            if (judge(x, y - 1, t))
                move(c, x, y, 0, t);
            else if (judge(x - 1, y, t))
                move(c, x, y, 2, t);
            else if (judge(x, y + 1, t))
                move(c, x, y, 1, t);
            else if (judge(x + 1, y, t))
                move(c, x, y, 3, t);
        }
        return 0;
    }

}
