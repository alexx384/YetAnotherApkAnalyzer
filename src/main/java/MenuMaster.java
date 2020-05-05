import extract.PropertiesExtractor;
import org.apache.commons.cli.*;

public class MenuMaster {

    private static final String PROGRAM_NAME = System.getProperty("sun.java.command").split(" ")[0];
    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_ERROR = -1;
    private static CommandLineParser cmdParser = new DefaultParser();
    private static HelpFormatter formatter = new HelpFormatter();
    private static Options cmdOptions = new Options();

    static {
        Option help = Option.builder("help")
                .desc("Prints current help message")
                .build();
        Option apkFilePath = Option.builder("a")
                .longOpt("apkFilePath")
                .hasArg()
                .argName("app.apk")
                .desc("set path to APK file or folder with APK files (required)")
                .build();
        Option resultFilePath = Option.builder("r")
                .longOpt("resultFilePath")
                .hasArg()
                .argName("result.csv")
                .desc("set Ip address and port of MobSf server")
                .build();
        Option mobsfAddress = Option.builder("m")
                .longOpt("mobsfAddress")
                .hasArg()
                .argName("ip:port")
                .desc("set Ip address and port of MobSf server (required)")
                .build();
        Option mobsfApiKey = Option.builder("k")
                .longOpt("mobsfApiKey")
                .hasArg()
                .argName("apiKey")
                .desc("set REST api key of MobSF (required)")
                .build();
        Option pythonPath = Option.builder("p")
                .longOpt("pythonPath")
                .hasArg()
                .argName("venv/bin/python")
                .desc("path to python interpreter")
                .build();
        Option androwarnPath = Option.builder("w")
                .longOpt("androwarnPath")
                .hasArg()
                .argName("androwarn/androwarn.py")
                .desc("path to Androwarn framework startup file")
                .build();
        Option notDeleteCache = Option.builder()
                .longOpt("notDeleteCache")
                .desc("do not delete cache after successful parameters extraction")
                .build();

        cmdOptions.addOption(help);
        cmdOptions.addOption(apkFilePath);
        cmdOptions.addOption(resultFilePath);
        cmdOptions.addOption(mobsfAddress);
        cmdOptions.addOption(mobsfApiKey);
        cmdOptions.addOption(pythonPath);
        cmdOptions.addOption(androwarnPath);
        cmdOptions.addOption(notDeleteCache);
    }

    public static int processArgs(String[] args) throws ParseException {

        CommandLine cmd = cmdParser.parse(cmdOptions, args);
        if (cmd.hasOption("help")) {
            formatter.printHelp(PROGRAM_NAME, cmdOptions, true);
        } else {
            String apkFilePath = cmd.getOptionValue("apkFilePath");
            String resultFilePath = cmd.getOptionValue("resultFilePath");
            String mobsfAddress = cmd.getOptionValue("mobsfAddress");
            String mobsfApiKey = cmd.getOptionValue("mobsfApiKey");
            String pythonPath = cmd.getOptionValue("pythonPath");
            String androwarnPath = cmd.getOptionValue("androwarnPath");
            boolean isNotDeleteCache = cmd.hasOption("notDeleteCache");

            if (apkFilePath == null || mobsfAddress == null || mobsfApiKey == null) {
                formatter.printHelp(PROGRAM_NAME, cmdOptions, true);
            } else {
                PropertiesExtractor extractor = PropertiesExtractor.build(mobsfAddress, mobsfApiKey, pythonPath,
                        androwarnPath);
                if (extractor == null) {
                    return EXIT_ERROR;
                }
                if (!extractor.extract(apkFilePath, resultFilePath, isNotDeleteCache)) {
                    return EXIT_ERROR;
                }
            }
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
