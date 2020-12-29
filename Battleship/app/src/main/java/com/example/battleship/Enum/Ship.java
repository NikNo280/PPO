package com.example.battleship.Enum;

public enum Ship {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int size;

    Ship(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
