import extract.ApkExtractor;
import org.apache.commons.cli.*;

public class MenuMaster {

    private static final String PROGRAM_NAME = System.getProperty("sun.java.command").split(" ")[0];
    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_ERROR = -1;
    private static CommandLineParser cmdParser = new DefaultParser();
    private static HelpFormatter formatter = new HelpFormatter();
    private static Options cmdOptions = new Options();

    static {
        Option extract = Option.builder("extract")
                .hasArg()
                .argName("app.apk")
                .desc("Extract properties from APK file to result.csv")
                .build();
        Option reduce = Option.builder("reduce")
                .hasArg()
                .argName("result.csv")
                .desc("Reduce properties of APK file in CSV file")
                .build();
        Option help = Option.builder("help")
                .desc("Prints current help message")
                .build();

        cmdOptions.addOption(extract);
        cmdOptions.addOption(reduce);
        cmdOptions.addOption(help);
    }

    public static int processArgs(String[] args) throws ParseException {

        CommandLine cmd = cmdParser.parse(cmdOptions, args);
        if (cmd.hasOption("extract")) {

            String extractedArg = cmd.getOptionValue("extract");

            if (extractedArg == null) {
                formatter.printHelp(PROGRAM_NAME, cmdOptions, true);
            }
            boolean result = new ApkExtractor(extractedArg).extract();
            if (!result) {

                return EXIT_ERROR;
            }

        } else if (cmd.hasOption("reduce")) {

            System.out.println("reduce");
        } else {

            formatter.printHelp(PROGRAM_NAME, cmdOptions, true);
        }

        return EXIT_SUCCESS;
    }

    public static void main(String[] args) {

        try {

            System.exit(
                    MenuMaster.processArgs(args)
            );
        } catch (ParseException pe) {

            pe.printStackTrace();
            System.exit(EXIT_ERROR);
        }
    }
}
