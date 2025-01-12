package com.BugFirstJava.View;

import com.BugFirstJava.Dao.Entity.Player;
import com.BugFirstJava.Dao.Items.Potions;
import com.BugFirstJava.Dao.Level.Level;
import com.BugFirstJava.Service.Game;
import com.BugFirstJava.Service.ResourceManager;

import java.util.List;
import java.util.Scanner;

public class Control {
    static Scanner sc = new Scanner(System.in);
    // 程序全局流程控制类，程序的入口
    /*
       编号：11
       难度：简单
       负责人：刘昊
       功能： 程序入口
       参数：无
       返回值:无
       实现思路：
            调用ResourceManager.playBGM(),播放BGM
            while(true):
                 调用Show.menuView()，显示菜单界面
                 调用Input.getMenuChoice()，获取用户选择
                    若选择开始游戏：
                        调用Game类的gameFlow()，开始游戏
                    若选择进入设置
                        【敬请期待】
                    若选择历史记录
                        调用Show.recordView()
                    若选择退出游戏：
                        打印“谢谢使用”
                        退出循环,程序结束
     */
    public static void start() {
        // 播放菜单音乐
        ResourceManager.playMenuBgm();
        while(true){
            Show.MenuView();
            switch(Input.getMenuChoice()){
                case 1->{
                    ResourceManager.stopMenuBgm();
                    gameFlow(false);
                    ResourceManager.playMenuBgm();
                }
                case 2->{
                     ResourceManager.stopMenuBgm();
                    gameFlow(true);
                    ResourceManager.playMenuBgm();
                }
                case 3->{
                    volumeSetting();
                }
                case 4->history();
                case 5->{
                    ResourceManager.stopMenuBgm();
                    System.out.println("感谢使用");
                    System.exit(0);
                }
            }
        }
    }

