package de.accso.loom.part3_future;

import de.accso.loom.part3_future.music.BigBand;
import de.accso.loom.part3_future.music.Instrument;
import de.accso.loom.part3_future.music.Musician;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.accso.loom.part3_future.util.TimeHelper.*;

public class BigBandBuilder {

    public BigBand getAllInstrumentsAndMusicians() {
        BigBand result = null;

        int maxTimeOutInMs = 20_000; // 1s max for each musician => 17s max

        try (var executor = Executors.newFixedThreadPool(2)) {

            // (1) create tasks and fork them
            logWithTime("Now forking to wake up all musicians");
            Future<List<Musician>>   musiciansTask   = executor.submit( this::wakeUpMusicians );

            logWithTime("Now forking to search all instruments");
            Future<List<Instrument>> instrumentsTask = executor.submit( this::searchInstruments );

            // (2) wait until all tasks are executed in parallel
            //     get all results and bring instruments and musicians together 🎵
            logWithTime("Now joining both tasks ... waiting ...");
            List<Musician>     musicians =   musiciansTask.get(maxTimeOutInMs, TimeUnit.MILLISECONDS);
            List<Instrument> instruments = instrumentsTask.get(maxTimeOutInMs, TimeUnit.MILLISECONDS);
            logWithTime("Both tasks joined, all done");
            result = new BigBand(instruments, musicians);

            return result;

        }
        catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Instrument> searchInstruments() {
        logWithTime("Task: Searching all instruments ... starting");

        List<String> instrumentNames = Stream.of(Instrument.AllInstruments.values()).map(Enum::name).toList();

        List<Instrument> instruments = instrumentNames.stream()
                .peek(_ -> randomPause(50, 500)) // it takes a while to find each instrument
                .map(Instrument::new)
                .peek(instrument -> System.err.println("\tInstrument " + instrument.name() + " found and ready ..."))
                .collect(Collectors.toList());

        logWithTime("Task: Searching all instruments ... done");

        return instruments;
    }

    private List<Musician> wakeUpMusicians() {
        logWithTime("Task: Waking up all musicians ... starting");

        List<String> musicianNames = Stream.of(Musician.AllMusicians.values()).map(Enum::name).toList();

        // now let's enforce an error here at musician number 3
        AtomicInteger countDownToError = new AtomicInteger(3);

        List<Musician> musicians = musicianNames.stream()
                .peek(_ -> randomPause(100, 1_000)) //  it takes a while to wake up each musician
//                .peek(_ -> {
//                    countDownToError.decrementAndGet();
//                    if (countDownToError.get() == 0) {
//                        String errorText = "Boom! Error while working on waking up all musicians!";
//                        System.err.println(errorText);
//                        throw new RuntimeException(errorText);
//                    }
//                })
                .map(Musician::new)
                .peek(musician -> System.err.println("\tMusician " + musician.name() + " woke up ..."))
                .collect(Collectors.toList());

        logWithTime("Task: Waking up all musicians ... done");

        return musicians;
    }
}