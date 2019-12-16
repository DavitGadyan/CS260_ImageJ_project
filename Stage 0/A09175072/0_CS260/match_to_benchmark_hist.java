import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;
import ij.IJ;





public class match_to_benchmark_hist implements PlugInFilter {
       public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }

    int[] matchHistograms (int[] hA, int[] hR) {
        // hA . . . histogram hA of the target image IA (to be modified)
        // hR . . . reference histogram hR
        // returns the mapping fhs() to be applied to image IA

        int K = hA.length;
        double[] PA = Cdf(hA); // get CDF of histogram hA
        double[] PR = Cdf(hR); // get CDF of histogram hR
        int[] fhs = new int[K]; // mapping fhs()

        // compute mapping function fhs():
        for (int a = 0; a < K; a++) {
            int j = K - 1;
            do {
                fhs[a] = j;
                j--;
            } while (j >= 0 && PA[a] <= PR[j]);
        }
        return fhs;
    }




    double[] Cdf (int[] h) {
        // returns the cumul. distribution function for histogram h
        int K = h.length;

        int n = 0; // sum all histogram values
        for (int i = 0; i < K; i++) {
            n += h[i];
        }

        double[] P = new double[K]; // create CDF table P
        int c = h[0]; // cumulate histogram values
        P[0] = (double) c / n;
        for (int i = 1; i < K; i++) {
            c += h[i];
            P[i] = (double) c / n;
        }
        return P;
    }

    public void run(ImageProcessor ipA) {
        // get benchmark image
        ImagePlus impR = IJ.openImage("https://i.ibb.co/FXrtCRk/165-11-cropped.png");
        
        IJ.log("Benchmark image loaded successfully!!!" );
        impR.show();
        ImageProcessor ipR = impR.getProcessor();


        // int width = ip1.getWidth();
        // int height = ip1.getHeight();
        // for (int row = 0; row < height; row++)
        //     for (int col = 0; col < width; col++) {
        //         int color = new Color(ip.getPixel(col, row)), pixel, r, g, b;
        //         r = color.getRed();
        //         g = color.getGreen();
        //         b = color.getBlue();
        //     }
        
        // ip2


        int[] hA = ipA.getHistogram(); // get histogram for IA
        int[] hR = ipR.getHistogram(); // get histogram for IR
        int[] fhs = matchHistograms(hA, hR); // mapping function fhs(a)
        ipA.applyTable(fhs); // modify the target image IA


        // int width = ip.getWidth(), height = ip.getHeight(), pixel, r, g, b;
        // double rb;
        // Color color;
    }
}