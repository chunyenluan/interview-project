# Flow Log Tagger

## Overview

This Java program parses a file containing flow log data and maps each row to a tag based on a lookup table defined in a CSV file. It generates a report of the counts of matches for each tag and the counts for each port/protocol combination.

## Assumptions

1. The flow log format is consistent and allows extracting `dstport` and `protocol` using a simple split operation.
2. The lookup table contains unique `dstport,protocol` combinations.
3. The matches are case-insensitive for the protocol.
4. Any `dstport,protocol` combination that doesn't match the lookup table is counted as "Untagged".

## How to Compile and Run

1. Compile the program using the following command: javac FlowLogTagger.java

2. Run the program with the paths to the lookup table and the flow log file: java FlowLogTagger lookup.csv flowlog.txt

## Output

The program outputs two sections:
1. **Tag Counts:** Displays the number of times each tag is matched.
2. **Port/Protocol Combination Counts:** Displays the count of each `port/protocol` combination.

## Testing

- The program has been tested with small sample files containing known `dstport` and `protocol` combinations.
- Edge cases such as empty lines, missing fields, and case differences in protocol have been handled.

## Dependencies

- This program only uses standard Java libraries, so no additional dependencies are required.

