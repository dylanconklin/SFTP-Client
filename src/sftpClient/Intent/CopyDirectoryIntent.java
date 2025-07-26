package sftpClient.Intent;
import java.util.logging.Logger;
import sftpClient.Client.Client;
import java.util.ArrayList;


public class CopyDirectoryIntent extends Intent {
    private static final Logger logger = Logger.getLogger(CopyDirectoryIntent.class.getName());

    @Override
    public void parse(ArrayList<String> args) {
        //System.out.println(" Raw args before parse: " + args);
        if (args.get(0).equalsIgnoreCase("cpdir")) {
            args.remove(0);
        }
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Usage: cpdir <sourceDir> [targetDir]");
        } else if (args.size() == 1) {
            String src = args.get(0);
            String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 100000); // shorter random suffix
            String dest = src + "_" + uniqueSuffix;
            args.add(dest);  // set target to something like Documents_12345
        }
    }

    @Override
    public ArrayList<String> execute(Client client, ArrayList<String> args) {
        //System.out.println(" Raw args before parse: " + args);
        parse(args); // modifies args if necessary
        //System.out.println(" Raw args after parse: " + args);

        String src = args.get(0);
        String dest = args.get(1);

        ArrayList<String> result = new ArrayList<>();
        try {
            client.copyDirectory(src, dest);
            result.add("Success: Copied directory from `" + src + "` to `" + dest + "`");
            logger.info("Success: Copied directory from `" + src + "` to `" + dest + "`");
        } catch (Exception e) {
            result.add("Failed: " + e.getMessage());
            logger.warning("Failed: " + e.getMessage());
        }
        return result;
    }
}
