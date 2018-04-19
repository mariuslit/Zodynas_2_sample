package sample;

import java.util.Map;
import java.util.TreeMap;

public class Informacija {

    private Map<String, String> zodynasI = new TreeMap<>();
    private String fragmentasI = new String();

//    public Informacija(Map<String, String> zd, String fr) {
//        this.zodynasI = zd;
//        this.fragmentasI = fr;
//    }

    public void setInformacija(Map<String, String> zd, String fr) {
        this.zodynasI = zd;
        this.fragmentasI = fr;
        System.out.println(fragmentasI);
    }

    public String getFragmentas() {
        return fragmentasI;
    }

    public Map getZodynas() {
        return zodynasI;
    }

}
