package sample.ReadWriteFile;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * txt failas turi būti pagrindiniame projekto kataloge, jokiu būdu ne crs\, ne \crs\sample
 * mano kodas pagal << Created by andriusbaltrunas on 3/3/2017. >> pavyzdį
 */
public class ReadWriteData {
    private static final String SPLITER = " -->> ";
    private static final String SPLITER2 = " <enter> ";

    public static Map readFile(String failoVardas) {
        Map<String, String> data = new TreeMap<>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(new File(failoVardas + ".txt")));
            String line;

            while ((line = file.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] mas = line.split(SPLITER);
                String s = mas[1].replace(SPLITER2,"\n");
                if (mas.length == 2) {
                    data.put(mas[0], s);
                }
            }
        } catch (IOException e) {
            System.out.println("Can t read data from file");
        }
        return data;
    }

    public static void writeFile(Map<String, String> dict, String fileName) {
        try (BufferedWriter file = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            for (String item : dict.keySet()) {
                file.write(item + SPLITER + dict.get(item).replace("\n", SPLITER2));
                file.newLine();
            }
//            System.out.println("writeFile >> pakeistas failas: " + failoVardas + ".txt");
        } catch (IOException e) {
            System.out.println("Can t write data to file");
        }
    }

}
