import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HistogramPlotter {

    public static BufferedImage plotHistogram(int[] histogram, int width, int height) {

        //tworzy nowy obraz o podanych wymiarach i RGB
        BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //cos do rysowania na obrazie
        Graphics2D g2d = histogramImage.createGraphics();

        // Tło na biało
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height); //caly obraz na bialo

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
        int binWidth = width / histogram.length;  //szerokosc slupka
        for (int i = 0; i < histogram.length; i++) {
            int binHeight = (int) (((double) histogram[i] / max) * height);

            //rysowanie prostokatow, slupkow na obrazie
            g2d.fillRect(i * binWidth, height - binHeight, binWidth, binHeight);
        }

        //zwalnia zasoby graficzne
        g2d.dispose();

        //zwraca obraz
        return histogramImage;
    }
}
