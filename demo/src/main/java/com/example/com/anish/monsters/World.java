package com.example.com.anish.monsters;

import java.awt.Color;
import java.util.Random;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public Player[] players;
    public Monster[] monsters;
    public boolean state;
    public boolean ifSucceed;
    public boolean ifFinish;
    public int monsterCnt;
    public int monsterNum;
    public int fruitCnt;
    public Move allMove;
    public int ifBegin;
    public int playerNum;
    public int playerCnt;

    private Tile<Thing>[][] tiles;
    MazeGenerator mg;

    public World() {

        allMove = new Move(this);
        players = new Player[20];
        for (int i = 0; i < 20; i++) {
            players[i] = new Player(Color.RED, this);
            players[i].id += i;
        }
        state = true;
        monsterNum = 0;
        ifSucceed = false;
        ifFinish = false;
        monsterCnt = 0;
        playerCnt = 0;
        ifBegin = 0;
        playerNum = 0;
        mg = new MazeGenerator(WIDTH);

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        // mg.dfsMaze(0, 0, mg.visited);
    }

    public void newPlayer(Player _player) {
        Random r = new Random();
        int i = r.nextInt(30);
        int j = r.nextInt(30);
        boolean found = false;
        while (!found) {
            if (mg.maze[i][j] == 1) {
                found = true;
                tiles[i][j].setThing(_player);
                _player.setPosition(i, j);
                mg.maze[i][j] = _player.id;
            }
        }
    }

    public void newGame(int _playerNum) {
        // set the maze including floor and wall

        fruitCnt = mg.generateMaze();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
                if (mg.maze[i][j] == 0) {
                    tiles[i][j].setThing(new Wall(this));
                } else if (mg.maze[i][j] == 2) {
                    tiles[i][j].setThing(new Fruit(Color.GRAY, this));
                }
            }
        }

        tiles[0][0].setThing(players[0]);
        players[0].setPosition(0, 0);
        mg.maze[0][0] = 10;

        playerNum = _playerNum;
        for (int i = 1; i < playerNum; i++)
            newPlayer(players[i]);

        monsters = new Monster[10];
    }

    public void continueGame() {
        monsters = new Monster[10];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
                if (mg.maze[i][j] == 0) {
                    tiles[i][j].setThing(new Wall(this));
                } else if (mg.maze[i][j] == 2) {
                    tiles[i][j].setThing(new Fruit(Color.GRAY, this));
                } else if (mg.maze[i][j] >= 10) {
                    tiles[i][j].setThing(players[mg.maze[i][j] - 10]);
                    players[mg.maze[i][j] - 10].setPosition(i, j);
                } else if (mg.maze[i][j] == 4) {
                    monsters[monsterNum] = new Monster(Color.yellow, this, i, j);
                    monsterNum++;
                }
            }
        }
    }

    public void execute(int dir, Player _player) {
        // if (COUNT < mg.node.size()) {
        // Node tp = mg.node.elementAt(COUNT);
        // tiles[tp.x][tp.y].setThing(new Monster(new Color(255, 0, 0), this));
        // COUNT++;
        // }
        _player.setKey();
    }

    public synchronized int getMazeInfo(int x, int y) {
        return mg.maze[x][y];
    }

    public synchronized void setMazeInfo(int x, int y, int num) {
        mg.maze[x][y] = num;
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public synchronized void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

}
