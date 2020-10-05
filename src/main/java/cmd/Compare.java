package cmd;

import core.*;
import list.UnitListData;
import megamek.common.MechSummary;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "compare")
public class Compare implements Callable<Integer> {

    @CommandLine.Option(names = {"-m", "--megamek-cache-file"},
            description = "Path to megamek units.cache file")
    String mmCacheFile;

    @CommandLine.Option(names = {"-o", "--mul-out"},
            description = "Filename to write MUL entries (default: ${DEFAULT-VALUE})",
            defaultValue = "mul.txt")
    String outfile;

    @CommandLine.Option(names = {"-i", "--mul-in"},
            description = "Path to saved MUL unit file")
    String infile = "";

    @CommandLine.Option(names = {"-s", "--ssw-path"},
            description = "Path to SSW-Master folder")
    String sswMasterPath = "";

    @CommandLine.Option(names = {"-b", "--bv-file"},
            description = "Path to save mismatched BVs (default: ${DEFAULT-VALUE})",
            defaultValue = "mismatched-bvs.csv")
    String bvOutfile;

    @CommandLine.Option(names = {"--mul"},
            description = "Compare with MUL (default: false)")
    boolean useMul;

    boolean useMml = false;

    List<MulUnit> mulUnits = new ArrayList<>();
    List<UnitListData> sswUnits = new ArrayList<>();
    List<MechSummary> mmUnits = new ArrayList<>();
    List<BVDiscrepancy> discreps = new ArrayList<>();

    @Override
    public Integer call() {
        if (!mmCacheFile.isEmpty()) {
            useMml = true;
        }
        if (!useMml && !useMul) {
            System.out.println("Error: not enough options. You must specify at least one source other than SSW to compare to.");
            return -1;
        }
        int result = loadUnits();
        if (result != 0) {
            return result;
        }
        result = compare();
        if (result != 0) {
            return result;
        }
        result = writeDiscrepancies();
        return result;
    }

    private int loadUnits() {
        if (useMul) {
            try {
                mulUnits = downloadMul();
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        }
        if (useMml) {
            MegamekLoader loader = new MegamekLoader();
            System.out.println("Loading Megamek units from " + mmCacheFile + "...");
            try {
                mmUnits = loader.load(mmCacheFile);
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        }

        SSWLoader loader = new SSWLoader();
        System.out.println("Loading SSW units from " + sswMasterPath + "...");
        try {
            sswUnits = loader.load(sswMasterPath);
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

    private int compare() {
        int matchingBV = 0;
        for (UnitListData sswUnit : sswUnits) {
            Optional<MulUnit> mulUnit = findMulUnit(sswUnit);
            if (mulUnit.isPresent()) {
                if (sswUnit.getBV() == mulUnit.get().getBV()) {
                    matchingBV++;
                } else { // BV Discrepancy
                    if (useMml) {
                        Optional<MechSummary> mmUnit = findMMUnit(sswUnit);
                        if (mmUnit.isPresent()) { // If there is a matching MM unit found, add it
                            discreps.add(new BVDiscrepancy(mulUnit.get(), sswUnit, mmUnit.get()));
                            break;
                        }
                    }
                    // Otherwise, just use the MUL and SSW (MM BV will be recorded as 0)
                    discreps.add(new BVDiscrepancy(mulUnit.get(), sswUnit));
                }
            }
        }
        System.out.println(matchingBV + " mechs found in MUL with matching BVs.");
        System.out.println(discreps.size() + " mechs found with mismatching BVs.");
        return 0;
    }

    private Optional<MulUnit> findMulUnit(UnitListData sswUnit) {
        for (MulUnit mulUnit : mulUnits) {
            if (sswUnit.getFullName().equals(mulUnit.getName())) {
                return Optional.of(mulUnit);
            }
        }
        return Optional.empty();
    }

    private Optional<MechSummary> findMMUnit(UnitListData sswUnit) {
        for (MechSummary mmUnit : mmUnits) {
            if (mmUnit.getName().equals(sswUnit.getFullName())) {
                return Optional.of(mmUnit);
            }
        }
        return Optional.empty();
    }

    private List<MulUnit> downloadMul() throws Exception {
        MULClient client = new MULClient();
        List<MulUnit> results;
        System.out.println("Searching MUL, please wait...");
        results = client.searchMUL();
        if (results == null) {
            throw new Exception("No MUL results found");
        }
        System.out.println(results.size() + " units found. Writing to file " + outfile + "...");
        int numWritten = CsvParser.WriteMulUnits(results, outfile);
        System.out.println(numWritten + " units written to " + outfile + ".");
        return results;
    }

    private int writeDiscrepancies() {
        try {
            CsvParser.WriteBVDiscrepancies(discreps, bvOutfile);
        } catch (IOException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }
}
