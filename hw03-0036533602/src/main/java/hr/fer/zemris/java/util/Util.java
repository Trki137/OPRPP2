package hr.fer.zemris.java.util;

import hr.fer.zemris.java.problem7.Band;
import hr.fer.zemris.java.problem7.BandVotes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Util {

    public static Workbook createTableWorkbook(int a, int b, int n){
        XSSFWorkbook workbook = new XSSFWorkbook();
        for(int i = 1; i <= n; i++) {
            Sheet sheet = workbook.createSheet(String.valueOf(i));
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
            headerCell.setCellValue("number");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("power");
            headerCell.setCellStyle(headerStyle);

            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            int rowCounter = 2;

            int aCopy = a;
            while (aCopy <= b) {
                Row row = sheet.createRow(rowCounter++);

                Cell cell = row.createCell(0);
                cell.setCellValue(aCopy);
                cell.setCellStyle(style);

                cell = row.createCell(1);
                cell.setCellValue((int) Math.pow(aCopy++, i));
                cell.setCellStyle(style);
            }
        }


        return workbook;
    }

    public static Band parseBand(String bandRecord){
        int httpIndex = bandRecord.indexOf("http");

        String IdAndName = bandRecord.substring(0, httpIndex);
        String ytLink = bandRecord.substring(httpIndex).trim();

        String id = IdAndName.substring(0, IdAndName.indexOf(' ')).trim();
        String bandName = IdAndName.substring(IdAndName.indexOf(' ')+1).trim();

        return new Band(Integer.parseInt(id),ytLink,bandName);
    }

    public static List<BandVotes> getResults(String bandsPathString, String resultsPath) throws IOException {
        List<Band> bands = Util.getBands(bandsPathString);

        List<String> results = Files.readAllLines(Path.of(resultsPath));

        List<BandVotes> votes = new ArrayList<>();

        for(String line: results){
            String[] split = line.split("\t");
            int id = Integer.parseInt(split[0]);
            int numOfVotes = Integer.parseInt(split[1]);

            Optional<Band> band = bands.stream().filter(b -> b.getId() == id).findFirst();
            if(band.isEmpty())
                throw new IllegalStateException();

            votes.add(new BandVotes(band.get().getBandName(),numOfVotes));
        }

        return votes;
    }

    public static List<Band> getBands(String bandsPathString) throws IOException {
        List<String> fileRecords = Files.readAllLines(Path.of(bandsPathString));
        List<Band> bands = new ArrayList<>();
        for (String bandString : fileRecords){
            bands.add(Util.parseBand(bandString));
        }

        return bands;
    }

    public static Workbook createBandWorkbook(List<BandVotes> votes) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Band");
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
        headerCell.setCellValue("Band");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("votes");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int rowCounter = 2;

        for(BandVotes bandVotes: votes){
            Row row = sheet.createRow(rowCounter++);

            Cell cell = row.createCell(0);
            cell.setCellValue(bandVotes.getBandName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(bandVotes.getVotes());
            cell.setCellStyle(style);
        }

        return workbook;
    }
}
