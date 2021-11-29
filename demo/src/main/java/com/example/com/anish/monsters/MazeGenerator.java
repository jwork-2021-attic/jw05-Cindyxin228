package com.example.com.anish.monsters;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;
import java.util.Vector;

class MazeGenerator {

    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    public int[][] maze;
    private int dimension;
    public boolean[][] visited;
    public Vector<Node> node;
    Boolean ifFinished;

    MazeGenerator(int dim) {
        maze = new int[dim][dim];
        dimension = dim;
        visited = new boolean[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                visited[i][j] = false;
        }
        ifFinished = false;
        node = new Vector<Node>();
    }

    public void generateMaze() {
        stack.push(new Node(0, 0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (validNextNode(next)) {
                maze[next.y][next.x] = 1;
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
            }
        }
    }

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
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[y][x] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[node.y][node.x] != 1;
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
    private Boolean pointOnGrid(int x, int y) {
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

    public void dfsMaze(int x, int y, boolean[][] visit) {
        if (ifFinished == true)
            return;
        // ���ý�������
        if (x < 0 || y < 0)
            return;
        // �������Խ�磬���� maze[x][y]==1 ��ʾ�����ϰ�
        if (x > dimension - 1 || y > dimension - 1 || maze[x][y] == 0)
            return;
        if (visit[x][y] == true)
            return; // �ж��Ƿ�ͨ·��Խ��
        if (x == dimension - 1 && maze[x][y] == 1) { // �ж��Ƿ�ִ����
            ifFinished = true;
            node.add(new Node(x, y));
            return;
        }

        Vector<Node> temp = node;
        node.add(new Node(x, y)); // ��¼·��
        visit[x][y] = true; // ���߹���·���
        // ���ĸ���������
        dfsMaze(x + 1, y, visit); // ��������
        dfsMaze(x, y + 1, visit); // ��������
        dfsMaze(x, y - 1, visit); // ��������
        dfsMaze(x - 1, y, visit); // ��������
        // ��·�ߺͱ�ǻָ�����һ�ε�״̬
        visit[x][y] = false;
        // ���
        node = temp;
    }
}