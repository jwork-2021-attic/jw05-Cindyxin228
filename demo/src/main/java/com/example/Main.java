package com.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.example.asciiPanel.AsciiFont;
import com.example.asciiPanel.AsciiPanel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.example.com.anish.monsters.Player;
import com.example.com.anish.monsters.World;
import com.example.com.anish.screen.Screen;
import com.example.com.anish.screen.WorldScreen;

public class Main extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private WorldScreen screen;

    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH + 20, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new WorldScreen();
        addKeyListener(this);

    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = (WorldScreen) screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public class EchoServer implements Runnable {
        Selector selector;
        ServerSocketChannel serverChannel;
        int playerNumber;

        public EchoServer() throws IOException {
            playerNumber = 0;
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
            serverChannel.bind(hostAddress);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        @Override
        public void run() {
            while (true) {
                int readyCount = -1;
                try {
                    readyCount = selector.select();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (readyCount == 0) {
                    continue;
                }

                Set<SelectionKey> readyKeys = selector.selectedKeys();
                java.util.Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client;
                        try {
                            client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        int BUFFER_SIZE = 1024;
                        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                        try {
                            client.read(buffer);
                            ((Buffer) buffer).flip();
                            // request:
                            // id, 0(accept)/1(send info)/2(key press)
                            int id = buffer.getInt();
                            System.out.println(id);
                            int type = buffer.getInt();
                            if (type == 0) {
                                if (id >= screen.world.playerNum)
                                    screen.world.playerNum = id + 1;
                            } else if (type == 2) {
                                int keyPressed = buffer.getInt();
                                screen.switchKey(keyPressed);
                            } else if (type == 1) {
                                ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE * 10);
                                if (!screen.world.state)
                                    buf.putInt(0);
                                else
                                    buf.putInt(1);
                                buf.putInt(screen.world.ifBegin);
                                if (!screen.world.ifSucceed)
                                    buf.putInt(0);
                                else
                                    buf.putInt(1);
                                if (!screen.world.ifFinish)
                                    buf.putInt(0);
                                else
                                    buf.putInt(1);
                                buf.putInt(screen.world.monsterCnt);
                                buf.putInt(screen.world.playerCnt);
                                buf.putInt(screen.world.fruitCnt);

                                for (int i = 0; i < screen.world.WIDTH; i++) {
                                    for (int j = 0; j < screen.world.HEIGHT; j++) {
                                        buf.putInt(screen.world.mg.maze[i][j]);
                                    }
                                }
                                ((Buffer) buf).flip();
                                client.write(buf);
                            }
                        } catch (Exception e) {
                            // e.printStackTrace();
                            continue;
                        }
                    }
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Main app = new Main();
        try {
            new Thread(app.new EchoServer()).start();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // app.setVisible(true);
        System.out.println("start");
        // while (true) {
        // app.repaint();
        // try {
        // TimeUnit.MILLISECONDS.sleep(500);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
    }

}
