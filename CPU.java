import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import components.*;

public class CPU {
    // Initialize global CPU components
    static int clockCycles = 1;
    static int numInstructions = 0;
    static int countID = 0;
    static int countEX = 0;
    static String IR;
    static int Accumulator;
    static String[] MEM = new String[2048];

    // Initialize components
    static Registers registers = new Registers();
    static ALU alu = new ALU();

    public static void main(String[] args) {
        // Parse text file
        parse("");

        // Calculate number of loops
        final int numLoops = 7 + ((numInstructions - 1) * 2);

        // Iterate over loop counts
        for (int i = 0; i < numLoops; i++) {
            fetch();
            decode();
            execute();
            memory();
            writeback();
            clockCycles++;
        }
            
    }

    private static void parse(String filelocation) {
        // load file
        File file = new File(filelocation);
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return; // Handle the exception or terminate the program
        }

        // Iterate over each line (instruction) in the text file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Instruction i = new Instruction(line);
            String instructionBinaryCode= i.getBinaryString();
            
            int PC=registers.getPC();
            registers.incPC(); 
            MEM[PC]=instructionBinaryCode;
        }

        // Get total instructions from the value of the PC after finishing reading the text file
        numInstructions = registers.incPC();
        
        // Reset PC for later fetching, close file
        registers.setPC(0);
        scanner.close();
    }

    /**
     * Takes a binary instruction and parses it into the type of instruction and the operation it does.
     * @param instruction 32-bit binary string instruction
     * @return "Translated" instruction operation
     */
    public static String RIJ(String instruction)
    {
           String opcode = instruction.substring(0, 4);
     
              
              int decimal = Integer.parseInt(opcode, 2);
              System.out.println(decimal); // Output: 42

              switch (decimal)
              {
                case 0 : return "R ADD";
                case 1: return "R SUB";
                case 2: return "R MUL";
                case 3: return "I MOVI";
                case 4: return "I JEQ";
                case 5:  return "R AND";
                case 6: return "I XORI";
                case 7: return "J JMP";
                case 8: return "R LSL ";
                case 9:  return "R LSR";
                case 10: return "I MOVR";
                case 11: return "I MOVM";

              }

          
        return "";

    }

    private static void writeback() {
    }

    private static void memory() {
        // Only do memory on even cycles
        if (clockCycles % 2 == 1) return;


    }

    private static void execute() {
        // Only decode after first decode completed
        if (clockCycles < 4) return;

        // Act based on ALU status, busy simulates the 2 cycles it takes to perform the operations
        if (alu.isBusy()) {
            Accumulator = alu.free();
            return;
        }

        String decode = RIJ(IR);
        String Itype = decode.substring(0, 1);
        String OPCode = decode.substring(2);
        if (Itype == "R") {
            int R1 = Integer.parseInt(IR.substring(4,9), 2);
            int R2 = Integer.parseInt(IR.substring(9,14), 2);
            int R3 = Integer.parseInt(IR.substring(14,19), 2);
            
            int value_R2 = registers.getRegister(R2);
            int value_R3 = registers.getRegister(R3);
            
            // switch(OPCode) {
            //     case "ADD": Registers[R1]=value_R2+value_R3; break; 
            //     case "SUB": Registers[R1]=value_R2-value_R3; break; 
            //     case "MUL": Registers[R1]=value_R2*value_R3; break; 
            // }
        }
        else if (Itype=="I") {
            
        }
        else {
            
        }
    }

    private static void decode() {
        // Only decode after first fetch completed
        if (clockCycles < 2) return;
    }

    private static void fetch() {
        // Only fetch on odd cycles
        if (clockCycles % 2 == 0) return;

        // Move PC to MAR, then increment it
        int AR = registers.getPC();
        registers.incPC();

        // Move current 
    }
}