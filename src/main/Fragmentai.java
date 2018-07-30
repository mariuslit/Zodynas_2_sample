package main;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Fragmentai {

    // randa visus žodžius su fragmentu žodžio viduryje ir grąžina sąrašą

    // variantų paieškos varikliukas veikia puikiai
    public Set<String> rastiZodziusSuPradziosFragmentu(String fragment, Map<String, String> keyReiksmes) {

        Set<String> variant = new LinkedHashSet<>(); // čia talpinamas atsakymas

        for (String item : keyReiksmes.keySet()) {

            // jei žodis prasideda fragmentu
            if (item.toLowerCase().startsWith(fragment.toLowerCase())) { // true jei rado atitikmenį
                variant.add(item);
            }
        }
        return variant;
    }

    // randa visus žodžius su fragmentu žodžio viduryje ir grąžina sąrašą
    public Set<String> rastiZodziusSuVidurioFragmentu(String fragmentas, Map<String, String> keyReiksmes) {

        Set<String> variant = new LinkedHashSet<>();

        for (String item : keyReiksmes.keySet()) {

            // jei žodis ne prasideda fragmentu ir jei žodis turi fragmentą viduryje
            if (!item.toLowerCase().startsWith(fragmentas) && item.toLowerCase().contains(fragmentas)) {
                variant.add(item);
            }
        }
        return variant;
    }
}

