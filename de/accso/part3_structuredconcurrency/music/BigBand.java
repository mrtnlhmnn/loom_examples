package de.accso.part3_structuredconcurrency.music;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record BigBand(List<Instrument> instruments, List<Musician> musicians) {
    public BigBand {
        assert instruments.size() == musicians.size();
    }

    public void startToPlay() {
        // mix them all up
        Collections.shuffle(instruments);
        Collections.shuffle(musicians);

        String bigBandSetup =
                IntStream.range(0, instruments.size())
                .mapToObj(i ->  String.format(" %s plays '%s'", musicians.get(i), instruments.get(i)))
                .collect(Collectors.joining(", "));

        System.out.println("And finally ... Today's big band setup is: " + bigBandSetup);
    }
}
