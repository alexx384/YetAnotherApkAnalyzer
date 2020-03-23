import org.apache.commons.cli.*;

public class MenuMaster {

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

    public static void processArgs(String[] args) throws ParseException {

        CommandLine cmd = cmdParser.parse(cmdOptions, args);
        if (cmd.hasOption("extract")) {

            System.out.println("extract");
        } else if (cmd.hasOption("reduce")) {

            System.out.println("reduce");
        } else {

            formatter.printHelp("Hello", cmdOptions, true);
        }
    }

    public static void main(String[] args) {

        try {

            MenuMaster.processArgs(args);
        } catch (ParseException pe) {

            pe.printStackTrace();
        }
    }
}
