package com.example.com.anish.monsters;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//To save all information
//score(monster int, player int, fruit)
/*position
    player¡¯s x and y
    monster's number + monsters' x and y
    maze[x][y]¡¯s value(the both above can be accessed by this)
*/

public class Save {
    World world;

    public Save(World _world) throws IOException {
        world = _world;
    }

    // 0 wall, 1 floor, 2 fruit, 3 player, 4 monster
    public void saveRecord() throws IOException {
        FileOutputStream record = null;
        try {
            record = new FileOutputStream("./record.txt");
            record.write(world.playerNum);
            record.write(world.monsterCnt);
            record.write(world.playerCnt);
            record.write(world.fruitCnt);
            for (int i = 0; i < world.WIDTH; i++) {
                for (int j = 0; j < world.HEIGHT; j++) {
                    record.write(world.mg.maze[i][j]);
                }
            }
        } finally {
            record.close();
        }
        // FileInputStream record1 = null;
        // try {
        // record1 = new FileInputStream("./record.txt");
        // int tp = record1.read();
        // while (tp != -1) {
        // System.out.println(tp);
        // tp = record1.read();
        // }
        // } finally {
        // record.close();
        // }

    }
}
