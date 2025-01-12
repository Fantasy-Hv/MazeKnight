package com.BugFirstJava.Dao.Record;

import com.BugFirstJava.Dao.Entity.Player;
import com.BugFirstJava.Dao.Level.Level;

import java.io.Serial;
import java.io.Serializable;

public class Save implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    public Level level;
    public Player player;

    public Save(Level level, Player player) {
        this.level = level;
        this.player = player;
    }
}
