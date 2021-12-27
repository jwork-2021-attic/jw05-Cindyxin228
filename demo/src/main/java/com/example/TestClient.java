package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;

import javax.swing.JFrame;

import com.example.asciiPanel.AsciiFont;
import com.example.asciiPanel.AsciiPanel;

import java.util.*;

import com.example.com.anish.screen.WorldScreen;
import java.util.concurrent.TimeUnit;
import com.example.com.anish.monsters.World;
import com.example.com.anish.monsters.Player;
import com.example.com.anish.monsters.Number;
import com.example.com.anish.monsters.Character;
import com.example.com.anish.monsters.Monster;
import com.example.com.anish.monsters.Save;
import com.example.com.anish.monsters.Wall;
import com.example.com.anish.monsters.Continue;
import com.example.com.anish.monsters.Floor;
import com.example.com.anish.monsters.Fruit;

/**
 * 
 * Test client for NIO server
 *
 */

// world传过来的有
// world.state、ifbegin、ifsucceed、ifFinish
// monsterCnt、playerCnt、fruitCnt
// world.mg.maze
public class TestClient extends JFrame implements KeyListener {

	InetSocketAddress hostAddress;
	SocketChannel client;
	private AsciiPanel terminal;
	int id;
	static int CLIENTNUM = 0;
	private WorldScreen screen;

