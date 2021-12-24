package com.example.com.anish.monsters;

import java.awt.Color;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.concurrent.*;

import javax.lang.model.util.ElementScanner6;

public class Monster extends Creature implements Runnable {

    public static final int WIDTH = 30;

    public Monster(Color color, World world) {
        super(color, (char) 2, world);
        Random r = new Random();
        int X = r.nextInt(30), Y = r.nextInt(30);
        boolean flag = true;
        int cnt = 0;
        while (flag && cnt <= 10) {
            if (world.mg.maze[X][Y] == 1 && (X != 0 || Y != 0)) {
                flag = false;
                world.monsterNum++;
                world.mg.maze[X][Y] = 4;
                world.put(this, X, Y);
                this.setPosition(X, Y);
            }
            cnt++;
        }
    }

    public Monster(Color color, World world, int x, int y) {
        super(color, (char) 2, world);
        world.mg.maze[x][y] = 4;
        world.put(this, x, y);
        this.setPosition(x, y);
    }

    @Override
    public void run() {
        while (this.world.state && !this.world.ifFinish) {
            int x = this.getX(), y = this.getY();
            int dir = bfs(new Node(x, y), this.world.mg.maze);
            // world.monsterCnt +=
            world.allMove.move(this, x, y, dir, 4);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
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
                // System.out.println(tp.x + "tp.x");
                // System.out.println(tp.y + "tp.y");
                // System.out.println(up[i] + ": up[" + i);
                // System.out.println(down[i] + ": down[" + i);
                int tpX = tp.x + up[i], tpY = tp.y + down[i];

                // System.out.println(tpX);
                // System.out.println(tpY);
                if (tpX < 0 || tpY < 0 || tpX >= WIDTH || tpY >= WIDTH || maze[tpX][tpY] == 0) {
                    continue;
                }
                if ((!visit[tpX][tpY])) {
                    Q.offer(new Node(tpX, tpY));
                    if (maze[tpX][tpY] != 0) {
                        if (ifFir)
                            myPath[tpX][tpY] = i;
                        else {
                            myPath[tpX][tpY] = myPath[tp.x][tp.y];
                        }
                        visit[tpX][tpY] = true;
                    }
                }
                if (maze[tpX][tpY] == 2) {
                    int dir = myPath[tpX][tpY];
                    switch (dir) {
                        case 0:
                            if (node.y - 1 < 0 || maze[node.x][node.y - 1] == 0) {
                                myPath[tpX][tpY] = 1;
                                continue;
                            } else
                                return dir;
                        case 1:
                            if (node.y + 1 >= WIDTH || maze[node.x][node.y + 1] == 0) {
                                myPath[tpX][tpY] = 2;
                                continue;
                            } else
                                return dir;
                        case 2:
                            if (node.x - 1 < 0 || maze[node.x - 1][node.y] == 0) {
                                myPath[tpX][tpY] = 3;
                                continue;
                            } else
                                return dir;
                        case 3:
                            if (node.x + 1 >= WIDTH || maze[node.x + 1][node.y] == 0) {
                                myPath[tpX][tpY] = 0;
                                continue;
                            } else
                                return dir;
                    }
                }
            }
            ifFir = false;
        }
        return 0;
    }
}
