package sample.ReadWriteFile;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MANO KODAS, KOPIJUOTA IŠ: << Created by andriusbaltrunas on 3/3/2017. >>
 * TXT FAILAS TURI BŪTI pagrindiniame projekto kataloge ( jokiu būdu ne crs\, ne \crs\sample)
 */
public class ReadWriteData {
// kai metodas ArrayList reikalingas s
    public static void readFile(Map<String, String> zoynas, String failoVardas) {
        System.out.println("Užkrautas žodynas: " + failoVardas);
//        ArrayList<String> s = new ArrayList<>();
        try {
            BufferedReader failas = new BufferedReader(new FileReader(new File(failoVardas + ".txt")));
            String line;
            while ((line = failas.readLine()) != null) { // jeigu eilutė egzistuoja, t.y. ne lygi nuliui
                String[] masyvasDuZodziai = line.split("-");
                if (masyvasDuZodziai.length == 2) {
                    zoynas.put(masyvasDuZodziai[0], masyvasDuZodziai[1]);
//                    s.add(masyvasDuZodziai[0]);
                }
            }
        } catch (IOException e) {
        }
//        return s;
    }

    public static void writeDataIntoFile(List<String> zodynas, String failoVardas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(failoVardas + ".txt"))) {
            for (String item : zodynas) {
                bw.write(item);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Can t write data to file");
        }
    }

}
