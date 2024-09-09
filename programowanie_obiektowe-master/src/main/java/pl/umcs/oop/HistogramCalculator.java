package pl.umcs.oop;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;

//oblicza histogram dla danego kanalu RGB obrazu
public class HistogramCalculator {
    public static int[] calculateHistogram(BufferedImage image, int channel, int numThreads) throws InterruptedException, ExecutionException {
        int height = image.getHeight();

        //przechowuje hisogram, kotry bedzie modyfikowany przez watki
        AtomicIntegerArray histogram = new AtomicIntegerArray(256);

        //tworzy pule watkowm o okreslonej stalej watkow
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        //lista przchowujaca obiekty Future- zadania do wykonania
        List<Future<?>> futures = new ArrayList<>(); //moze byc Future<T>

        int chunkSize = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int begin = i * chunkSize;
            int end = (i == numThreads - 1) ? height : begin + chunkSize;

            //przetwarza czesci obrazu
            HistogramWorker worker = new HistogramWorker(begin, end, channel, image, histogram);

            //dodaje zadania do wykonania w puli watkow i zapisuje
            futures.add(executor.submit(worker));
        }

        for (Future<?> future : futures) {
            future.get(); // get Czeka na zakończenie wszystkich wątków
        }

        //zamyjka pule watkow
        executor.shutdown();

        // Konwersja z AtomicIntegerArray do zwykłej tablicy int[]
        int[] result = new int[256];
        for (int i = 0; i < 256; i++) {
            result[i] = histogram.get(i);
        }

        //zwraca wyniki histogramu
        return result;
    }
}