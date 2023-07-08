package hr.fer.zemris.java.problem4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PieChart extends JFrame {

    private final JFreeChart chart;
    public PieChart(String chartTitle, Map<String, Integer> data){
        PieDataset<String> dataset = createDataset(data);

        chart = createChart(dataset,chartTitle);
    }

    public JFreeChart getChart() {
        return chart;
    }

    private JFreeChart createChart(PieDataset<String> dataset, String chartTitle) {
        JFreeChart chart = ChartFactory.createPieChart3D(
                chartTitle,
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }

    private PieDataset<String> createDataset(Map<String, Integer> data) {
        DefaultPieDataset<String> result = new DefaultPieDataset<>();

        for(var entrySet : data.entrySet()) {
            result.setValue(entrySet.getKey(), entrySet.getValue());
        }
        return result;

    }

}
