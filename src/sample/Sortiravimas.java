package sample;

import java.util.LinkedHashMap;
import java.util.Map;

public class Sortiravimas {

    private Map<String, String> sortingLinkedHashMap() {

        // įkeliam žodyną į nerūšiuotą mapą
        Map<String, String> unsortMap = new LinkedHashMap<>(Controller.dictionaryTreeMap);
        Map<String, String> map = new LinkedHashMap<>();

        // sukuriam naują LinkedHashMap
        // key = senasis key mažosiomis raidėmis + eilės skaičius jeigu dubluotas
        // o value = key

        for (String item : unsortMap.keySet()) {
            map.put(item.toLowerCase(), item);
        }

        return unsortMap;
    }
}
