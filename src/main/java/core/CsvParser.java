package core;

import list.UnitListData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public static List<MulUnit> LoadMulUnits(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<MulUnit> units = new ArrayList<>();
        CSVParser parser = CSVParser.parse(br, CSVFormat.DEFAULT);
        for (CSVRecord record : parser) {
            List<String> values = new ArrayList<>();
            for (int i = 0; i < record.size(); i++) {
                values.add(record.get(i));
            }
            units.add(new MulUnit(values));
        }
        br.close();
        return units;
    }

    public static int WriteMulUnits(List<MulUnit> unitList, String filename) throws IOException {
        int unitsWritten = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        CSVPrinter printer = new CSVPrinter(bw, CSVFormat.DEFAULT);
        for (MulUnit unit : unitList) {
            printer.printRecord(unit.getFields());
            unitsWritten++;
        }
        bw.flush();
        bw.close();
        return unitsWritten;
    }

    public static int WriteBVDiscrepancies(List<BVDiscrepancy> discreps, String filename) throws IOException {
        int unitsWritten = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        CSVPrinter printer = new CSVPrinter(bw, CSVFormat.DEFAULT.withHeader("Name", "MUL BV", "SSW BV", "Unit Type", "URL", "Intro Year"));
        for (BVDiscrepancy d : discreps) {
            printer.printRecord(d.getFields());
            unitsWritten++;
        }
        bw.flush();
        bw.close();
        return unitsWritten;
    }
}
