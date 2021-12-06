package com.example.com.anish.monsters;

import java.awt.Color;

public class Character extends Thing {
    public Character(Color color, World world, int num) {
        super(color, (char) (num + 97), world);
    }
}
