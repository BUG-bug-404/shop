package com.core.util;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import javax.swing.*;
import java.awt.*;

public class TimeChart {


    private static double btime_Heap=15.0;
    private static double btime_Shell=10.0;
    private static double btime_Bubble=11.0;
    private static double btime_Quick;
    private static double btime_Radix;
    private static double btime_Simple;
    private static double wtime_Heap;
    private static double wtime_Simple;
    private static double wtime_Radix;
    private static double wtime_Quick;
    private static double wtime_Bubble;
    private static double wtime_Shell;

    static class  chart{
        // 步骤1：创建CategoryDataset对象（准备数据）
        CategoryDataset dataset = createDataset();
      //  CategoryDataset dataset1 = createDataset1();
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset);
        //JFreeChart freeChart1 = createChart1(dataset1);


        //根据CategoryDataset创建JFreeChart对象
        public static JFreeChart createChart(CategoryDataset categoryDateset){

            // 创建JFreeChart对象：ChartFactory.createLineChart
            JFreeChart jfreechart = ChartFactory.createLineChart("The Confrontation Of Sorts In Best Case", // 标题
                    "Sort By",         //categoryAxisLabel （category轴，横轴，X轴标签）
                    "Spent Time",      // valueAxisLabel（value轴，纵轴，Y轴的标签）
                    categoryDateset,  //Dataset
                    PlotOrientation.VERTICAL, false, // legend
                    false,          //Tooltips
                    false);        //URLs

            // 使用CategoryPlot设置各种参数。
            CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();

            // 背景色 透明度
            plot.setBackgroundAlpha(0.5f);

            // 前景色 透明度
            plot.setForegroundAlpha(1.0f);

            // 其他设置 参考 CategoryPlot类
            LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
            renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
            renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
            renderer.setUseSeriesOffset(true); // 设置偏移量
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setBaseItemLabelsVisible(true);
            return jfreechart;
        }

        public static CategoryDataset createDataset() {
            String[] rowKeys = {"Sort"};
            String[] colKeys = {"HeapSort","ShellSort","BubbleSort","QuickSort","RadixSort","SimpleSort"};

            double[][] data = {{TimeChart.btime_Heap,TimeChart.btime_Shell,TimeChart.btime_Bubble,TimeChart.btime_Quick,TimeChart.btime_Radix,TimeChart.btime_Simple},};
            //System.out.println(TimeChat.btime_Heap);
            return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);
        }

        //最坏情况下
        public static JFreeChart createChart1(CategoryDataset categoryDateset){

            // 创建JFreeChart对象：ChartFactory.createLineChart
            JFreeChart jfreechart = ChartFactory.createLineChart("The Confrontation Of Sorts In Worst Case", // 标题
                    "Sort By",           //categoryAxisLabel （category轴，横轴，X轴标签）
                    "Spent Time",       //valueAxisLabel（value轴，纵轴，Y轴的标签）
                    categoryDateset,    // Dataset
                    PlotOrientation.VERTICAL, false, // legend
                    false,            //Tooltips
                    false);          // URLs


            CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
            plot.setBackgroundAlpha(0.5f);
            plot.setForegroundAlpha(0.5f);
            LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
            renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
            renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
            renderer.setUseSeriesOffset(true); // 设置偏移量
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setBaseItemLabelsVisible(true);
            return jfreechart;
        }

        public static CategoryDataset createDataset1() {
            String[] rowKeys = {"Sort"};
            String[] colKeys = {"HeapSort","ShellSort","BubbleSort","QuickSort","RadixSort","SimpleSort"};

            double[][] data = {{TimeChart.wtime_Heap,TimeChart.wtime_Shell,TimeChart.wtime_Bubble,TimeChart.wtime_Quick,TimeChart.wtime_Radix,TimeChart.wtime_Simple},};
            //System.out.println(TimeChat.btime_Heap);
            return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);
        }

    }

    public static void main(String[] strings){
        TimeChart tc = new TimeChart(); //另一个类，初始化data[][]中的变量
        chart ct = new chart();
        ChartPanel chartf = new ChartPanel(ct.freeChart,true);
      //  ChartPanel chartf1 = new ChartPanel(ct.freeChart1,true);
        JFrame jf = new JFrame();
        jf.add(chartf, BorderLayout.WEST);
       // jf.add(chartf1,BorderLayout.EAST);
        jf.setVisible(true);
        jf.setSize(1400, 600);
        jf.setLocationRelativeTo(null);
    }
}
