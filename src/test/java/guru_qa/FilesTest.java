package guru_qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.InputStreamReader;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class FilesTest {
    private ClassLoader cl = FilesTest.class.getClassLoader();

    @DisplayName("Распаковка и проверка CVS из архива ZIP ")
    @Test
    void zipCvsFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("For qa_guru.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> csvContent = csvReader.readAll();
                    Assertions.assertArrayEquals(new String[]{"Abril;United States"}, csvContent.get(1));
                    break;

                }
            }
        }
    }

    @DisplayName("Распаковка и проверка Excel из архива ZIP ")
    @Test
    void zipExcelFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("For qa_guru.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(xls.excel.getSheetAt(0).getRow(13).getCell(0)
                            .getStringCellValue(), " Сменный фильтр Morelian 4PCS совместимый с Philips Electrolux Series FC9172 ");
                    break;
                }
            }
        }
    }

    @DisplayName("Распаковка и проверка PDF из архива ZIP ")
    @Test
    void zipPdfFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("For qa_guru.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.text).contains("На фасадном торце расположены петли для навески дверей.");
                    break;
                }
            }
        }
    }
}