package com.change.test;

/**
 * @author YangQing
 * @version 1.0.0
 */

/*公式解析
在某些应用中，为了支持灵活性，往往用到自定义的公式。
比如，有如下的原始公式集合：
    int add(int x, int y):  返回x与y的和
    int add(int x, int y, int z):  返回x,y,z三个数的和
    int min(int x, int y):  返回x,y中较小的值
    int max(int x, int y):  返回x,y中较大的值
    int doubleMe(int x):  返回 x 的2倍
给出一个自定义公式串
add(min(5,3),max(2,8),add(1,doubleMe(1)))
通过手工计算可以得出结果为：14
本题的任务是：编写一个解析程序，能够对由上述原始公式任意组合出来的公式计算其结果。
也就是输入一个自定义公式串，输出它的计算结果（可以不考虑输入公式本身有语法错误的情况）。
输入的公式串中可以含有多余的空格，类似：
add( min(5, 3) , max(2 , 8) )  也是合法的公式。

程序输入：公式串
程序输出：该公式的计算值
 */
import java.util.Scanner;
import java.util.Stack;

public class FormulaCalculator01 {
    // add 返回x与y的和
    public static int add(int x,int y){
        return x+y;
    }
    // add 返回x,y,z三个数的和
    public static int add(int x,int y,int z){
        return x+y+z;
    }
    // min 返回x,y中较小的值
    public static int min(int x, int y){
        return x<y?x:y;
    }
    // max 返回x,y中较大的值
    public static int max(int x, int y){
        return x>y?x:y;
    }
    // doubleMe 返回 x 的2倍
    public static int doubleMe(int x){
        return x*2;
    }
    public static int check(String s){
        Stack<String> method = new Stack<String>(); // 保存方法名
        Stack<Character> bracket = new Stack<Character>();// 保存对应括号
        Stack<String> num = new Stack<String>();    // 保存数字
        Stack<Integer> count = new Stack<Integer>(); //保存参数个数

        StringBuffer ss = new StringBuffer();   // 添加字母
        StringBuffer nn = new StringBuffer();   // 添加数字
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(c>='A'&&c<='Z'||c>='a'&&c<='z'){
                ss.append(c);   // 拼凑方法名
            }else if(c=='('){
                method.push(ss.toString()); // 方法名拼凑完成,添加到栈
                count.push(0);
                ss.setLength(0);    // 放入方法,并清空临时串
                bracket.push(')');
            }else if(c>='0'&&c<='9'){
                nn.append(c);   // 拼凑数字
            }else if(nn.length()!=0){
                num.push(nn.toString());    // 数字名拼凑完成,添加到栈
                count.push(count.pop()+1);  // 参数个数加1
                nn.setLength(0);
            }
            if(c==')'){
                String m = method.pop();    // 得到方法
                int[] n = new int[count.pop()]; // 依参数个数开辟数组
                for(int j=0;j<n.length;j++){ // 得到每个参数
                    n[j] = Integer.parseInt(num.pop());
                }
                bracket.pop();  // 去一层括号
                if("add".equals(m)){
                    if(n.length==2){    // add() 两个参数
                        num.push(""+add(n[1],n[0]));
                    }else if(n.length==3){  // add() 三个参数
                        num.push(""+add(n[2],n[1],n[0]));
                    }
                }else if("min".equals(m)){  // min()
                    num.push(""+min(n[1],n[0]));
                }else if("max".equals(m)){  // max()
                    num.push(""+max(n[1],n[0]));
                }else if("doubleMe".equals(m)){ // doubleMe()
                    num.push(""+doubleMe(n[0]));
                }
                if(count.size()!=0){    // 如果不是最外层
                    count.push(count.pop()+1);
                }
            }
        }
        return Integer.parseInt(num.pop()); // 返回最后的结果数据
    }
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("输入公式串");
//        add(max(5,3),max(2,8),add(1,doubleMe(1)))
        String s = scan.nextLine();
        System.out.println(check(s));
    }
}