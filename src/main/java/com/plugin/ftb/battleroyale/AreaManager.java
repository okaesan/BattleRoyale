package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
 
import javax.swing.*;
public class AreaManager {
	/*
     * 禁止区域の順番リストを返す
     */
    public static List<Integer> getRandomList() {
        List<Integer> randomList = new ArrayList<>();
        int[] list = prepareList();
        int ran = new Random().nextInt(list.length);
        list[ran] = -1;
        randomList.add(ran);
        for(int i=1; i<list.length; i++) {
            int highestIndex = getHighestIndex(getCountedList(list));
            list[highestIndex] = -1;
            randomList.add(highestIndex);
        }
       
        return randomList;
    }
   
    private static int[] prepareList() {
        int[] list = {0, 0, 0, 0,
                      0, 0, 0, 0,
                      0, 0, 0, 0,
                      0, 0, 0, 0};
        return list;
    }
   
    private static int[] getCountedList(int[] list) {
        for(int i=0; i<list.length; i++) {
            int count = 0;
            if(list[i] == -1) continue;
            int up = i-4 < 0 ? -1 : list[i-4];
            int down = i+4 > list.length-1 ? -1 : list[i+4];
            int right = i+1 > list.length-1 || (i+1)%4 == 0 ? -1 : list[i+1];
            int left = i-1 < 0 || (i+4)%4 == 0 ? -1 : list[i-1];
           
            if(up == -1) count += 1;
            if(down == -1) count += 1;
            if(right == -1) count += 1;
            if(left == -1) count += 1;
           
            list[i] = count;
        }
       
        return list;
    }
   
    private static int getHighestIndex(int[] list) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        for(int i=0; i<list.length; i++) {
            hashMap.put(i, list[i]);
        }
        List<Entry<Integer,Integer>> entries =
                  new ArrayList<Entry<Integer,Integer>>(hashMap.entrySet());
            Collections.sort(entries, new Comparator<Entry<Integer,Integer>>() {
     
                @Override
                public int compare(
                      Entry<Integer,Integer> entry1, Entry<Integer,Integer> entry2) {
                    return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
                }
            });
       
        ArrayList<Integer> highest = new ArrayList<>();
        for (Entry<Integer,Integer> s : entries) {
            if(entries.get(0).getValue() == s.getValue()) {
                highest.add(s.getKey());
            }else {
                break;
            }
        }
       
        Collections.shuffle(highest);
        return highest.get(0);
    }
}
