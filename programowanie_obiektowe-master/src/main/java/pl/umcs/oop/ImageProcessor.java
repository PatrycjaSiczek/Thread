package pl.umcs.oop;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageProcessor {
    private BufferedImage image;
    public void loadImage(String path) throws IOException {
        File file = new File(path); //referencja na file tworzy nowy plik ze sciezka do pliku
        image = ImageIO.read(file); //odczyt i zapis pliku
    }
    //  image.getWidth()
    //  int pixel = image.getRGB(0,0);
    //  image.setRGB(0,0,20);




    public void saveImage(String path) throws IOException {
        File file = new File(path); //zwraca sciezke do pliku
        ImageIO.write(image, "png", file);
    }


    public void increaseBrightness(int factor) {
        for (int x = 0; x < image.getHeight(); x++) {
            for(int y = 0 ; y < image.getWidth() ; y++) {
                int pixel = image.getRGB(x, y);
                pixel = brightenPixel(pixel, factor);
                image.setRGB(x, y, pixel);
            }
        }
    }


    private int brightenPixel(int pixel, int factor){
        int mask = 255;
        int blue = pixel & mask;
        int green = (pixel >> 8) & mask;
        int red = (pixel >> 16) & mask;
        blue = brightenPixelPart(blue, factor);
        green= brightenPixelPart(red, factor);
        red = brightenPixelPart(red, factor);
        return blue + (green << 8) + (red << 16);
    }


    private int brightenPixelPart(int color, int factor){
        color += factor;
        if(color > 255) {
            return 255;
        } else {
            return color;
        }
    }


    public void increaseBrightnessThreads(int factor) throws InterruptedException {
        int availableProcessors = Runtime.getRuntime().availableProcessors(); // wbudowane do watku, pokazuje ile dostepne rdzeni
        Thread threads[] = new Thread[availableProcessors];
        for (int i = 0; i < threads.length; i++) {
            final int finalI = i;// finalne aby moglo byc w lambda
            threads[i] = new Thread(() -> {
                //  int start = image.getHeight() * finalI /  availableProcessors ;
                int start = image.getHeight() /  availableProcessors * finalI; //oblicza poczatkowy wiersz dla biezacego watku
                int end = start + image.getHeight() / availableProcessors;
                if (finalI == availableProcessors - 1) {
                    end = image.getHeight();
                }
                for (int x = start; x < end; x++) {
                    for (int y = 0; y < image.getWidth(); y++) {
                        int pixel = image.getRGB(x, y);
                        pixel = brightenPixel(pixel, factor);
                        image.setRGB(x, y, pixel);
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }

public void setBrightnessThreadPool (int brightness){
    int threadsCount = Runtime.getRuntime().availableProcessors(); //liczba rdzeni
    ExecutorService executor = Executors.newFixedThreadPool(threadsCount); //tworzenie

    for (int i = 0; i < image.getHeight(); ++i){
        final int y = i;
        executor.execute(() -> {
            for(int x=0; x<image.getWidth(); x++){
                int rgb = image.getRGB(x,y);
                int b=rgb&0xFF;
                int g=(rgb&0xFF00)>>8;
                int r=(rgb&0xFF0000)>>16;
                b=clamp(b+brightness,0,255); // zamist dodatkowych funkcji odrazu ogranicza od 0 do 255
                g=clamp(g+brightness,0,255);
                r=clamp(r+brightness,0,255);
                rgb=(r<<16)+(g<<8)+b;
                image.setRGB(x,y,rgb);
            }
        });
    }
    executor.shutdown();
    try {
        boolean b = executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}

}

/*
RGB(red,green,blue), maksymalnie 255 np 0,0,255- intensywny niebieski
hex to na szesnastkowe RGB
#0000FF zwiekszamy jasnosc o 20 czyli 14(w szesnastkowym) czyli #1414FF - bo nie moze byc wiecej jak F
#000000 czarny
#FFFFFF biały
P & 255 - odczytanie B
(P >> 8) & 255 - odczytanie G
(P >> 16) & 255 - odczytanie R
w = (r << 16) + (g << 8) + b;

*/
