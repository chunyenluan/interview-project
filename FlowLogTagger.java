import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FlowLogTagger {

    public static void main(String[] args) {

        // Assume args[0] is the path to the lookup CSV file
        // Assume args[1] is the path to the flow log file

        if (args.length < 2) {
            System.out.println("Usage: java FlowLogTagger <lookup file> <flow log file>");
            return;
        }

        String lookupFilePath = args[0];
        String flowLogFilePath = args[1];

        try {
            Map<String, String> lookupTable = readLookupTable(lookupFilePath);
            Map<String, Integer> tagCounts = new HashMap<>();
            Map<String, Integer> portProtocolCounts = new HashMap<>();
            int untaggedCount = 0;

            // Process the flow log file
            try (BufferedReader br = new BufferedReader(new FileReader(flowLogFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Parse the line to get dstport and protocol
                    // Assuming the line structure is known and can be split to extract these values
                    String[] fields = line.split("\\s+");  // Adjust the regex based on actual log format
                    String dstport = fields[3];  // Assume 4th field is dstport (adjust as needed)
                    String protocol = fields[4]; // Assume 5th field is protocol (adjust as needed)
                    // case insensitive
                    String key = dstport + "," + protocol.toLowerCase();

                    // Count port/protocol combination
                    portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);

                    // Lookup tag
                    if (lookupTable.containsKey(key)) {
                        String tag = lookupTable.get(key);
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    } else {
                        untaggedCount++;
                    }
                }
            }

            // Print the results
            System.out.println("Tag Counts:");
            tagCounts.forEach((tag, count) -> System.out.println(tag + "\t" + count));
            System.out.println("Untagged\t" + untaggedCount);

            System.out.println("\nPort/Protocol Combination Counts:");
            portProtocolCounts.forEach((key, count) -> {
                String[] parts = key.split(",");
                System.out.println(parts[0] + "\t" + parts[1] + "\t" + count);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> readLookupTable(String lookupFilePath) throws IOException {
        Map<String, String> lookupTable = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(lookupFilePath))) {
            String line;
            // create the while loop to keep tracking all the data in the lookup.csv
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    // it supposes to be 3 String for each line
                    // it supposes to be case insensitive
                    String key = parts[0] + "," + parts[1].toLowerCase();
                    // dstport,protocol will be the key
                    // tag will be the value
                    lookupTable.put(key, parts[2]);
                }
            }
        }
        return lookupTable;
    }
}

