import cmd.*;
import core.*;
import megamek.common.MechSummary;
import picocli.CommandLine;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "MULEditor", version = "MULEditor 1.0", mixinStandardHelpOptions = true,
    subcommands = { Compare.class })
public class MULEditor implements Callable<Integer> {
    @Override
    public Integer call() {
//        List<MulUnit> mulUnits;
//        if (infile.isEmpty()) {
//            try {
//                mulUnits = downloadMul();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return 1;
//            }
//        } else {
//            try {
//                mulUnits = CsvParser.LoadMulUnits(infile);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return 3;
//            }
//        }
//
//        ArrayList<UnitListData> sswUnits = new UnitList(sswMasterPath, false).getList();
//        int matchingNames = 0;
//        int matchingBV = 0;
//
//        List<BVDiscrepancy> discreps = new ArrayList<>();
//        for (UnitListData sswUnit : sswUnits) {
//            for (MulUnit mulUnit : mulUnits) {
//                if (sswUnit.getFullName().equals(mulUnit.getName())) {
//                    matchingNames++;
//                    if (sswUnit.getBV() == mulUnit.getBV()) {
//                        matchingBV++;
//                    } else {
//                        discreps.add(new BVDiscrepancy(mulUnit, sswUnit));
//                    }
//                    break;
//                }
//            }
//        }
//        System.out.println("Found " + matchingNames + " SSW units in the MUL based on name.");
//        System.out.println("Of these, " + matchingBV + " agreed on BV.");
//        System.out.println("Writing the mechs that didn't agree to " + bvOutfile + "...");
//        try {
//            CsvParser.WriteBVDiscrepancies(discreps, bvOutfile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return 3;
//        }

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MULEditor()).execute(args);
        System.exit(exitCode);
    }
}
