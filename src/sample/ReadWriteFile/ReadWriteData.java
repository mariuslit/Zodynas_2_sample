package sample.ReadWriteFile;

import sample.Controller;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * MANO KODAS, KOPIJUOTA IŠ: << Created by andriusbaltrunas on 3/3/2017. >>
 * TXT FAILAS TURI BŪTI pagrindiniame projekto kataloge ( jokiu būdu ne crs\, ne \crs\sample)
 */
public class ReadWriteData {
    public static Map readFile(String failoVardas) {
        Map<String,String> duomenys = new TreeMap<>();
        try {
            BufferedReader failas = new BufferedReader(new FileReader(new File(failoVardas + ".txt")));
            String line;

            while ((line = failas.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] masyvasDuZodziai = line.split("-");
                if (masyvasDuZodziai.length == 2) {
                    duomenys.put(masyvasDuZodziai[0], masyvasDuZodziai[1]);
                }
//                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Can t read data to file");
        }
        return duomenys;
    }

    public static void writeFile(Map<String, String> zodynas, String failoVardas) {
        if (!failoVardas.startsWith("z")) { // todo laikinai neleidžama įrašinėti į z1-z6
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(failoVardas + ".txt"))) {
                for (String item : zodynas.keySet()) {
                    bw.write(item + "-" + zodynas.get(item));
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Can t write data from file");
            }
        }
    }

}
