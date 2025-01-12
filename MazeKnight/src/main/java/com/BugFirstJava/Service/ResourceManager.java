package com.BugFirstJava.Service;

import com.BugFirstJava.Dao.Entity.Player;
import com.BugFirstJava.Dao.Level.Level;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.BugFirstJava.Dao.Entity.Tower;
import com.BugFirstJava.Dao.Position.Position;
import com.BugFirstJava.Dao.Record.Record;
import com.BugFirstJava.Dao.Record.Save;

import static com.BugFirstJava.Service.InitRecordFile.*;

public class ResourceManager {
    //主要进行I/O操作，负责管理历史记录、加载地图数据，以及音频相关操作
//    static File RecordPath = new File("src\\...\\Record.re");
//    static File unitRecordFile = new File("src/main/resources/Records/unitRecord.re");
//    static File soldierRecordFile = new File("src/main/resources/Records/soldierRecord.re");
//    static File playerRecordFile = new File("src/main/resources/Records/playerRecord.re");
    static AudioManager manuBgm ;
    static AudioManager gameBgm ;
    static AudioManager hitSound ;
    static AudioManager deadSound ;
    static AudioManager coins;
    static AudioManager potion ;
    static AudioManager winSound ;
    /*
    编号：13
    难度：中等
    负责人：仁雁
     功能：获取关卡对象
          地图文件命名格式：“level_编号.txt”,内容为 字符方阵，只含有以下字符：
          '.' '&' '#' 'S' ']''?''$' ' '，其中'S'表示起点
          new一个Level对象，根据关卡编号，从Maze文件夹中找到对应编号的地图文件
            使用字符流读取地图到Level的二维数组maze中，
            同时记录起点和出口位置，创建防御塔对象存储到towers字典中
            完成后返回该Level对象
     参数：关卡编号
     返回值：关卡对象
     */
  static Level getLevelFromFile(int level) {
      Random random = new Random(System.currentTimeMillis());
    String fileName = "level_" + level + ".txt";
    InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream("Mazes/"+fileName);
    if (inputStream == null) {
        System.out.println("加载不到文件惹TAT");
        System.out.println("未能找到关卡 " + level + " 的文件");
        return null;
    }
    BufferedReader Prereader = new BufferedReader(new InputStreamReader(inputStream));
    try {
        // 第一次遍历文件，确定行数和最大列数
        int rowCount = 0;
        int maxColCount = 0;
        String line;
        while ((line = Prereader.readLine()) != null) {
            if (!line.isEmpty()) { // 忽略空白行
                maxColCount = Math.max(maxColCount, line.length());
                rowCount++;
            }
        }
        Prereader.close();
        // 重新打开文件进行第二次遍历，读取地图数据
        inputStream = ResourceManager.class.getClassLoader().getResourceAsStream("Mazes/"+fileName);
        if (inputStream == null) {
            System.out.println("第二次加载文件失败TAT");
//            System.out.println("未能找到关卡 " + level + " 的文件");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int[][] maze = new int[rowCount][maxColCount];
        // 初始化
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < maxColCount; j++) {
                maze[i][j] = -5;
            }
        }
        //给迷宫规格赋值
        HashMap<Position, Tower> towers = new HashMap<>();
        Position entrance = null;
        Position exit = null;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    switch (ch) {
                        case '.':
                            maze[row][col] = -3; // 空地
                            break;
                        case '#':
                            maze[row][col] = -1; // 城墙
                            break;
                        case '&':
                            maze[row][col] = -2; // 防御塔
                            Tower tower = new Tower(row, col);
                           towerDesign(tower,level);
                            towers.put(Position.getPos(row, col), tower);
                            break;
                        case 'S':
                            maze[row][col] = 0; // 起点
                            entrance = Position.getPos(row, col);
                            break;
                        case ']':
                            maze[row][col] = -4; // 出口
                            exit = Position.getPos(row, col);
                            break;
                        case ' ':
                            maze[row][col] = -5; // 空白
                            break;
                        case '?':
                            maze[row][col] = -6; // 金币
                            break;
                        case '$':
                            maze[row][col] = -7; // 药水
                            break;
                        default:
                            throw new Exception("未知字符: " + ch);
                    }
                }
                row++;
            }
        }

        // 检查是否缺少起点或出口
        if (entrance == null || exit == null) {
            throw new IllegalStateException("地图缺少起点或出口");
        }
        Game.setVisibleAround(maze, entrance);
        // 返回关卡对象
        return new Level(level, maze, entrance, exit, towers);

    } catch (Exception e) {
        System.err.println("未能找到关卡 " + level + " 的文件: " + e.getMessage());
    } finally {
        try {
            Prereader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 如果发生错误，返回null
    return null;
}

  static Level getLevel(int level,boolean isFromFile){
      if (isFromFile) return getLevelFromFile(level);
      else return getLevelFromGenerator(level);
  }
  static Level getLevelFromGenerator(int level){
      char[][] map = MazeGenerator.generate(level);
      HashMap<Position, Tower> towers = new HashMap<>();
      Position entrance = null;
      Position exit = null;
      int[][] maze = new int[map.length][map[0].length];
      for (int row = 0; row < map.length; row++) {
          for (int col = 0; col < map[row].length; col++) {
              switch (map[row][col]) {
                  case '.':
                      maze[row][col] = -3; // 空地
                      break;
                  case '#':
                      maze[row][col] = -1; // 城墙
                      break;
                  case '&':
                      maze[row][col] = -2; // 防御塔
                      Tower tower = new Tower(row, col);
                      towerDesign(tower,level);
                      towers.put(Position.getPos(row, col), tower);
                      break;
                    case 'S':
                        maze[row][col] = 0; // 起点
                        entrance = Position.getPos(row, col);
                        break;
                    case ']':
                        maze[row][col] = -4; // 出口
                        exit = Position.getPos(row, col);
                        break;
                    case ' ':
                        maze[row][col] = -5; // 空白
                        break;
                    case '?':
                        maze[row][col] = -6; // 金币
                        break;
                    case '$':
                        maze[row][col] = -7; // 药水
                        break;
                    default:
                        break;
                    }
          }
      }
      if (entrance == null||exit == null) {
          new RuntimeException("找不到起点和出口 TAT……重启吧！").printStackTrace();
          try {
              Thread.sleep(1500);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          return null;
      }
      Game.setVisibleAround(maze, entrance);
      return new Level(level, maze, entrance, exit, towers);
  }

  public static void towerDesign(Tower tower,int level){
      Random random = new Random(System.currentTimeMillis());
      tower.health*=1+0.1*level;
      tower.attackStrength*=1+0.07*level;
      tower.defence*=1+0.09*level;
      int randomNum = random.nextInt(100);
      if (randomNum < 30)
          tower.health *= 1+0.15*level;
      else if (randomNum < 65)
          tower.attackStrength *= 1+0.15*level;
      else if (randomNum > 70)
          tower.defence *= 1+0.15*level;

  }
//-----------------------------存档记录-------------------------------------------------------------
    /*
      编号：14
      难度：简单
      负责人：拓展，待定
       功能：获取玩家的历史记录
       参数：记录类型：1每次游戏记录  2角色历史最高记录  3玩家历史最高记录
       返回值：Record对象数组/若无记录，返回null
       描述：用对象输入流读取Record.re文件中的Record对象
     */
     public static Record[] getRecord(int type) {
        File useFile;
        if (!InitRecordFile.verifyRecords())
            InitRecordFile.init();
        switch (type) {
            case 1 -> useFile = unitRecordFile;
            case 2 -> useFile = soldierRecordFile;
            case 3 -> useFile = playerRecordFile;
            default -> {
                System.err.println("无效的记录类型: " + type);
                return null; // 返回null表示无效的记录类型
            }
        }
            ArrayList<Record> records = new ArrayList<>();
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(useFile));
                records = (ArrayList<Record>) in.readObject();
                    in.close();
                } catch (ClassNotFoundException|IOException e) {}
            if (records.isEmpty()) return null;
            if (type == 1) records.sort((r1, r2) -> {
                if (r1.time.isEqual(r2.time))return 0;
                else return r1.time.isBefore(r2.time) ? 1 : -1;});
            return records.toArray(new Record[0]);
    }

    /*
       编号：15
       难度：简单
       负责人：拓展，待定
       功能：更新玩家的历史记录到.re文件中
       参数：游戏状态：-1玩家退出 0 玩家死亡 1玩家胜利
       返回值：无
       描述： 调用getRecord()方法获取所有历史Record对象
             调用Game.getRecord()获取本次游戏的Record对象并更新到历史Record对象中
            用新的Record对象覆盖Record.re文件
     */
    public static  boolean updateRecord(int state) {
        ArrayList<Record> records ;
        //----------------更新player的record-----------------------------
        Player player = Game.getPlayer();
        player.record.highestLevel = Game.level.id;
        if (state == 1) player.record.score += 100;

        //------------------追加每次游戏记录----------------------------
        if (state < 1) {
            Record[] rs = getRecord(1);
            if (rs!=null)
             records = new ArrayList<>(Arrays.asList(rs));
            else records = new ArrayList<>();
            records.add(player.record);
            try{
                 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(unitRecordFile,false));
               out.writeObject(records);
                 out.close();
            }catch (IOException e){
               System.err.println("保存每次游戏记录出错");
            }
        }
        //----------------更新角色历史最高记录---------------------------------
        Record[] recordArray = getRecord(2);
        if (recordArray!=null) records = new ArrayList<>(Arrays.asList(recordArray));
        else records = new ArrayList<>();
        Record newRecord = Game.getRecord();
            boolean hasRecord = false;
            if (!records.isEmpty()) {
                for (Record record : records) {
                    if (record.soldierSyb == newRecord.soldierSyb) {
                        record.time = newRecord.time;
                        record.highestLevel = Math.max(record.highestLevel, newRecord.highestLevel);
                        record.killCount += newRecord.killCount;
                        record.score = Math.max(record.score, newRecord.score);
                        hasRecord = true;
                        break;
                    }
                }
            }
            if (!hasRecord)
                records.add(player.record);
        try {
            ObjectOutputStream soldierWrite = new ObjectOutputStream(new FileOutputStream(soldierRecordFile, false));
            soldierWrite.writeObject(records);
            soldierWrite.close();
        } catch (IOException e) {
            System.err.println("updateRecord(): 角色记录保存出错");
            return false; // 返回false表示更新失败
        }

        //----------------更新玩家历史记录--------------------------
        records.clear();
        Record[] olds = getRecord(3);
        if (olds != null && olds.length > 0) {
            Record old = olds[0];
            old.time = player.record.time;
            old.score = Math.max(old.score, player.record.score);
            old.highestLevel = Math.max(old.highestLevel, player.record.highestLevel);
            old.killCount += player.record.killCount;
            records.addAll(Arrays.asList(olds));
        } else records.add(player.record);
        try {
            ObjectOutputStream playerWrite = new ObjectOutputStream(new FileOutputStream(playerRecordFile, false));
            playerWrite.writeObject(records);
            playerWrite.close();
        } catch (IOException e) {
            System.err.println("updateRecord(): 玩家记录保存出错");
            return false; // 返回false表示更新失败
        }
        //-----------------------------------------------------
        Save save = new Save(Game.level,player);
        ArrayList<Save> saves = new ArrayList<>();
        saves.add(save);
        try {
            ObjectOutputStream saveWrite = new ObjectOutputStream(new FileOutputStream(InitRecordFile.gameSaveFile, false));
            saveWrite.writeObject(saves);
            saveWrite.close();
        } catch (IOException e) {
            System.err.println("updateRecord(): 存档保存出错");
            return false; // 返回false表示更新失败
        }
        return true;
    }
    public static Save  getSave(){
        try {
            if (!gameSaveFile.exists())
                InitRecordFile.initSave();
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(InitRecordFile.gameSaveFile));
            ArrayList<Save> saves = (ArrayList<Save>) in.readObject();
            in.close();
            if (saves.isEmpty()) return null;
            return saves.get(0);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("getSave(): 存档读取出错,请重启");
            InitRecordFile.initSave();
            throw new RuntimeException(e);
        }
    }

