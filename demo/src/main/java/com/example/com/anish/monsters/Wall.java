package com.example.com.anish.monsters;

import com.example.asciiPanel.AsciiPanel;

public class Wall extends Thing {

    public Wall(World world) {
        super(AsciiPanel.cyan, (char) 177, world);
    }

}
