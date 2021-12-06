package com.example.com.anish.monsters;

import java.awt.Color;

public class Number extends Thing {
    public Number(Color color, World world, int num) {
        super(color, (char) (num + 48), world);
    }
}
