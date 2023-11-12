package org.java;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;

@Log4j2
public class CliHandler {
    private static final String ELEMS_OPT_DESCRIPTION = "Elements to calculate";
    private static final String HELP_OPT_DESCRIPTION = "Display available options";
    private static final String ELEMS_OPT = "n";
    private static final String HELP_OPT = "h";
    private static final String HELP_OPT_FULL = "help";
    private static final String APPLICATION_NAME = "Threads app";
    private final Options options = new Options();
    @Getter
    private int numElements;

    public CliHandler(String[] args) {
        parseArgsFromCli(args);
    }

    private void parseArgsFromCli(String[] args) {
        CommandLineParser parser = new DefaultParser();

        options.addOption(addElemsOption())
                .addOption(addHelpOption());

        try {
            CommandLine cmd = parser.parse(options, args);
            String optVal;
            if (cmd.hasOption(ELEMS_OPT)) {
                optVal = cmd.getOptionValue(ELEMS_OPT);
                numElements = Integer.parseInt(optVal);
            }
            if (cmd.hasOption(HELP_OPT)) {
                printHelp();
            }
        } catch (NumberFormatException | ParseException exp) {
            log.error("Argument parsing error. Reason: {}", exp.getMessage());
            printHelp();
            System.exit(0);
        }

    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("USAGE: " + APPLICATION_NAME + "-n <arg> -t <arg>");
        formatter.printHelp(" ", options, false);

    }

    private Option addHelpOption() {
        return new Option(HELP_OPT, HELP_OPT_FULL, false, HELP_OPT_DESCRIPTION);
    }

    private Option addElemsOption() {
        Option elemsOpt = new Option(ELEMS_OPT, true, ELEMS_OPT_DESCRIPTION);
        elemsOpt.setRequired(true);

        return elemsOpt;
    }

}
