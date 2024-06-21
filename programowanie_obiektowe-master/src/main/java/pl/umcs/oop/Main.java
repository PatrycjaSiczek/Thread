package pl.umcs.oop;


import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ImageProcessor ip = new ImageProcessor();


        try {
            long start1 = System.currentTimeMillis();
            ip.loadImage("image.png");
            ip.increaseBrightness(100);
            ip.saveImage("image2.png");
            long end1 = System.currentTimeMillis();


            long start2 = System.currentTimeMillis();
            ip.loadImage("image.png");
            ip.increaseBrightnessThreads(100);
            ip.saveImage("image2.png");
            long end2 = System.currentTimeMillis();
            //   System.out.println(end1 - start1);
            System.out.println(end2 - start2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //pierwsza metoda tworzenia watkow
        Thread thread1 = new Thread(() -> {
            //obliczenia
        });
        Thread thread2 = new Thread(() -> {
            //obliczenia
        });


        thread1.start();
        thread2.start();//zaczyna


        thread1.join();
        thread2.join(); //konczy


        //kolejna metoda tworzenia watkow
        ExecutorService executor = Executors.newFixedThreadPool(12);  //tworzenie nowej puli watkow


        executor.execute(() -> { //wykonanmie puli watkow
            //oblicxenia
        });
        executor.shutdown();//zakancza pule watkow
        if(executor.isTerminated()){ //jesli to prawda to wszystko sie zakonczylo pomyslnie


        }


        System.out.println(Runtime.getRuntime().availableProcessors());//zwraca liczbe procesorow dostepnych dla maszyny wirtulnej javy
        System.currentTimeMillis(); //zwraca czas kotry uplynal od 1970


    }
}


//histogram
//kanal to RGB - 3
// r(kanal czerwony)  0 -255- taki diagram tablica 255 el samych 0, liczymy ile jest oposzczegolnych np ile 0 ile 1 ile 2...
// tab[r]++;
// do zadania 6
// biblioteka xChart
// maven xChhart- kopiowac, na github xchart znalesc jak zbudowac wykres i dodajemy srie danych - nazwa i tablica elementow new swing raper



        package pl.umcs.oop;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class HistogramWorker implements Runnable {
    private int begin, end;
    private BufferedImage image;
    private int channel; // 0 - Red, 1 - Green, 2 - Blue
    private AtomicIntegerArray histogram;

    public HistogramWorker(int begin, int end, int channel, BufferedImage image, AtomicIntegerArray histogram) {
        this.begin = begin;
        this.end = end;
        this.image = image;
        this.channel = channel;
        this.histogram = histogram;
    }

    @Override
    public void run() {
        for (int y = begin; y < end; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int value = 0;
                switch (channel) {
                    case 0: // Red
                        value = (rgb >> 16) & 0xFF;
                        break;
                    case 1: // Green
                        value = (rgb >> 8) & 0xFF;
                        break;
                    case 2: // Blue
                        value = rgb & 0xFF;
                        break;
                }
                histogram.incrementAndGet(value);
            }
        }
    }
}


        package pl.umcs.oop;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;

public class HistogramCalculator {
    public static int[] calculateHistogram(BufferedImage image, int channel, int numThreads) throws InterruptedException, ExecutionException {
        int height = image.getHeight();
        AtomicIntegerArray histogram = new AtomicIntegerArray(256);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<?>> futures = new ArrayList<>();

        int chunkSize = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int begin = i * chunkSize;
            int end = (i == numThreads - 1) ? height : begin + chunkSize;
            HistogramWorker worker = new HistogramWorker(begin, end, channel, image, histogram);
            futures.add(executor.submit(worker));
        }

        for (Future<?> future : futures) {
            future.get(); // Czekaj na zakończenie wszystkich wątków
        }

        executor.shutdown();

        // Konwersja z AtomicIntegerArray do zwykłej tablicy int[]
        int[] result = new int[256];
        for (int i = 0; i < 256; i++) {
            result[i] = histogram.get(i);
        }

        return result;
    }
}


        package pl.umcs.oop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("path/to/image.jpg"));
            int numThreads = 4; // Na przykład
            int channel = 0; // 0 - Red, 1 - Green, 2 - Blue

            int[] histogram = HistogramCalculator.calculateHistogram(image, channel, numThreads);

            // Wypisz histogram
            for (int i = 0; i < histogram.length; i++) {
                System.out.println("Value " + i + ": " + histogram[i]);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

        package pl.umcs.oop;

import java.awt.*;
        import java.awt.image.BufferedImage;

public class HistogramPlotter {
    public static BufferedImage plotHistogram(int[] histogram, int width, int height) {
        BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = histogramImage.createGraphics();

        // Tło na biało
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Kolor dla wykresu
        g2d.setColor(Color.BLACK);

        // Znajdź maksymalną wartość histogramu do normalizacji wysokości słupków
        int max = 0;
        for (int value : histogram) {
            if (value > max) {
                max = value;
            }
        }

        // Rysowanie słupków histogramu
        int binWidth = width / histogram.length;
        for (int i = 0; i < histogram.length; i++) {
            int binHeight = (int) (((double) histogram[i] / max) * height);
            g2d.fillRect(i * binWidth, height - binHeight, binWidth, binHeight);
        }

        g2d.dispose();
        return histogramImage;
    }
}



public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("path/to/image.jpg"));
            int numThreads = 4; // Na przykład
            int channel = 0; // 0 - Red, 1 - Green, 2 - Blue

            int[] histogram = HistogramCalculator.calculateHistogram(image, channel, numThreads);

            // Generowanie wykresu histogramu
            int width = 800;
            int height = 600;
            BufferedImage histogramImage = HistogramPlotter.plotHistogram(histogram, width, height);

            // Zapisanie obrazu wykresu histogramu
            ImageIO.write(histogramImage, "png", new File("path/to/histogram.png"));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
