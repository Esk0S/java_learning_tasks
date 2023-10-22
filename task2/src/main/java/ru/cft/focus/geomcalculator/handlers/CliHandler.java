package ru.cft.focus.geomcalculator.handlers;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CliHandler {
    private static final String CONSOLE_OPT_DESCRIPTION = "Output to console";
    private static final String FILE_OPT_DESCRIPTION = "Output to file";
    private static final String INPUT_OPT_DESCRIPTION = "Path to input data";
    private static final String HELP_OPT_DESCRIPTION = "Display available options";
    private static final String INPUT_OPT = "i";
    private static final String FILE_OPT = "f";
    private static final String CONSOLE_OPT = "c";
    private static final String HELP_OPT = "h";
    private static final String HELP_OPT_FULL = "help";
    private static final String APPLICATION_NAME = "geom-calculator";
    private static final Logger logger = LogManager.getLogger(CliHandler.class);
    private final Options options = new Options();
    private String inputPath = "";
    private boolean fileOutput;

    public CliHandler(String[] args) {
        parseArgsFromCli(args);
    }

    private void parseArgsFromCli(String[] args) {
        CommandLineParser parser = new DefaultParser();

        options.addOption(addInputOption())
                .addOption(addHelpOption())
                .addOptionGroup(addOutputOptions());

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(INPUT_OPT)) {
                inputPath = cmd.getOptionValue(INPUT_OPT);
            }
            if (cmd.hasOption(FILE_OPT)) {
                fileOutput = true;
            }
            if (cmd.hasOption(HELP_OPT)) {
                printHelp();
            }

        } catch (ParseException exp) {
            logger.error("Argument parsing error. Reason: {}", exp.getMessage());
            printHelp();
            System.exit(0);
        }

    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("USAGE: " + APPLICATION_NAME + " -c | -f  -i <arg>");
        formatter.printHelp(" ", options, false);

    }

    private Option addHelpOption() {
        return new Option(HELP_OPT, HELP_OPT_FULL, false, HELP_OPT_DESCRIPTION);
    }

    private Option addInputOption() {
        Option inputOpt = new Option(INPUT_OPT, true, INPUT_OPT_DESCRIPTION);
        inputOpt.setRequired(true);

        return inputOpt;
    }

    private OptionGroup addOutputOptions() {
        Option consoleOpt = new Option(CONSOLE_OPT, false, CONSOLE_OPT_DESCRIPTION);
        Option fileOpt = new Option(FILE_OPT, false, FILE_OPT_DESCRIPTION);

        OptionGroup outputGroup = new OptionGroup();
        outputGroup.addOption(consoleOpt)
                .addOption(fileOpt)
                .setRequired(true);

        return outputGroup;
    }

    public String getInputPath() {
        return inputPath;
    }

    public boolean isFileOutput() {
        return fileOutput;
    }
}
