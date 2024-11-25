package de.accso.part3_structuredconcurrency;

import de.accso.part3_structuredconcurrency.music.BigBand;

public class StructuredConcurrencyExample {
    public static void main(String[] args) {
        BigBand bigBand = new BigBandBuilder().getAllInstrumentsAndMusicians();

        if (bigBand == null) {
            System.err.println("Too bad ... No BigBand could be built.");
        }
        else {
            bigBand.startToPlay();
        }
    }
}