	public TestClient() throws IOException {
		super();
		hostAddress = new InetSocketAddress("localhost", 9093);
		client = SocketChannel.open(hostAddress);
		id = CLIENTNUM;
		screen = new WorldScreen();
		CLIENTNUM++;
		terminal = new AsciiPanel(50, 30, AsciiFont.TALRYTH_15_15);
		add(terminal);
		pack();
		addKeyListener(this);
		ByteBuffer buffer = ByteBuffer.allocate(74);
		buffer.putInt(id);
		buffer.putInt(0);
		buffer.flip();
		try {
			client.write(buffer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void printCharacter(AsciiPanel terminal, String s, int x, int y) {
		for (int i = 0; i < s.length(); i++) {
			Character c = new Character(Color.PINK, screen.world, s.charAt(i) - 'a');
			terminal.write(c.getGlyph(), x + i, y, c.getColor());
		}
	}

	public void judgeFinish() {
		if (screen.world.monsterCnt + screen.world.playerCnt == screen.world.fruitCnt)
			screen.world.ifFinish = true;
	}

	public void judgeSucceed() {
		// System.out.println(world.monsterCnt);
		// System.out.println(world.player.COUNT);
		// System.out.println(world.fruitCnt);
		if (screen.world.ifFinish) {
			if (screen.world.playerCnt > screen.world.monsterCnt)
				screen.world.ifSucceed = true;
		}
	}

	private void displayBegin(AsciiPanel terminal) {
		for (int x = 0; x < screen.world.WIDTH; x++) {
			for (int y = 0; y < screen.world.HEIGHT; y++) {

				terminal.write(screen.world.get(x, y).getGlyph(), x, y, screen.world.get(x, y).getColor());

			}
		}
		int num = screen.world.playerCnt;
		int a = num / 10, b = num % 10;
		Number aNum = new Number(Color.PINK, screen.world, a), bNum = new Number(Color.PINK, screen.world, b);
		printCharacter(terminal, "player", 32, 20);
		terminal.write(aNum.getGlyph(), 40, 20, aNum.getColor());
		terminal.write(bNum.getGlyph(), 41, 20, bNum.getColor());

		int c = screen.world.monsterCnt / 10, d = screen.world.monsterCnt % 10;
		Number cNum = new Number(Color.PINK, screen.world, c), dNum = new Number(Color.PINK, screen.world, d);
		printCharacter(terminal, "monster", 32, 15);
		terminal.write(cNum.getGlyph(), 40, 15, cNum.getColor());
		terminal.write(dNum.getGlyph(), 41, 15, dNum.getColor());

		if (screen.world.state == false) {
			printCharacter(terminal, "fail", 32, 10);
		}

		judgeFinish();
		judgeSucceed();
		if (screen.world.ifSucceed == true) {
			printCharacter(terminal, "succeed", 32, 10);
		} else if (screen.world.ifFinish)
			printCharacter(terminal, "fail", 32, 10);
	}

	public void displayOutput(AsciiPanel terminal) {
		if (screen.world.ifBegin == 0) {
			printCharacter(terminal, "please", 1, 1);
			printCharacter(terminal, "press", 8, 1);
			printCharacter(terminal, "n", 14, 1);
			printCharacter(terminal, "to", 16, 1);
			printCharacter(terminal, "begin", 19, 1);
			printCharacter(terminal, "a", 25, 1);
			printCharacter(terminal, "new", 27, 1);
			printCharacter(terminal, "game", 31, 1);
			printCharacter(terminal, "or", 36, 1);
			printCharacter(terminal, "c", 39, 1);
			printCharacter(terminal, "to", 41, 1);
			printCharacter(terminal, "continue", 1, 3);
		} else {
			displayBegin(terminal);
		}
	}

	class receiveInfo implements Runnable {

		@Override
		public void run() {
			ByteBuffer buffer = ByteBuffer.allocate(10240);
			int numRead = -1;
			while (true) {
				try {
					numRead = client.read(buffer);
					buffer.flip();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (numRead > 0) {
					if (buffer.getInt() == 0)
						screen.world.state = true;
					else
						screen.world.state = false;
					screen.world.ifBegin = buffer.getInt();
					if (buffer.getInt() == 0)
						screen.world.ifSucceed = false;
					else
						screen.world.ifSucceed = true;
					if (buffer.getInt() == 0)
						screen.world.ifFinish = false;
					else
						screen.world.ifFinish = true;
					screen.world.monsterCnt = buffer.getInt();
					screen.world.playerCnt = buffer.getInt();
					screen.world.fruitCnt = buffer.getInt();
					for (int i = 0; i < screen.world.WIDTH; i++) {
						for (int j = 0; j < screen.world.HEIGHT; j++) {
							screen.world.put(new Floor(screen.world), i, j);
							screen.world.mg.maze[i][j] = buffer.getInt();
							if (screen.world.mg.maze[i][j] == 0) {
								screen.world.put(new Wall(screen.world), i, j);
							} else if (screen.world.mg.maze[i][j] == 2) {
								screen.world.put(new Fruit(Color.GRAY, screen.world), i, j);
							} else if (screen.world.mg.maze[i][j] >= 10) {
								System.out.println(screen.world.players[0].id);
								screen.world.put(screen.world.players[screen.world.mg.maze[i][j] - 10], i, j);
								screen.world.players[screen.world.mg.maze[i][j] - 10].setPosition(i, j);
							} else if (screen.world.mg.maze[i][j] == 4) {
								new Monster(Color.yellow, screen.world, i, j);
							}
						}
					}

					try {
						TimeUnit.MILLISECONDS.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				repaint();
			}
		}

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
		ByteBuffer buffer = ByteBuffer.allocate(74);
		buffer.putInt(id);
		buffer.putInt(2);
		buffer.putInt(e.getKeyCode());
		buffer.flip();
		try {
			client.write(buffer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(e.getKeyCode());
		buffer.clear();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public static void main(String[] args) {

		TestClient testClient = null;
		try {
			testClient = new TestClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		testClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testClient.setVisible(true);

		new Thread(testClient.new receiveInfo()).start();
		while (true) {
			ByteBuffer buffer = ByteBuffer.allocate(74);
			buffer.putInt(testClient.id);
			buffer.putInt(1);
			buffer.flip();
			try {
				testClient.client.write(buffer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// System.out.println(messages[i]);
			buffer.clear();
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
