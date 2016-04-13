/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package c45.database;

/**
 *
 * @author Acer
 */


import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a pie chart using 
 * data from a {@link DefaultPieDataset}.
 */
public class idspiechart extends ApplicationFrame {

    /**
     * Default constructor.
     *
     * @param title  the frame title.
     */
    public idspiechart(String title,int totalp,int attackp) {
        super(title);
        setContentPane(createDemoPanel(totalp,attackp));
    }

    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset(int totalp,int attackp) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Normal Packets", totalp);
        dataset.setValue("Attack Packets", attackp);
       // dataset.setValue("Three", new Double(27.5));
      //  dataset.setValue("Four", new Double(17.5));
      //  dataset.setValue("Five", new Double(11.0));
      //  dataset.setValue("Six", new Double(19.4));
        return dataset;        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Result of Captured Packets ",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel(int totalp,int attackp) {
        JFreeChart chart = createChart(createDataset(totalp,attackp));
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        idspiechart demo = new idspiechart(" ",100,5);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
    
    public static void showChart(int normalp,int attackp) {

        idspiechart demo = new idspiechart(" ",normalp,attackp);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}