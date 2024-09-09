package pl.umcs.oop;


import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class Main {
    public static void main(String[] args) throws InterruptedException {

        int[] histogram = new int[256];
//        for (int i = 0; i < 256; i++) {
//            histogram[i] = (int) (Math.random() * 500); // Losowe wartości do histogramu
//        }int[] histogram = new int[256];
////        for (int i = 0; i < 256; i++) {
////            histogram[i] = (int) (Math.random() * 500); // Losowe wartości do histogramu
////        }

        ImageProcessor ip = new ImageProcessor();
        try {

            //mierzy czas dla tej metody
            long start1 = System.currentTimeMillis();
            ip.loadImage("image.png"); //wczytuje obaz z pliku
            ip.increaseBrightness(100); //zwieksza jasnosc
            ip.saveImage("image2.png");  //zapisuje obraz do pliku
            long end1 = System.currentTimeMillis(); // pobiera czas po zakonczeniu pracy

            // mirzy czas dla drugiej metody
            long start2 = System.currentTimeMillis();
            ip.loadImage("image.png");
            ip.increaseBrightnessThreads(100);
            ip.saveImage("image2.png");
            long end2 = System.currentTimeMillis();

          //System.out.println(end1 - start1);
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

        try {
            //wczytuje obraz z podanej sciezki
            BufferedImage image = ImageIO.read(new File("path/to/image.jpg"));
            int numThreads = 4; // liczba watkow
            int channel = 0; // 0 - Red, 1 - Green, 2 - Blue

            //obliczanie  histogramu z uzyciem wielu watkow
            int[] histogram = HistogramCalculator.calculateHistogram(image, channel, numThreads);

            // Wypisz histogram wartosci do konsoli
            for (int i = 0; i < histogram.length; i++) {
                System.out.println("Value " + i + ": " + histogram[i]);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        try {
            BufferedImage image = ImageIO.read(new File("path/to/image.jpg"));
            int numThreads = 4; // watki
            int channel = 0; // 0 - Red, 1 - Green, 2 - Blue

            //oblicza hisogram z watkow
            int[] histogram = HistogramCalculator.calculateHistogram(image, channel, numThreads);

            // Generowanie wykresu histogramu
            int width = 800;
            int height = 600;

            //generuje histogram
            BufferedImage histogramImage = HistogramPlotter.plotHistogram(histogram, width, height);

            // Zapisanie obrazu wykresu histogramu
            ImageIO.write(histogramImage, "png", new File("path/to/histogram.png"));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        // Zapisanie obrazu
        handler.saveImage("obrazKopia2.jpg");

        handler.loadImage("obraz.jpg");

        // Obliczenie histogramu czerwonego kanału
        int[] redHistogram = handler.calculateChannelHistogram(3);

        // Wyświetlenie histogramu
        if (redHistogram != null) {
            for (int i = 0; i < redHistogram.length; i++) {
                System.out.println("Wartość: " + i + ", Liczba pikseli: " + redHistogram[i]);
            }
        }

        // Przykładowy histogram (dla celów demonstracyjnych)
        int[] histogram = new int[256];
        for (int i = 0; i < 256; i++) {
            histogram[i] = (int) (Math.random() * 500); // Losowe wartości do histogramu
        }

        //ImageHandler handler = new ImageHandler();
        handler.generateHistogramImage(histogram, "histogram.png");
    }
    }

}

//histogram
//kanal to RGB - 3
// r(kanal czerwony)  0 -255- taki diagram tablica 255 el samych 0, liczymy ile jest oposzczegolnych np ile 0 ile 1 ile 2...
// tab[r]++;
// do zadania 6
// biblioteka xChart
// maven xChhart- kopiowac, na github xchart znalesc jak zbudowac wykres i dodajemy srie danych - nazwa i tablica elementow new swing raper