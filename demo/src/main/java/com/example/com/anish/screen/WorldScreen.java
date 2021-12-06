package com.example.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;

// import com.anish.monsters.BubbleSorter;
// import com.anish.monsters.Monster;
import com.example.com.anish.monsters.World;
import com.example.com.anish.monsters.Player;
import com.example.com.anish.monsters.Number;
// import com.anish.monsters.Matrix;

import com.example.asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    public World world;
    public Thread p;
    // String[] sortSteps;

    public WorldScreen() {
        world = new World();
        final int size = 16;
        p = new Thread(world.player);
        p.start();
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    // private void execute(Monster[] bros, String step) {
    // String[] couple = step.split("<->");
    // getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros,
    // Integer.parseInt(couple[1])));
    // }

    // private Monster getBroByRank(Monster[] bros, int rank) {
    // for (Monster bro : bros) {
    // if (bro.getRank() == rank) {
    // return bro;
    // }
    // }
    // return null;
    // }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
        int num = world.player.COUNT;
        int a = num / 10, b = num % 10;
        Number aNum = new Number(Color.YELLOW, world, a), bNum = new Number(Color.YELLOW, world, b);
        terminal.write(aNum.getGlyph(), 40, 20, aNum.getColor());
        terminal.write(bNum.getGlyph(), 41, 20, bNum.getColor());
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent e) {
        int dir = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                dir = 0;
                break;
            case KeyEvent.VK_DOWN:
                dir = 1;
                break;
            case KeyEvent.VK_LEFT:
                dir = 2;
                break;
            case KeyEvent.VK_RIGHT:
                dir = 3;
                break;
        }
        world.execute(dir);
        return this;
    }

}
