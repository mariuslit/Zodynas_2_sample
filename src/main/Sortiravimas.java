package main;

import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * teisingas a-z rūšiavimas, be didžiųjų / mažųjų raidžių
 */
public class Sortiravimas {

    // grąžina surūšiuotas key reikšmes a-z neatsižvelgiant į didžiasias raides
    public static TreeMap<String, String> sortiruotiAaZzKeyReiksmes(TreeMap<String, String> dictionaryTreeMap) {

        // sukuriam naują TreeMap
        // key = senasis key mažosiomis raidėmis + eilės skaičius jeigu dubluotas
        // value = key
        TreeMap<String, String> newTreeMap = new TreeMap<>();

        // key naudojamas tas pats žodis paverstas mažosiomis raidėmis ir pridėjus numeraciją žodžio priekyje
        int num = 0;
        for (String item : dictionaryTreeMap.keySet()) {
            if (newTreeMap.containsKey(item)) {
                newTreeMap.put(item.toLowerCase() + ++num, item);
            } else {
                newTreeMap.put(item.toLowerCase(), item);
            }
        }
        return newTreeMap;
    }


    // grąžina surūšiuotas key reikšmes a-z neatsižvelgiant į didžiasias raides
    public static TreeMap<String, String> sortiruotiAaZzKeyReiksmes(TreeSet<String> dictionaryTreeMap) {

        // sukuriam naują TreeMap
        // key = senasis key mažosiomis raidėmis + eilės skaičius jeigu dubluotas
        // value = key
        TreeMap<String, String> newTreeMap = new TreeMap<>();

        // key naudojamas tas pats žodis paverstas mažosiomis raidėmis ir pridėjus numeraciją žodžio priekyje
        int num = 0;
        for (String item : dictionaryTreeMap) {
            if (newTreeMap.containsKey(item)) {
                newTreeMap.put(item.toLowerCase() + ++num, item);
            } else {
                newTreeMap.put(item.toLowerCase(), item);
            }
        }
        return newTreeMap;
    }
}

