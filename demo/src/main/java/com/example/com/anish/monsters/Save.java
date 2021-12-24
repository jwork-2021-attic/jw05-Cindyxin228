package com.example.com.anish.monsters;

import java.io.BufferedOutputStream;
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
        saveRecord();
    }

    private void saveRecord() throws IOException {
        FileOutputStream record = new FileOutputStream("./record.txt");
        BufferedOutputStream stream = new BufferedOutputStream(record);
        try {
            stream.write(world.monsterCnt);
            stream.write(world.player.COUNT);
            stream.write(world.fruitCnt);
            for (int i = 0; i < world.WIDTH; i++) {
                for (int j = 0; i < world.HEIGHT; j++) {
                    stream.write(world.mg.maze[i][j]);
                }
            }
        } finally {
            stream.close();
            record.close();
        }
    }
}
