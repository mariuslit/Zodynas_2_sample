package sample;

import java.util.Map;
import java.util.TreeMap;

public class Informacija {

    private Map<String, String> zodynasR = new TreeMap<>();
    private String fragmentasR = new String();
    private String vertimasR = new String();
    private String zodynoIdR = new String();

    // konstruktoriaus Overloading'as
    public Informacija(Map<String, String> zodynas, String fragmentas) {
        this.zodynasR = zodynas;
        this.fragmentasR = fragmentas;
    }

    // konstruktoriaus Overloading'as
    public Informacija(Map<String, String> zodynas, String fragmentas, String vertimas/*, String zodynoID*/) {
        this.zodynasR = zodynas;
        this.fragmentasR = fragmentas;
        this.vertimasR = vertimas;
//        this.zodynoIdR = zodynoID; // jei reikės papildomo parametro
    }

    public void setInformacija(Map<String, String> zodynas, String fragmentas) {
        this.zodynasR = zodynas;
        this.fragmentasR = fragmentas;
//        System.out.println(fragmentasR);
    }

    public Map getZodynas() {
        return zodynasR;
    }

    public String getFragmentas() {
        return fragmentasR;
    }

    public String getVertimas() {
        return vertimasR;
    }
//    public String getZodynoID() {
//        return zodynoIdR;
//    }
}
