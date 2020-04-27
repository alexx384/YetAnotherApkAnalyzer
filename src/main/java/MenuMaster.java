import extract.PropertiesExtractor;
import org.apache.commons.cli.*;
import write.PropertiesWriter;

public class MenuMaster {

    private static final String PROGRAM_NAME = System.getProperty("sun.java.command").split(" ")[0];
    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_ERROR = -1;
    private static CommandLineParser cmdParser = new DefaultParser();
    private static HelpFormatter formatter = new HelpFormatter();
    private static Options cmdOptions = new Options();

    static {
        Option extract = Option.builder("extract")
                .desc("Extract properties from APK file into CSV file")
                .build();
        Option reduce = Option.builder("reduce")
                .desc("Reduce properties of APK file in CSV file")
                .build();
        Option help = Option.builder("help")
                .desc("Prints current help message")
                .build();
        Option apkFilePath = Option.builder()
                .longOpt("apkFilePath")
                .hasArg()
                .argName("app.apk")
                .desc("set Ip address and port of MobSf server")
                .build();
        Option resultFilePath = Option.builder()
                .longOpt("resultFilePath")
                .hasArg()
                .argName("result.csv")
                .desc("set Ip address and port of MobSf server")
                .build();
        Option mobsfAddress = Option.builder()
                .longOpt("mobsfAddress")
                .hasArg()
                .argName("ip:port")
                .desc("set Ip address and port of MobSf server")
                .build();
        Option mobsfApiKey = Option.builder()
                .longOpt("mobsfApiKey")
                .hasArg()
                .argName("apiKey")
                .desc("set REST api key of MobSF")
                .build();
        Option pythonPath = Option.builder()
                .longOpt("pythonPath")
                .hasArg()
                .argName("venv/bin/python")
                .desc("path to python interpreter")
                .build();
        Option androwarnPath = Option.builder()
                .longOpt("androwarnPath")
                .hasArg()
                .argName("androwarn/androwarn.py")
                .desc("path to Androwarn framework startup file")
                .build();

        cmdOptions.addOption(extract);
        cmdOptions.addOption(reduce);
        cmdOptions.addOption(help);
        cmdOptions.addOption(apkFilePath);
        cmdOptions.addOption(resultFilePath);
        cmdOptions.addOption(mobsfAddress);
        cmdOptions.addOption(mobsfApiKey);
        cmdOptions.addOption(pythonPath);
        cmdOptions.addOption(androwarnPath);
    }

    public static int processArgs(String[] args) throws ParseException {

        CommandLine cmd = cmdParser.parse(cmdOptions, args);
        if (cmd.hasOption("extract")) {

            String apkFilePath = cmd.getOptionValue("apkFilePath");
            String resultFilePath = cmd.getOptionValue("resultFilePath");
            String mobsfAddress = cmd.getOptionValue("mobsfAddress");
            String mobsfApiKey = cmd.getOptionValue("mobsfApiKey");
            String pythonPath = cmd.getOptionValue("pythonPath");
            String androwarnPath = cmd.getOptionValue("androwarnPath");

            PropertiesWriter writer;
            if (resultFilePath == null) {
                writer = PropertiesWriter.build();
            } else {
                writer = PropertiesWriter.build(resultFilePath);
            }
            if (writer == null) {
                return EXIT_ERROR;
            }

            boolean result = new PropertiesExtractor(
                    apkFilePath, writer, mobsfAddress, mobsfApiKey, pythonPath, androwarnPath
            ).extract();
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
