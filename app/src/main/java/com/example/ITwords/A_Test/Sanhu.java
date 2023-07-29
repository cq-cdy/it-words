//广度优先遍历
//        我们将三个瓶子的状态标示为一个数。
//        8 0 0
//        然后开始拓展这个数的所有可能的状态，第一步这个数可以变为，括号里的数是上一步的数字
//        3 5 0（8 0 0） 、 5 0 3（8 0 0）
//        然后继续拓展第二步所有可能的状态，并且不得和之前的状态出现重复（这叫剪枝）
//        0 5 3（3 5 0）、3 2 3（3 5 0）、5 3 0（5 0 3）
//        继续第三步
//        6 2 0（3 2 3）、2 3 3（5 3 0）
//        我们发现状态变少了，这是因为剪枝约束 — 不得出现和之前重复的状态，这个很重要进行一定量的剪枝。
//        继续第四步
//        6 0 2（6 2 0）、2 5 1（2 3 3）
//        继续第五步，怎么还没出现 4 这个数字呢，好着急啊！
//        1 5 2（6 0 2）、7 0 1（2 5 1）
//        继续第六步
//        1 4 3（1 5 2）
//        这就是算法的停止条件，出现第一个数字 4。所以最终的路径就是
//        1 4 3 <-- 1 5 2 <-- 6 0 2 <-- 6 2 0 <-- 3 2 3 <-- 3 5 0 <-- 8 0 0
//
//        代码：
import java.util.*;

/**
 * 使用广度优先遍历，所以第一次解的的答案所需的倒水次数最少，解为最优解
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sanhu {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入三个值表示容器的容水量,三个瓶子的容积目前是8，5，3：");
        int x1 = sc.nextInt();
        int x2 = sc.nextInt();
        int x3 = sc.nextInt();

        String string = x1+""+x2+""+x3;
        System.out.println(string);

        ContainerPot containerPot = new ContainerPot(8, 0, 0);
        List<String> list = new ArrayList<String>(); //保存状态
        list.add(string); //将第一个状态存入到状态数组中
        containerPot.init();//初始化这些瓶子可以装多少水
        String result = "";//保存最终带有4的结果
        int n = 0;//状态计数
        do {
            //遍历倒水
            for (int i = 0; i < 3 && containerPot.flag; i++) {
                for (int j = 0; j < 3 && containerPot.flag; j++) {
                    if (i != j) { //不能让壶相同
                        if (containerPot.canPour(i, j)) { //是否可以倒水
                            String str = containerPot.getString();//保存初始状态
                            //倒入水
                            containerPot.pour(i, j);
                            String strResult = containerPot.getString();
                            if (!list.contains(strResult)) {
                                list.add(strResult);
                                containerPot.addList(str, strResult);
                            }
                            //如果出现容量为4的壶就停止
                            if (containerPot.pot[0] == 4 || containerPot.pot[1] == 4 || containerPot.pot[2] == 4) {
                                result = result + containerPot.pot[0] + containerPot.pot[1] + containerPot.pot[2];
                                containerPot.flag = false;//已经找到
                                break;
                            }
                            //初始化倒水到上一步
                            containerPot.intPot(str);
                        }
                    }
                }
            }
            n++;
            containerPot.intPot(list.get(n));
        } while (containerPot.flag);
        System.out.println(list);
        System.out.println(containerPot.listMap);
        String key = result;
        System.out.print(key + "<--");
        do {
            for (Map.Entry<String, List<String>> entry : containerPot.listMap.entrySet()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    if (entry.getValue().get(i).equals(key)) {
                        System.out.print(entry.getKey() + "<--");
                        key = entry.getKey();
                        break;
                    }
                }

            }
        } while (containerPot.listMap.containsKey(key) && !key.equals(string));

        System.out.println();
    }
}


class ContainerPot {

    int[] pot = new int[3];
    //    List<String> list;//存放已经出现的结果
    Map<String, List<String>> listMap = new HashMap<>();
    int[] maxP = new int[3];

    boolean flag = true;

    public ContainerPot(int a, int b, int c) {
        pot[0] = a;
        pot[1] = b;
        pot[2] = c;
    }

    public void init() {
        maxP[0] = 8;
        maxP[1] = 5;
        maxP[2] = 3;
    }

    //将key值作为它的上一步
    public void addList(String key, String str) {
        if (listMap.isEmpty() || !listMap.containsKey(key)) {
            List<String> temp = new ArrayList<String>();
            temp.add(str);
            listMap.put(key, temp);
        } else {
//            System.out.println(listMap.containsKey(key));
//            System.out.println(listMap.get(key));
            listMap.get(key).add(str);
        }
    }

    //判读是否已经有这个key值在里面存放了
    public boolean isAdd(String str) {
        return listMap.containsKey(str);
    }

    //判读是否可以从from壶倒水到to壶
    public boolean canPour(int from, int to) {
        //如果没用水就不能倒了
        if (pot[from] == 0) {
            return false;
        }

        //如果里面已经有水了 就不能倒了
        if (pot[to] == maxP[to]) {
            return false;
        } else {
            return true;
        }
    }

    //倒水的过程
    public void pour(int from, int to) {
        //做一个判读看看是否能剩水
        if (pot[from] + pot[to] > maxP[to]) {
            pot[from] = pot[from] - (maxP[to] - pot[to]);
            pot[to] = maxP[to];
        } else {
            pot[to] = pot[to] + pot[from];
            pot[from] = 0;
        }
    }

    public String getString() {
        String str = "";
        for (int i = 0; i < pot.length; i++) {
            str = str + pot[i];
        }
        return str;
    }

    public void intPot(String str) {
        for (int i = 0; i < str.length(); i++) {
            pot[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }
    }

    public void printDD() {
        for (List<String> value : listMap.values()) {
            System.out.println(value.toString());
        }
    }
}
