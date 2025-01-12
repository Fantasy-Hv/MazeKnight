package com.BugFirstJava.View;

import com.BugFirstJava.Dao.Items.Potions;
import com.BugFirstJava.Dao.Level.Level;
import com.BugFirstJava.Service.Game;
import com.BugFirstJava.Service.ResourceManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    //--------------------------接受输入---------------------------------------
    static Scanner sc = new Scanner(System.in);
    /*
       编号：8
       难度：简单
       负责人：刘昊
       功能：接受 [游戏过程中]玩家的指令：
            移动指令包括方向和步数
            若输入不合法，输出错误提示，并重新接受指令
            若输入合法，返回指令
             *输入不区分大小写
             合法示例：w 5
              移动：WASD + 空格 + 步数
              退出：Q
       返回值：指令对应字符的大写形式(+空格+步数)
       *合法输入加上M（商城页面）
    */
    public static String getCommand(){
         // 提示玩家
        System.out.print("请输入移动:(WASD), (Q退出),(M商城): ");
        while(true) {
            String inputstr = sc.nextLine();
            String input = inputstr.toUpperCase();//转换为大写
            // 检查是否为退出指令
            if ("Q".equals(input)) {
                return "Q";
            }
            // 检查是否为商城指令
            if ("M".equals(input)) {
                return "M";
            }
            String[] parts = input.split(" ", 2);//分割字符串
            if (parts.length == 2) {
                // 验证方向指令
                if (!"W".equals(parts[0]) && !"A".equals(parts[0]) && !"S".equals(parts[0]) && !"D".equals(parts[0])) {
                    System.out.println("方向必须是 W, A, S, 或 D");
                    continue;
                }
                // 验证步数是否为正整数
                try {
                    int steps = Integer.parseInt(parts[1]);
                    if (steps <= 0) {
                        System.out.println("步数必须是正整数.");
                        continue;
                    }
                    return input;
                } catch (Exception e) {
                    System.out.println("步数必须是一个有效的正整数.");
                }
            } else if (parts.length == 1) {
                // 单个字符指令，默认一步
                if ("W".equals(parts[0]) || "A".equals(parts[0]) || "S".equals(parts[0]) || "D".equals(parts[0])) {
                    return parts[0] + " 1";
                } else {
                    System.out.println("错误请重新输入.");
                }
            } else {
                System.out.println("错误请重新输入.");
            }
        }
    }



    /*
    编号：9
    难度：简单
    负责人：刘昊
       功能：接受玩家在[菜单界面]的选择：
            若输入不合法，输出错误提示，并重新接受指令
            若输入合法，返回指令
       返回值：指令对应数字
    */
    public static int getMenuChoice(){

        while(true) {
            try {
                int n = sc.nextInt();
                sc.nextLine();
            if (n < 1 || n > 5) System.out.println("输入错误,重新输入");
            else return n;
            }catch (InputMismatchException e) {
                System.out.println("输入错误,重新输入");
                sc.next();
            }

        }
    }
public static int getVolumeInput(){
         while(true){
            int n=0;
             System.out.print("请输入音量大小(0-100):");
             try {
                 n = Integer.parseInt(sc.nextLine().trim());
                 if (n < 0 || n > 100) {
                     System.out.println("输入有误，请重新输入！");
                 } else return n;
             }catch(Exception e){
                 System.out.println("输入有误，请重新输入！");
                  sc.next();
             }
         }
}
public static int getVolumeSetChoice() {
//        获取玩家在设置面板的选择
//        可能的输入：1 2 3 Q(不分大小写)
//         while（true）:
//            读取输入
//            判断输入
//            不合法->打印错误信息提示
//            合法->返回选择，Q用-1代替

        while (true) {
            System.out.print("请输入选择（1/2/3/Q）：");
            try {
                String n = sc.nextLine().trim();
                if (n.equals("Q") || n.equals("q")) {
                    return -1;
                } else if (n.equals("1") || n.equals("2") || n.equals("3")) {
                    return Integer.parseInt(n);
                } else {
                    System.out.println("输入有误，请重新输入！");
                }
            } catch (Exception e) {
                System.out.println("输入有误，请重新输入！");
                sc.next();
            }
        }
    }


    /*
       编号：10
       难度：简单
       负责人：刘昊
       功能：接受 [兵种选择时]玩家的选择：
       参数：无
       返回值： soldier实例对应字符的大写形式
          while（true）:
            打印提示信息，提示玩家选择兵种
            接受玩家输入的兵种符号
            调用Game.isExist()方法判断输入是否合法
            若输入不合法，输出错误提示，进入下一轮循环继续接受输入
            若输入合法，退出循环，返回该字符的大写形式
     */
    public static char getSoldierChoice(){
        while (true) {
            System.out.println("请选择你要使用的角色(输入对应符号):");
            String input = sc.nextLine();
            if (input.length() > 0) {
                char inputstr = input.charAt(0);
                inputstr = Character.toUpperCase(inputstr);
                if (Game.isExist(inputstr)) return inputstr;
                else System.out.println("输入错误，请重新输入");
            } else {
                System.out.println("请输入内容，不能为空，重新输入");
            }
        }
    }
