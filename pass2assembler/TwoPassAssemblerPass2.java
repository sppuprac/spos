import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TwoPassAssemblerPass2 {
    private Map<String, Integer> symtable = new HashMap<>();
    private Map<String, String> littable = new HashMap<>();
    private int locCounter = 0;

    public void pass2(String input_ic, String littableFile, String symtableFile) throws IOException {
        // Read the literal table into littable map
        BufferedReader litReader = new BufferedReader(new FileReader(littableFile));
        String litLine;

        while ((litLine = litReader.readLine()) != null) {
            String[] litTokens = litLine.split("\\s+");
            littable.put(litTokens[0], litTokens[1]);
        }

        litReader.close();

        // Read the symbol table into symtable map
        BufferedReader symReader = new BufferedReader(new FileReader(symtableFile));
        String symLine;

        while ((symLine = symReader.readLine()) != null) {
            String[] symTokens = symLine.split("\\s+");
            symtable.put(symTokens[1], Integer.parseInt(symTokens[0]));
        }

        symReader.close();

        // Create FileWriter for output2.txt
        FileWriter outputWriter = new FileWriter("output2.txt");

        // Process the intermediate code
        BufferedReader reader = new BufferedReader(new FileReader(input_ic));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s+");

            if (tokens.length > 1) {
                String opcode = tokens[1];

                if (opcode.equals("(01,AD)")) {
                    // Assembler Directive (Start)
                    locCounter = Integer.parseInt(tokens[2].replaceAll("[^\\d]", ""));
                } else if (opcode.startsWith("(")) {
                    // Opcodes in parentheses
                    handleParenthesesOpcode(outputWriter, locCounter, opcode, tokens);
                } else if (opcode.equals("(05,AD)")) {
                    // Literal pool
                    // Do nothing for now
                } else if (opcode.matches("\\d+")) {
                    // Lines with just the opcode and operand without parentheses
                    String operand = opcode.replaceAll("[^\\d]", "");
                    printAndWriteMachineCode(outputWriter, locCounter, "IS", operand);
                } else {
                    // Print debugging information for unrecognized opcode
                    System.out.println("Unrecognized Opcode: " + opcode);
                }
            }

            locCounter++;
        }

        reader.close();
        outputWriter.close();
    }

    private void handleParenthesesOpcode(FileWriter outputWriter, int locCounter, String opcode, String[] tokens) throws IOException {
        if (opcode.equals("(04,IS)") || opcode.equals("(01,IS)") || opcode.equals("(05,IS)")) {
            // Instruction statements
            String operand = tokens[tokens.length - 1].replaceAll("[^\\d]", "");
            printAndWriteMachineCode(outputWriter, locCounter, opcode, operand);
        } else if (opcode.equals("(01,DL)") || opcode.equals("(02,DL)")) {
            // Data statements
            String operand = tokens[tokens.length - 1].replaceAll("[^\\d]", "");
            printAndWriteMachineCode(outputWriter, locCounter, opcode, operand);
        } else {
            // Print debugging information for unrecognized opcode
            System.out.println("Unrecognized Opcode: " + opcode);
        }
    }

    private void printAndWriteMachineCode(FileWriter outputWriter, int locCounter, String opcode, String operand) throws IOException {
        // Print to console
        System.out.println("+ " + String.format("%03d", locCounter) + " " + opcode.substring(1) + " 0 " + operand);

        // Write to file
        outputWriter.write("+ " + String.format("%03d", locCounter) + " " + opcode.substring(1) + " 0 " + operand + "\n");
    }

    public static void main(String[] args) {
        TwoPassAssemblerPass2 pass2Assembler = new TwoPassAssemblerPass2();

        try {
            pass2Assembler.pass2("input_ic.txt", "littable.txt", "symtable.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
