package com.example.battleship.Enum;

public enum GameStatus {
    PLACEMENT_START("Placement Start"),
    START_GAME("Start Game"),
    FINISH_GUEST("Finish guest"),
    FINISH_HOST("Finish host"),
    WAITING_SECOND_PLAYER("Waiting second player"),
    GUEST("guest"),
    HOST("host");

    private final String name;

    GameStatus(String status) {
        this.name = status;
    }

    public String getName() {
        return name;
    }
}
