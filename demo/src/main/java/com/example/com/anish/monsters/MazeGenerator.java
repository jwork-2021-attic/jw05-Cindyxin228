package com.example.com.anish.monsters;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;
import java.util.Vector;

public class MazeGenerator {

    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    public int[][] maze;
    private int dimension;
    public boolean[][] visited;
    public Vector<Node> node; // store the path
    Boolean ifFinished;

    MazeGenerator(int dim) {
        maze = new int[dim][dim];
        dimension = dim;
        visited = new boolean[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                maze[i][j] = 1;
        }
        ifFinished = false;
        node = new Vector<Node>();
    }

    public int generateMaze() {
        // set the path
        int fCnt = 0;
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            int x = dimension / 5 * i;
            int ran1 = 1 + r.nextInt(8);
            int ran2 = 1 + r.nextInt(8);
            int ranY = r.nextInt(dimension - 3);
            for (int j = 0; j < ran1; j++) {
                for (int m = 0; m < ran2; m++) {
                    if (j != 0 || m != 0) {
                        int tpX = x - j, tpY = ranY - m;
                        if (pointOnGrid(tpX, tpY) && tpX * tpY != 0)
                            maze[tpX][tpY] = 0;
                        tpX = x + j;
                        tpY = ranY + m;
                        if (pointOnGrid(tpX, tpY) && tpX * tpY != 0)
                            maze[tpX][tpY] = 0;
                    }

                }
            }
        }
        // set the fruit
        for (int i = 0; i < 100; i++) {
            int fruitX = r.nextInt(dimension), fruitY = r.nextInt(dimension);
            if ((fruitX + fruitY != 0) && validNextNode(new Node(fruitX, fruitY)) && maze[fruitX][fruitY] == 1) {
                maze[fruitX][fruitY] = 2;
                fCnt++;
            }
        }
        return fCnt;
    }

    // public void generateMaze() {
    // stack.push(new Node(0, 0));
    // while (!stack.empty()) {
    // Node next = stack.pop();
    // if (validNextNode(next)) {
    // maze[next.y][next.x] = 1;
    // ArrayList<Node> neighbors = findNeighbors(next);
    // randomlyAddNodesToStack(neighbors);
    // }
    // }
    // }

    public String getRawMaze() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : maze) {
            sb.append(Arrays.toString(row) + "\n");
        }
        return sb.toString();
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // check if the node hasn't been added and it hasn't been surrounded
    private boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int y = node.y - 1; y < node.y + 2; y++) {
            for (int x = node.x - 1; x < node.x + 2; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[x][y] != 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 2) && maze[node.y][node.x] != 0;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y - 1; y < node.y + 2; y++) {
            for (int x = node.x - 1; x < node.x + 2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y) && pointNotNode(node, x, y)) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    // check if x,y is in range
    public Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    // check if the node is of the same row or column as (x,y)
    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    // check if the node is (x,y)
    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }

    // public void dfsMaze(int x, int y, boolean[][] visit) {
    // if (ifFinished == true)
    // return;
    // // 设置结束条件
    // if (x < 0 || y < 0)
    // return;
    // // 如果坐标越界，或者 maze[x][y]==1 表示遇到障碍
    // if (x > dimension - 1 || y > dimension - 1 || maze[x][y] == 0)
    // return;
    // if (visit[x][y] == true)
    // return; // 判断是否通路和越界
    // if (x == dimension - 1 && maze[x][y] == 1) { // 判断是否抵达出口
    // ifFinished = true;
    // node.add(new Node(x, y));
    // return;
    // }

    // Vector<Node> temp = node;
    // node.add(new Node(x, y)); // 记录路线
    // visit[x][y] = true; // 将走过的路标记
    // // 向四个方向搜索
    // dfsMaze(x + 1, y, visit); // 向右搜索
    // dfsMaze(x, y + 1, visit); // 向下搜索
    // dfsMaze(x, y - 1, visit); // 向上搜索
    // dfsMaze(x - 1, y, visit); // 向左搜索
    // // 将路线和标记恢复成上一次的状态
    // visit[x][y] = false;
    // // 清除
    // node = temp;
    // }
}