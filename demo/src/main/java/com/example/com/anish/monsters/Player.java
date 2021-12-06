package com.example.com.anish.monsters;

import java.awt.Color;

public class Player extends Creature implements Runnable {

    boolean judgeKey = true, recvKey = true;
    int direction;
    public int COUNT = 0;

    public Player(Color color, World world) {
        super(color, (char) 1, world);
    }

    public synchronized void setKey(int dir) {
        judgeKey = !recvKey;
        direction = dir;
    }

    public void playerMove(int dir) {
        Move playMove = new Move(world);
        int x = this.getX(), y = this.getY();
        switch (dir) {
            case 0:
                COUNT += playMove.move(this, x, y, x, y - 1, 3);
                break;
            case 1:
                COUNT += playMove.move(this, x, y, x, y + 1, 3);
                break;
            case 2:
                COUNT += playMove.move(this, x, y, x - 1, y, 3);
                break;
            case 3:
                COUNT += playMove.move(this, x, y, x + 1, y, 3);
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (recvKey != judgeKey) {
                playerMove(direction);
                recvKey = judgeKey;
            }
        }
    }
}
