import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TwoPassAssemblerPass2 {
    private int locCounter = 0;
    private Map<String, Integer> symtable = new HashMap<>();
    private Map<String, String> littable = new HashMap<>();

    public void loadTables(String symtableFile, String littableFile) throws IOException {
        BufferedReader symReader = new BufferedReader(new FileReader(symtableFile));
        String symLine;
        while ((symLine = symReader.readLine()) != null) {
            String[] symTokens = symLine.split("\\s+");
            symtable.put(symTokens[1], Integer.parseInt(symTokens[2]));
        }
        symReader.close();

        BufferedReader litReader = new BufferedReader(new FileReader(littableFile));
        String litLine;
        while ((litLine = litReader.readLine()) != null) {
            String[] litTokens = litLine.split("\\s+");
            littable.put(litTokens[0], litTokens[2]);
        }
        litReader.close();
    }

    public void pass2(String input_ic, String symtableFile, String littableFile) throws IOException {
        loadTables(symtableFile, littableFile);

        FileWriter outputWriter = new FileWriter("output2.txt");

        BufferedReader reader = new BufferedReader(new FileReader(input_ic));
        String line;

        System.out.println("Output for Pass 2:");

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s+");

            if (tokens.length > 1) {
                String opcode = tokens[1];
                String operand = tokens[tokens.length - 1].replaceAll("[^\\d]", "");

                if (symtable.containsKey(operand)) {
                    operand = String.valueOf(symtable.get(operand));
                }

                if (littable.containsKey(operand)) {
                    operand = littable.get(operand);
                }

                printAndWriteMachineCode(outputWriter, locCounter, opcode, operand);
            }

            locCounter++;
        }

        reader.close();
        outputWriter.close();
    }

    private void printAndWriteMachineCode(FileWriter outputWriter, int locCounter, String opcode, String operand) throws IOException {
        System.out.println("+ " + String.format("%03d", locCounter) + " " + opcode + " 0 " + operand);
        outputWriter.write("+ " + String.format("%03d", locCounter) + " " + opcode + " 0 " + operand + "\n");
    }

    public static void main(String[] args) {
        TwoPassAssemblerPass2 pass2Assembler = new TwoPassAssemblerPass2();

        try {
            pass2Assembler.pass2("input_ic.txt", "symtable.txt", "littable.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
