package sample.ReadWriteData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * txt failas turi būti pagrindiniame projekto kataloge, jokiu būdu ne crs\, ne \crs\sample
 * mano kodas pagal << Created by andriusbaltrunas on 3/3/2017. >> pavyzdį
 */
public class ReadWriteData {
    private static final String SPLITER = " -->> ";
    private static final String SPLITER2 = " <enter> ";
    public static int x = 0; // laikinas skaitliukas

    public static ObservableList getObsList(String fileName) {
        System.out.println(x+" obs");
        return FXCollections.observableList(new ArrayList<>(readFile(fileName).keySet()));
//        TreeMap<String,String> tr = new TreeMap(readFile(fileName));
//        ObservableList<String> obs;
//        List<String> str = new ArrayList<>(tr.keySet()); // todo observableList
//        return xxx;
//        for (String item : tr.keySet()) {
//        }
    }

    public static TreeMap readFile(String fileName) {
        TreeMap<String, String> data = new TreeMap<>();
        try {
//            BufferedReader file = new BufferedReader(new FileReader(new File(fileName + ".txt"))); // pirminė versija be UTF-8
            BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + ".txt"), "UTF-8"));

            /*
            try with resources try(BufferedReader file = new BufferedReader(new FileReader(failoVardas + ".txt"))){
....
            }cath(Exception e){
....
            }// #3

            */

            System.out.println(++x + " readFile: " + fileName);
            String line;

            while ((line = file.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] mas = line.split(SPLITER);
                String s = mas[1].replace(SPLITER2, "\n");
                if (mas.length == 2) {
                    data.put(mas[0], s);
                }
            }
        } catch (Exception e) {
            System.out.println("Can t read data from file");
        }
        return data;
    }

    public static void writeFile(TreeMap<String, String> dict, String fileName) {
//        try (BufferedWriter file = new BufferedWriter(new FileWriter(fileName + ".txt"))) { // pirminė versija be UTF-8
        try (BufferedWriter file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".txt"), "UTF-8"))) {
            for (String item : dict.keySet()) {
                file.write(item + SPLITER + dict.get(item).replace("\n", SPLITER2));
                file.newLine();
            }
            System.out.println(++x + " writeFile: " + fileName);
        } catch (IOException e) {
            System.out.println("Can t write data to file");
        }
    }

}
