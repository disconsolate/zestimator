package ir.hossein_shemshadi.spreadsheet;

import ir.hossein_shemshadi.annotations.ExcelCell;
import ir.hossein_shemshadi.annotations.ExcelRecord;
import ir.hossein_shemshadi.exceptions.CanNotConvertToExcelRecordException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Component
public class SpreadsheetWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpreadsheetWriter.class);
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    @Value("${spreadsheet.file.path}")
    private String filePath;
    @Value("${spreadsheet.file.name}")
    private String fileName;
    @Value("${spreadsheet.file.sheet.name}")
    private String sheetName;

    private void initiateExcelFile(List<String> headers) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            row.createCell(i).setCellValue(headers.get(i));
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(filePath + fileName), false);
            workbook.write(out);
            out.close();
            LOGGER.debug("Writing to " + fileName + " was completed successfully :)");
        } catch (Exception e) {
            LOGGER.error("Exception occurred while writing to spreadsheet file " + fileName, e);
        }
    }

    private boolean appendData(Map<Integer, Object> data) {
        try {
            InputStream input = new FileInputStream(filePath + fileName);
            workbook = new XSSFWorkbook(input);
            sheet = workbook.getSheetAt(0);
            int lastRowNumber = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNumber + 1);
            List indices = Arrays.asList(data.keySet().toArray());
            List values = new ArrayList(data.values());
            for (int i = 0; i < indices.size(); i++) {
                row.createCell((Integer) indices.get(i)).setCellValue(values.get(i).toString());
            }
            FileOutputStream fileOut = new FileOutputStream(filePath + fileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("file with name " + fileName + " not found!", e);
            return false;
        } catch (IOException e) {
            LOGGER.error("Exception occurred while writing to spreadsheet file " + fileName, e);
            return false;
        }
        return true;
    }

    private List<String> getHeaders(Class clazz) throws CanNotConvertToExcelRecordException {
        if (clazz.getAnnotation(ExcelRecord.class) == null) {
            LOGGER.error(clazz + " is not an excel record!!!");
            throw new CanNotConvertToExcelRecordException(clazz + " is not an excel record!!!");
        }
        List<String> headers = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            String headerName = field.getName();
            int headerIndex = field.getAnnotation(ExcelCell.class).number() - 1;
            headers.add(headerIndex, headerName);
        }
        return headers;
    }

    private Map<Integer, Object> getObjectFields(Object object) {
        Class<? extends Object> clazz = object.getClass();
        Map<Integer, Object> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                int cellIndex = fields[i].getAnnotation(ExcelCell.class).number() - 1;
                if (!Modifier.isPublic(fields[i].getModifiers())) {
                    fields[i].setAccessible(true);
                }
                Object value = fields[i].get(object);
                map.put(cellIndex, value);
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("Exception on reading field value", e);
        }
        return map;
    }

    public boolean persist(Object object) throws CanNotConvertToExcelRecordException {
        if (workbook == null || sheet == null) {
            initiateExcelFile(getHeaders(object.getClass()));
        }
        return appendData(getObjectFields(object));
    }
}
