import java.util.*;

class MDT {
    int index;
    String def;

    MDT(int i, String a) {
        this.index = i;
        this.def = a;
    }
}

class MNT {
    int index, mdtind;
    String name;
    Vector<String> ala;

    MNT(int i, String a, int ind, Vector<String> b) {
        this.index = i;
        this.name = a;
        this.mdtind = ind;
        this.ala = b;
    }
}

class Pass1MP {
    public static void main(String[] args) throws Exception {
        String[] input = {
            "MACRO",
            "INCR &X,&Y=,&REG=AREG",
            "MOVER &REG,&X",
            "ADD &REG,&Y",
            "MOVEM &REG,&X",
            "MEND",
            "MACRO",
            "DECR &A,&B,&REG=BREG",
            "MOVER &REG,&A",
            "SUB &REG,&B",
            "MOVEM &REG,&A",
            "MEND",
            "START 100",
            "READ N1",
            "READ N2",
            "INCR N1,Y=BREG,REG=CREG",
            "DECR N1,N2",
            "STOP",
            "N1 DS 1",
            "N2 DS 1",
            "END"
        };

        Vector<MDT> mdt = new Vector<>();
        Vector<MNT> mnt = new Vector<>();
        Vector<String> isc = new Vector<>();
        boolean macroDefinition = false;

        for (int i = 0; i < input.length; i++) {
            String line = input[i].trim();
            if (line.isEmpty())
                continue;

            if (line.contains("MACRO")) {
                macroDefinition = true;
                line = input[++i];
                String macroName = line.split("\\s+")[0];
                Vector<String> arguments = new Vector<>();
                while (!line.contains("MEND")) {
                    String[] tokens = line.split("\\s+");
                    for (String token : tokens) {
                        if (token.startsWith("&") && !token.contains("=")) {
                            arguments.addElement(token);
                        }
                    }
                    mdt.addElement(new MDT(mdt.size() + 1, line));
                    line = input[++i];
                }
                mnt.addElement(new MNT(mnt.size() + 1, macroName, mdt.size() - arguments.size(), arguments));
            } else {
                isc.addElement(line);
            }
        }

        System.out.println("\nPASS 1\n");
        System.out.println("MDT");
        for (MDT t : mdt) {
            System.out.println(t.index + " " + t.def);
        }

        System.out.println("\nMNT");
        for (MNT t : mnt) {
            System.out.print(t.index + " " + t.name + " " + t.mdtind + "\tALA: ");
            for (String arg : t.ala) {
                System.out.print(arg + " ");
            }
            System.out.println();
        }

        System.out.println("\nIntermediate code");
        for (String instruction : isc) {
            System.out.println(instruction);
        }
    }
}
