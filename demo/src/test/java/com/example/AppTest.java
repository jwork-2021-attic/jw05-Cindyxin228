package com.example;

import static org.junit.Assert.assertTrue;
import com.example.com.anish.monsters.World;
import com.example.com.anish.screen.Screen;
import com.example.com.anish.screen.WorldScreen;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        WorldScreen screen = new WorldScreen();
        screen.startThread();
        // down
        int cnt = 0;
        while (++cnt < 3) {
            screen.world.player.direction = 1;
            screen.world.player.setKey();
        }
        assertTrue(true);
    }
}
