package com.example.ITwords.A_Test;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class ThreeHu {
    Hu[] hus = new Hu[3];
    ArrayList<int[]> state = new ArrayList<int[]>();
    ThreeHu(){
        Hu hu1 = new Hu(8,8);
        Hu hu2 = new Hu(0,5);
        Hu hu3 = new Hu(0,3);
        hus[0] = hu1;
        hus[1] = hu2;
        hus[2] = hu3;
    }
    public void getStates(){
        StringBuilder s = new StringBuilder("  ");
        for( Hu h:hus){
            s.append(h.current_len);
        }
        System.out.println(s.toString());
    }

    boolean is_exist_state(Hu[] hus){
        for (int[] ints : state) {
            if(ints[0]== hus[0].current_len && ints[1]==hus[1].current_len
                    &&ints[2] == hus[2].current_len){
                return true;
            }
        }
        return false;
    }
    public boolean pull_each(Hu from ,Hu to,int value){
        if (from.is_pull(value)&& to.is_pull(value)){
            from.current_len-=value;
            to.current_len += value;
            return true;
        }
        return false;
    }


    public static void main(String[] args) {

        //new ThreeHu().getStates();
        ArrayList<int[]> state = new ArrayList<int[]>();
        int[] s = {1,2,3};
        int[] b = {1,2,3};


    }





}
class Hu{
    int max_len = 0;
    int current_len = 0;

    public Hu(int current,int max_len){
        this.current_len = current;
        this.max_len = max_len;
    }
    private   boolean isFull(){
        return max_len <= current_len;
    }

    public boolean is_add(int a){
        return !this.isFull() && a > 0 && current_len + a <= max_len;
    }

    public  boolean  is_pull(int a){
        return current_len >= 0 && a > 0 && current_len - a >= 0;

    }



}

