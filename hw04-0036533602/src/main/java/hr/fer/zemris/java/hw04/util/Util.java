package hr.fer.zemris.java.hw04.util;

import hr.fer.zemris.java.hw04.model.ExcelPollResultModel;
import hr.fer.zemris.java.hw04.model.PollOptionsModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

public class Util {

    public static JFreeChart createPieChart(ExcelPollResultModel model){
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for(PollOptionsModel optionsModel: model.getPollOptionsModelList()) {
            dataset.setValue(optionsModel.getOptionTitle(), optionsModel.getVotesCount());
        }

        JFreeChart chart = ChartFactory.createPieChart3D(
                model.getPollTitle(),
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
    public static Workbook createBandWorkbook(List<PollOptionsModel> result) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Option");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Votes");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int rowCounter = 2;

        for(PollOptionsModel pollOption: result){
            Row row = sheet.createRow(rowCounter++);

            Cell cell = row.createCell(0);
            cell.setCellValue(pollOption.getOptionTitle());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(pollOption.getVotesCount());
            cell.setCellStyle(style);
        }

        return workbook;
    }
}
