package de.accso.part3_structuredconcurrency;

import de.accso.part3_structuredconcurrency.music.BigBand;
import de.accso.part3_structuredconcurrency.music.Instrument;
import de.accso.part3_structuredconcurrency.music.Musician;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.accso.part3_structuredconcurrency.util.TimeHelper.randomPause;
import static de.accso.part3_structuredconcurrency.util.TimeHelper.printTimed;

public class BigBandBuilder {
    public static void main(String[] args) {
        BigBand bigBand = new BigBandBuilder().getAllInstrumentsAndMusicians();
        bigBand.startToPlay();
    }

    public BigBand getAllInstrumentsAndMusicians() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // create tasks and fork them
            printTimed("Now forking to wake up all musicians");
            StructuredTaskScope.Subtask<List<Musician>>     musiciansTask = scope.fork( this::wakeUpMusicians   );

            printTimed("Now forking to search all instruments");
            StructuredTaskScope.Subtask<List<Instrument>> instrumentsTask = scope.fork( this::searchInstruments );

            // wait until all tasks are executed in parallel
            printTimed("Now joining both tasks ... waiting ...");
            int maxTimeOutInMs = 20_000; // 1s max for each musician => 17s max
            scope.joinUntil( Instant.now().plusMillis(maxTimeOutInMs) );
            printTimed("Both tasks joined, all done");

            // bring all instruments and all musicians together 🎵
            List<Instrument> instruments = instrumentsTask.get();
            List<Musician>     musicians = musiciansTask.get();
            return new BigBand(instruments, musicians);
        }
        catch (InterruptedException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Instrument> searchInstruments() throws InterruptedException {
        printTimed("Task: Searching all instruments ...");

        List<String> instruments = Stream.of(Instrument.AllInstruments.values()).map(Enum::name).toList();

        List<Instrument> instrumentsAsList = instruments.stream()
                .map(Instrument::new)
                .peek(instrument -> System.err.println("\t" + instrument.name() + " found and ready ..."))
                .peek(_ -> randomPause(100, 500)) // it takes a little while to find each instrument
                .collect(Collectors.toList());

        printTimed("Task: Searching all instruments ... done");

        return instrumentsAsList;
    }

    private List<Musician> wakeUpMusicians() throws InterruptedException {
        printTimed("Task: Waking up all musicians ...");

        List<String> musicians = Stream.of(Musician.AllMusicians.values()).map(Enum::name).toList();

        // now let's enforce an error here
        // if (musicians.size() > 0) throw new RuntimeException("stupid error, should end _all_ tasks"); // <<<--- SEE HERE

        List<Musician> musiciansAsList = musicians.stream()
                .map(Musician::new)
                .peek(musician -> System.err.println("\t" + musician.name() + " woke up ..."))
                .peek(_ -> randomPause(10, 1_000)) // it might takes some time to wake up each musician
                .collect(Collectors.toList());

        printTimed("Task: Waking up all musicians ... done");

        return musiciansAsList;
    }
}
