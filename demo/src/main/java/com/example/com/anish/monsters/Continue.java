package com.example.com.anish.monsters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Continue {
    World world;

    public Continue(World _world) {
        world = _world;
    }

    public boolean readRecord() throws IOException {
        boolean ifRecord = false;
        FileInputStream record = null;
        File file = new File("./record.txt");
        if (!file.exists())
            return ifRecord;
        try {
            record = new FileInputStream("./record.txt");
            int playerNum = record.read();
            if (playerNum != world.contiNum)
                return false;
            world.monsterCnt = record.read();
            if (world.monsterCnt != -1) {
                ifRecord = true;
                System.out.println(world.monsterCnt);
                world.playerCnt = record.read();
                System.out.println(world.playerCnt);
                world.fruitCnt = record.read();
                System.out.println(world.fruitCnt);
                for (int i = 0; i < world.WIDTH; i++) {
                    for (int j = 0; j < world.HEIGHT; j++) {
                        int tp = record.read();
                        world.mg.maze[i][j] = tp;
                    }
                }

            }
        } finally {
            record.close();
        }
        return ifRecord;
    }
}
