package pl.umcs.oop;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicIntegerArray;


//implements runnable pozwala przekazac obiekt do watku i uruchomic
public class HistogramWorker implements Runnable {

    private int begin, end;
    private BufferedImage image; //obraz do histogramu
    private int channel; // 0 - Red, 1 - Green, 2 - Blue
    private AtomicIntegerArray histogram; //tablica liczb do histogramu

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
                //zwieksza wartosc histogramu
                histogram.incrementAndGet(value);
            }
        }
    }
}

