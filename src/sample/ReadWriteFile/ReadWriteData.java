package sample.ReadWriteFile;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * txt failas turi būti pagrindiniame projekto kataloge, jokiu būdu ne crs\, ne \crs\sample
 * mano kodas pagal << Created by andriusbaltrunas on 3/3/2017. >> pavyzdį
 */
public class ReadWriteData {
    private static final String SPLITERIS = " -->> ";
    private static final String SPLITERIS2 = " <enter> ";

    public static Map readFile(String failoVardas) {
        Map<String, String> duomenys = new TreeMap<>();
        try {
            BufferedReader failas = new BufferedReader(new FileReader(new File(failoVardas + ".txt")));
            String line;

            while ((line = failas.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] masyvasDuZodziai = line.split(SPLITERIS);
                String s = masyvasDuZodziai[1].replace(SPLITERIS2,"\n");
                if (masyvasDuZodziai.length == 2) {
                    duomenys.put(masyvasDuZodziai[0], s);
                }
            }
        } catch (IOException e) {
            System.out.println("Can t read data from file");
        }
        return duomenys;
    }

    public static void writeFile(Map<String, String> zodynas, String failoVardas) {
        try (BufferedWriter failas = new BufferedWriter(new FileWriter(failoVardas + ".txt"))) {
            for (String item : zodynas.keySet()) {
                failas.write(item + SPLITERIS + zodynas.get(item).replace("\n",SPLITERIS2));
                failas.newLine();
            }
//            System.out.println("writeFile >> pakeistas failas: " + failoVardas + ".txt");
        } catch (IOException e) {
            System.out.println("Can t write data to file");
        }
    }

}
