package com.BugFirstJava.Dao.Record;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Record implements Serializable {
    // 历史战绩类
    // 包含累计击杀防御塔数、最高关卡、最高分数
    public LocalDateTime time;//记录时间，初始化时就要赋值
    public int killCount;
    public int highestLevel;
    public char soldierSyb;//兵种符号
    public  int score;//积分
    public Record(){
        time = LocalDateTime.now();
    }
    @Serial
    private static final long serialVersionUID = 1;
}
