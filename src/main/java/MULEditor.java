import core.BVDiscrepancy;
import core.Client;
import core.CsvParser;
import core.MulUnit;
import list.UnitList;
import list.UnitListData;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "MULEditor", version = "MULEditor 1.0", mixinStandardHelpOptions = true)
public class MULEditor implements Callable<Integer> {

    @CommandLine.Option(names = { "-o", "--outfile" },
            description = "Filename to write MUL entries (default: ${DEFAULT-VALUE})",
            defaultValue = "mul.txt")
    String outfile;

    @CommandLine.Option(names = { "-i", "--infile" },
            description = "Path to saved MUL unit file")
    String infile = "";

    @CommandLine.Option(names = { "-s", "--ssw-path" },
            description = "Path to SSW-Master folder")
    String sswMasterPath = "";

    @CommandLine.Option(names = { "-b", "--bv-file" },
            description = "Path to save mismatched BVs (default: ${DEFAULT-VALUE})",
            defaultValue = "mismatched-bvs.csv")
    String bvOutfile;

    @Override
    public Integer call() {
        List<MulUnit> mulUnits;
        if (infile.isEmpty()) {
            try {
                mulUnits = downloadMul();
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        } else {
            try {
                mulUnits = CsvParser.LoadMulUnits(infile);
            } catch (IOException e) {
                e.printStackTrace();
                return 3;
            }
        }

        ArrayList<UnitListData> sswUnits = new UnitList(sswMasterPath, false).getList();
        int matchingNames = 0;
        int matchingBV = 0;

        List<BVDiscrepancy> discreps = new ArrayList<>();
        for (UnitListData sswUnit : sswUnits) {
            for (MulUnit mulUnit : mulUnits) {
                if (sswUnit.getFullName().equals(mulUnit.getName())) {
                    matchingNames++;
                    if (sswUnit.getBV() == mulUnit.getBV()) {
                        matchingBV++;
                    } else {
                        discreps.add(new BVDiscrepancy(mulUnit, sswUnit));
                    }
                    break;
                }
            }
        }
        System.out.println("Found " + matchingNames + " SSW units in the MUL based on name.");
        System.out.println("Of these, " + matchingBV + " agreed on BV.");
        System.out.println("Writing the mechs that didn't agree to " + bvOutfile + "...");
        try {
            CsvParser.WriteBVDiscrepancies(discreps, bvOutfile);
        } catch (IOException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

    private List<MulUnit> downloadMul() throws Exception {
        Client client = new Client();
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

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MULEditor()).execute(args);
        System.exit(exitCode);
    }
}
