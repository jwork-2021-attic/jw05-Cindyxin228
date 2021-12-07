package com.example.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;

// import com.anish.monsters.BubbleSorter;
// import com.anish.monsters.Monster;
import com.example.com.anish.monsters.World;
import com.example.com.anish.monsters.Player;
import com.example.com.anish.monsters.Number;
import com.example.com.anish.monsters.Character;
import com.example.com.anish.monsters.Monster;
// import com.anish.monsters.Matrix;

import com.example.asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    public World world;
    private Thread p;
    private Thread[] m;
    // String[] sortSteps;

    public WorldScreen() {
        world = new World();
        final int size = 16;
        p = new Thread(world.player);
        m = new Thread[6];
        for (int i = 0; i < 3; i++) {
            m[i] = new Thread(new Monster(Color.yellow, world));
            m[i].start();
        }
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
    private void printCharacter(AsciiPanel terminal, String s, int x, int y) {
        for (int i = 0; i < s.length(); i++) {
            Character c = new Character(Color.PINK, world, s.charAt(i) - 'a');
            terminal.write(c.getGlyph(), x + i, y, c.getColor());
        }
    }

    public void judgeSucceed() {
        if (world.monsterCnt + world.player.COUNT == world.fruitCnt)
            world.ifSucceed = true;
        else
            world.ifSucceed = false;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
        int num = world.player.COUNT;
        int a = num / 10, b = num % 10;
        Number aNum = new Number(Color.PINK, world, a), bNum = new Number(Color.PINK, world, b);
        printCharacter(terminal, "player", 32, 20);
        terminal.write(aNum.getGlyph(), 40, 20, aNum.getColor());
        terminal.write(bNum.getGlyph(), 41, 20, bNum.getColor());

        int c = world.monsterCnt / 10, d = world.monsterCnt % 10;
        Number cNum = new Number(Color.PINK, world, c), dNum = new Number(Color.PINK, world, d);
        printCharacter(terminal, "monster", 32, 15);
        terminal.write(cNum.getGlyph(), 40, 15, cNum.getColor());
        terminal.write(dNum.getGlyph(), 41, 15, dNum.getColor());

        if (world.state == false) {
            printCharacter(terminal, "fail", 32, 10);
        }

        judgeSucceed();
        if (world.ifSucceed == true) {
            printCharacter(terminal, "succeed", 32, 10);
        }
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
        world.player.direction = dir;
        world.player.setKey();
        return this;
    }

}
