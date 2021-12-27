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
    private Screen screen;

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
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public class EchoNIOServer {
        private Selector selector;

        private InetSocketAddress listenAddress;
        private final static int PORT = 9093;
        WorldScreen screen;

        private Player[] players;
        private int playerNum;
        private int startNum;
        private int continueNum;

        public EchoNIOServer(String address, int port) throws IOException {
            listenAddress = new InetSocketAddress(address, PORT);
            players = new Player[20];
            playerNum = 0;
            startNum = 0;
            continueNum = 0;
            screen = new WorldScreen();
        }

        /**
         * Start the server
         * 
         * @throws IOException
         */
        private void startServer() throws IOException {
            this.selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            // bind server socket channel to port
            serverChannel.socket().bind(listenAddress);
            serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started on port >> " + PORT);

            while (true) {
                // wait for events
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }

                // process selected keys...
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();

                    // Remove key from set so we don't process it twice
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) { // Accept client connections
                        this.accept(key);
                    } else if (key.isReadable()) { // Read from client
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            }
        }

        // accept client connection
        private void accept(SelectionKey key) throws IOException {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverChannel.accept();
            channel.configureBlocking(false);
            Socket socket = channel.socket();
            playerNum++;
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();

            // System.out.println("Connected to: " + remoteAddr);

            /*
             * Register channel with selector for further IO (record it for read/write
             * operations, here we have used read operation)
             */
            channel.register(this.selector, SelectionKey.OP_READ);
        }

        // read from the socket channel
        private void read(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int numRead = -1;
            numRead = channel.read(buffer);

            if (numRead == -1) {
                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("Connection closed by client: " + remoteAddr);
                channel.close();
                key.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);

            System.out.println("Got: " + new String(data));
        }

        private void write(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Main app = new Main();
        // server另开一个线程
        // try {
        // app.new EchoNIOServer("localhost", 9093).startServer();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        System.out.println("start");
        while (true) {
            app.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
