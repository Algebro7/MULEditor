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
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MULEditor()).execute(args);
        System.exit(exitCode);
    }
}
