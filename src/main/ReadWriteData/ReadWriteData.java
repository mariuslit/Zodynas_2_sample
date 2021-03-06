package main.ReadWriteData;

import java.io.*;
import java.util.TreeMap;

/**
 * txt failas turi būti pagrindiniame projekto kataloge, jokiu būdu ne crs\, ne \crs\main
 * mano kodas sukurtas pagal Andriaus pvz: << Created by andriusbaltrunas on 3/3/2017. >>
 */
public class ReadWriteData {
    private static final String SPLITER = " -->> ";
    private static final String SPLITER2 = " <enter> ";
    private static final String TXT = ".txt";
    private static final String UTF8 = "UTF-8";

    public static TreeMap readFile(String fileName) {
        TreeMap<String, String> data = new TreeMap<>();
        try (BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + TXT), UTF8))){
            String line;
            while ((line = file.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] mas = line.split(SPLITER);
                String string = mas[1].replace(SPLITER2, "\n");
                if (mas.length == 2) {
                    data.put(mas[0], string);
                }
            }
        } catch (Exception e) {
            System.out.println("Can t read data from file");
        }
        return data;
    }

    // duomenų įrašymas į failą
    public static void writeFile(TreeMap<String, String> dict, String fileName) {
        try (BufferedWriter file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + TXT), UTF8))){
            for (String item : dict.keySet()) {
                file.write(item + SPLITER + dict.get(item).replace("\n", SPLITER2));
                file.newLine();
            }
        } catch (IOException e) {
            System.out.println("Can t write data to file");
        }
    }

}