public static boolean getLoadingChoice(){
    while(true){
        System.out.print("是否加载存档? (输入Y 或 N)：");
        try {
            String confirm = sc.nextLine();
            if (confirm.equals("Y") || confirm.equals("y")) {
                return true;
            } else if (confirm.equals("N") || confirm.equals("n")) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("输入有误，请重新输入");
            sc.next();
        }
    }
}

      public static int getOrder(){
//        获取玩家在商店面板的选择
//        调用 Potion.values()获取药水数组
//        可能的输入
//           药水数组索引
//           Q : 退出交易（不分大小写）
//        while（true）:
//            读取输入
//            判断输入
//            不合法（索引越界/非法字符）->打印错误信息提示
//            合法->返回选择，Q用-1代替
                 Potions[] potions=Potions.values();
          int potionsLength = potions.length;
          System.out.println("请输入药水的索引值进行购买，或输入 Q/q 退出商店：");
          Scanner sc = new Scanner(System.in);
          while (true) {
              String input = sc.nextLine();
              // 判断输入是否为 Q 或 q，表示退出交易
              if (input.equalsIgnoreCase("Q")) {
                  System.out.println("退出商店。欢迎下次光临！");
                  return -1; // 返回 -1 表示退出
              }
              // 尝试将输入转换为整数
              try {
                  int choice = Integer.parseInt(input);
                  // 检查索引是否越界
                  if (choice > 0 && choice <= potionsLength) {
                      return choice-1; // 返回合法的索引值
                  } else {
                      // 打印错误信息
                      System.out.println("无效的输入，请输入有效的药水索引（1-" + potionsLength + "），或输入 Q/q 退出。");
                  }
              } catch (NumberFormatException e) {
                  // 如果输入不是整数，打印错误信息
                  System.out.println("无效的输入，请输入有效的药水索引（1-" + potionsLength + "），或输入 Q/q 退出。");
              }
          }
    }
    public static int getQuery() {
//        获取玩家在历史战绩面板的选择
//        可能的输入：1 2 3 Q(不分大小写)
//         while（true）:
//            读取输入
//            判断输入
//            不合法->打印错误信息提示
//            合法->返回选择，Q用-1代替

        while (true) {
            System.out.print("请输入选择（1/2/3/Q）：");
            try {
                String n = sc.nextLine().trim();
                if (n.equals("Q") || n.equals("q")) {
                    return -1;
                } else if (n.equals("1") || n.equals("2") || n.equals("3")) {
                    return Integer.parseInt(n);
                } else {
                    System.out.println("输入有误，请重新输入！");

                }
            } catch (Exception e) {
                System.out.println("输入有误，请重新输入！");
                sc.next();
            }
        }
    }

    public static boolean confirmBattle(){
        while(true){
            System.out.print("确认战斗:Y or 取消战斗:N,请输入:(Y/N):");

            try{
                String confirm=sc.nextLine();
                if(confirm.equals("Y") || confirm.equals("y")){
                    return true;
                }else if(confirm.equals("N" )|| confirm.equals("n")){
                    return false;
                }else{
                    System.out.println("输入有误，请重新输入！");

                }
            }catch(Exception e){
                System.out.println("输入有误，请重新输入！");
                sc.next();
            }
        }

    }
}
