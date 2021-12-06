package com.example.com.anish.monsters;

import java.awt.Color;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.concurrent.*;

public class Monster extends Creature implements Runnable {

    public static final int WIDTH = 30;
    int monsterCnt;
    Move myMove;

    public Monster(Color color, World world) {
        super(color, (char) 2, world);
    }

    @Override
    public void run() {
        while (true) {
            int x = this.getX(), y = this.getY();
            int dir = bfs(new Node(x, y), this.world.mg.maze);
            switch (dir) {
                case 0:
                    myMove.move(this, x, y, x, y - 1, 4);
                    break;
                case 1:
                    myMove.move(this, x, y, x, y + 1, 4);
                    break;
                case 2:
                    myMove.move(this, x, y, x - 1, y, 4);
                    break;
                case 3:
                    myMove.move(this, x, y, x + 1, y, 4);
                    break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private int bfs(Node node, int[][] maze) {
        int i, w;
        int[][] myPath = new int[WIDTH][WIDTH];
        boolean[][] visit = new boolean[WIDTH][WIDTH];
        for (i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                visit[i][j] = false;
            }
        }
        visit[node.x][node.y] = true;
        Queue<Node> Q = new LinkedList<>();
        Q.offer(node);
        int[] up = { 0, 0, -1, 1 };
        int[] down = { -1, 1, 0, 0 };
        boolean ifFir = true;
        while (Q.size() != 0) {
            Node tp = Q.remove();
            for (i = 0; i < 4; i++) {
                int tpX = node.x + up[i], tpY = node.y + down[i];
                if (tpX < 0 || tpY < 0 || tpX >= WIDTH || tpY >= WIDTH)
                    continue;
                if ((!visit[tpX][tpY])) {
                    if (maze[tpX][tpY] != 0) {
                        if (ifFir)
                            myPath[tpX][tpY] = i;
                        else {
                            myPath[tpX][tpY] = myPath[tp.x][tp.y];
                        }
                        visit[tpX][tpY] = true;
                    }
                    if (maze[tpX][tpY] == 2) {
                        return myPath[tpX][tpY];
                    }
                    Q.offer(tp);
                }
            }
            ifFir = false;
        }
        return 0;
    }
}
