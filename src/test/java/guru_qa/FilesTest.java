package guru_qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilesTest {
    private ClassLoader cl = FilesTest.class.getClassLoader();

    @DisplayName("Проверка на содержание необходимых файлов")
    @Test
    void verifyFilesInZip() throws Exception {

        cl.getResourceAsStream("For qa_guru.zip");
        {
            {
                ZipEntry entry;
                List<String> expectedFiles = List.of("csv", "pdf", "xlsx");
                List<String> expectedExtensions = List.of("csv", "pdf", "xlsx");


                assertEquals(expectedFiles, expectedExtensions);
            }
        }
    }
    @DisplayName("Проверка на содержание необходимых файлов в архиве ")
    @Test
    void verifyFilesInZip2() throws IOException {
        List<String> expectedExtensions = List.of("csv", "pdf", "xlsx");
        Set<String> foundExtensions = new HashSet<>();

        try (InputStream inputStream = cl.getResourceAsStream("For qa_guru.zip")) {
            if (inputStream == null) {
                throw new FileNotFoundException("ZIP archive not found!");
            }
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.isDirectory())
                    {
                        String fileName = entry.getName();
                        String extension = FilenameUtils.getExtension(fileName);
                        if (expectedExtensions.contains(extension)) {
                            foundExtensions.add(extension);
                        }
                    }
                }
            }
        }

        assertTrue(foundExtensions.containsAll(expectedExtensions),
                "Archive doesn't contain all expected files (csv, pdf, xlsx)");
    }

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