//------------------------控制音频播放-----------------------------------------------------
    public static void playGameBgm(){
        if(gameBgm == null) gameBgm = new AudioManager("Sounds/BGM01.wav");
        gameBgm.play(true);}
    public static boolean setGameVolume(int v){
        if(gameBgm == null) gameBgm = new AudioManager("Sounds/BGM01.wav");
        return gameBgm.setVolume(v);}
    public static void stopGameBgm(){gameBgm.stopPlay();}
    public static void playMenuBgm(){
        if(manuBgm == null) manuBgm = new AudioManager("Sounds/BGM02.wav");
        manuBgm.play(true);
    }
    public static void stopMenuBgm(){
        manuBgm.stopPlay();
    }
    public static boolean setMenuVolume(int volume){return manuBgm.setVolume(volume);}
    public static void playHitSound(){
        if(hitSound == null) hitSound = new AudioManager("Sounds/hit.wav");
        hitSound.play(false);}
    public static void playDeadSound(){
        if(deadSound == null) deadSound = new AudioManager("Sounds/ganmaDj.wav");
        deadSound.play(false);}
    public static void playPotionSound(){
        if(potion == null) potion = new AudioManager("Sounds/huifu.wav");
        potion.stopPlay();
        potion.play(false);}
    public static void playCoinSound(){
        if(coins == null) coins = new AudioManager("Sounds/jinbi.wav");
        coins.stopPlay();
        coins.play(false);}
    public static void playWinSound(){
        if(winSound == null) winSound = new AudioManager("Sounds/shengli.wav");
        winSound.play(false);}
    public static void stopDeadSound(){
        if(deadSound != null) deadSound.stopPlay();
    }
   public static boolean setEffectVolume(int volume){
        boolean res = true;
        if(hitSound == null) hitSound = new AudioManager("Sounds/hit.wav");
        if(deadSound == null) deadSound = new AudioManager("Sounds/ganmaDj.wav");
        if(coins == null) coins = new AudioManager("Sounds/jinbi.wav");
        if(winSound == null) winSound = new AudioManager("Sounds/shengli.wav");
        if(hitSound != null)res =hitSound.setVolume(volume);
        if(deadSound!=null) res = deadSound.setVolume(volume);
        if(potion!=null) res = potion.setVolume(volume);
        if(coins!=null) res = coins.setVolume(volume);
        if(winSound!=null)res = winSound.setVolume(volume);
        return res;
    }
public static void stopPotionSound() {
    if (potion != null) potion.stopPlay();
}
}