     /*
    编号：12
    难度：中等
    负责人:黄严
    功能: 控制游戏流程
    参数: 无
    返回值: 无
       1.初始化，
       调用Input.getSoldierChoice()并
       将返回值传给Game.initGame()，关卡编号传1；
       2.while(true){
          调用Show.MazeView()打印迷宫场景
          调用Input.getCommand()获取玩家输入的指令(已保证合法)
          直接判断玩家输入的指令类型：
             若为退出指令'Q'：
                    SourceManager.updateRecord(-1)更新并存储历史战绩
                     退出循环；
             若为商城指令'M':
                       调用trade()控制购物流程
             否则即为移动指令：
                调用Game.tarPosType()方法检查目标位置：
                   返回1（墙）：不处理
                   返回3（空地或出口）：调用Game.move()方法
                   返回2（防御塔）：
                         调用Game.roundOne()，根据返回值
                         打印伤害信息；
                         若塔死亡：
                            打印塔死亡信息；
                         否则：
                            调用Game.roundTwo()，
                            根据返回值打印伤害信息；
                            若玩家死亡：
                              调用Show.loseView()打印游戏失败场景
                              调用SourceManager.updateRecord(0)更新并存储历史战绩
                              退出循环；
                    返回6（金币）：
                        Game.pick();
                        Show.MazeView();
                        sout(捡到30软妹币)
                         continue;
                    返回7（药水）：
                         Game.consume(null);//返回药水的属性增益数组
                         Show.MazeView();
                         sout(捡到药水，xx属性加xx)
                         continue;
           }
          调用Game.isWin()判断是否胜利：
             若胜利:
                调用Show.winView()来实现打印胜利信息
                调用Game.nextLevel()加载下一关卡
                调用SourceManager.updateRecord(1)更新并存储历史战绩
             否则:不处理
        }
     */
    public static void gameFlow(boolean load){
            // 1. 初始化
        ResourceManager.playGameBgm();
        boolean fromFile ;
        if (!load){
            fromFile = true;
            Show.SoldiersView();
            char soldierChoice = Input.getSoldierChoice();
            Game.initGame(1,soldierChoice,load);
        }
        else {
            boolean hasSave =Game.initGame(1,'W',load);
            int id = Game.getRecord().highestLevel;
            fromFile = id <= 7;
            if (!hasSave) {
                System.out.println("尚无存档，已为您开启新游戏");
            }
            else System.out.println("正在读档...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 2. 游戏主循环
        // 打印迷宫场景
        Show.MazeView();
        Show.playerView();
        int flag = 0;
        while (true) {
            // 获取玩家输入的指令
            String command = Input.getCommand();
            // 处理玩家输入的指令
            if (command.equals("Q")) {
                ResourceManager.stopGameBgm();
                // 退出指令
                boolean saved = ResourceManager.updateRecord(-1);
                if (saved) System.out.println("保存成功");
                else System.out.println("保存失败");
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            else if (command.equals("M")) {
                flag = 0;
                trade();
                Show.MazeView();
                Show.playerView();
            }
            else{
                // 移动指令
                int targetPositionType = Game.tarPosType(command);
                switch (targetPositionType) {
                    case 1->{
                        Game.move();
                        Show.MazeView();
                        Show.playerView();
                    } // 墙 // 不处理
                    case 2-> { // 防御塔
                        flag++;
                        Game.move();
                        Show.MazeView();
                        if (flag <= 1){
                            Show.towerView();
                            Show.playerView();
                            System.out.println("继续前进以开始战斗");
                            continue;
                        }
                        int roundOneDamage = Game.roundOne();
                        ResourceManager.playHitSound();
                        int roundTwoDamage = 0;
                        if (roundOneDamage >0)
                            roundTwoDamage = Game.roundTwo();
                        Show.MazeView();
                        System.out.print("你对敌人造成了"+Math.abs(roundOneDamage)+"点伤害");
                        if (roundOneDamage<0) {
                            System.out.println("，并将敌人击败");
                            flag = 0;
                        }else {
                            //塔没死
                            System.out.println();
                            if (roundTwoDamage == 0) System.out.println("你躲过了敌人的攻击");
                            else {
                                System.out.print("敌人对你造成了" + Math.abs(roundTwoDamage) + "点伤害");
                                if (roundTwoDamage < 0) {
                                    // 打印游戏失败
                                    ResourceManager.playDeadSound();
                                    System.out.println("，并将你击败");
                                    ResourceManager.stopGameBgm();
                                    Show.loseView();
                                    boolean saved = ResourceManager.updateRecord(0);
                                    if (saved) System.out.println("保存成功");
                                    else System.out.println("保存失败");
                                    try {
                                        Thread.sleep(1300);
                                        ResourceManager.stopDeadSound();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return;
                                } else System.out.println();
                            }
                        }
                        Show.towerView();
                        Show.playerView();
                    }
                    case 3->{// 空地或出口
                        flag = 0;
                        Game.move();
                        Show.MazeView();
                        Show.playerView();
                    }
                    case 6-> {
                        flag = 0;
                        Game.move();
                        Game.pick();
                        ResourceManager.playCoinSound();
                        Show.MazeView();
                        System.out.println("捡到30软妹币");
                        Show.playerView();
                        continue;
                    }
                    case 7-> {
                        flag = 0;
                        ResourceManager.playPotionSound();
                        Potions p = Game.consume(null);
                        Show.MazeView();
                        System.out.print("捡到药水,");
                        Show.potionConsumeView(p);
                        Show.playerView();
                    }
                }
            }

            // 判断是否胜利
            if (Game.isWin()) {
                ResourceManager.playWinSound();
                ResourceManager.updateRecord(1);
                    Show.WinView();
                    Show.clear();
                    if (Game.getRecord().highestLevel == 1){
                    System.out.println("你知道吗，方向+空格+步数可以加快步伐。");
                    try {
                        Thread.sleep(2300);
                        System.out.println("\r");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                boolean isEnd = Game.nextLevel(fromFile);
                if (isEnd){
                    Show.endView();
                    fromFile = false;
                    Game.nextLevel(fromFile);
                }
                Show.MazeView();
                Show.playerView();
            }
        }
    }

     public static void trade(){
//        控制购物流程
//        调用shopView()展示商店面板
//        调用getOrder()获取玩家在商店面板的选择;
//        若为退出交易，结束方法
//        若为选择索引
//            调用 Potion.values()获取药水数组
//            调用 getPlayer() 获取玩家对象
//            若玩家够钱，则
//                调用 consume(),把对应药水对象传进去,输出消费信息
//            否则输出余额不足
         Player player = Game.getPlayer();
         while(true){
             Show.clear();
             Show.shopView();
             int order=Input.getOrder();
             if (order == -1) return;
             else {
                 Potions ps = Potions.values()[order];
               try {
                   if (player.gold >= ps.price) {
                         System.out.printf("软妹币-%d\n", ps.price);
                         Game.consume(ps);
                         Show.potionConsumeView(ps);
                         ResourceManager.playPotionSound();
                   } else System.out.println("余额不足！");
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                         throw new RuntimeException(e);
                     }
             }
         }

    }

    public static void volumeSetting(){
        /*
            音量设置控制
            1.sout提示（0-100数值）
            2.循环：（输入+验证合法性）
            3.AudioManager.setVolume()设置音量
         */
        while (true) {
            Show.clear();
            Show.volumeSetView();
            int type = Input.getVolumeSetChoice();
            if (type == -1) return;
            int volume = Input.getVolumeInput();
            boolean flag = false;
            switch (type){
                case 1 ->{
                    flag = ResourceManager.setMenuVolume(volume);
                }
                case 2->{
                    flag = ResourceManager.setGameVolume(volume);
                }
                case 3->{
                    flag = ResourceManager.setEffectVolume(volume);
                }
            }
            if (flag){
                System.out.println("设置成功");
            }
            else {
                System.out.println("设置失败,也许重启能解决问题^_^");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public static void history(){
//        控制查看历史记录
//        while（true）:
//            getQuery()获取指令
//            若为-1；结束循环
//            若为1 ：unitRecordsView()
//            若为2 : soldierRecordView()
//            若为3 : playerRecordView()
        while (true){
            Show.clear();
            System.out.println(Show.YELLOW+Show.ITEMTAB+"\t\t -------------历史记录---------------\n"+Show.RESET);
            System.out.println(Show.CHOICETAB+"1 显示游戏的全部记录");
            System.out.println(Show.CHOICETAB+"2 显示角色的全部记录");
            System.out.println(Show.CHOICETAB+"3 显示玩家的全部记录");
            System.out.println(Show.CHOICETAB+"Q 返回主菜单");
            switch (Input.getQuery()){
                case -1-> {return;}
                case 1->Show.unitRecordsView();
                case 2->Show.soldierRecordView();
                case 3->Show.playerRecordView();
            }
        }

    }
}
