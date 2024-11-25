package de.accso.loom.part3_future;

import de.accso.loom.part3_future.music.BigBand;

public class FutureExample {
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
