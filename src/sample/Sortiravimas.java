package sample;

import java.util.TreeMap;

public class Sortiravimas {

    // metodas skirtas surūšiuoti a-z neatsižvelgiant į didžiasias raides
    public static TreeMap<String,String> sortedTreeMap(TreeMap<String, String> oldTreeMap) {

        // sukuriam naują TreeMap
        // key = senasis key mažosiomis raidėmis + eilės skaičius jeigu dubluotas
        // value = key
        TreeMap<String, String> newTreeMap = new TreeMap<>();

        int num = 0;
        for (String item : oldTreeMap.keySet()) {
            if (newTreeMap.containsKey(item)) {
                newTreeMap.put(item.toLowerCase() + ++num, item);
            } else {
                newTreeMap.put(item.toLowerCase(), item);
            }
        }
        return newTreeMap;
    }

}
