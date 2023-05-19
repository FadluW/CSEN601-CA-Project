import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import components.*;

public class CPU {
    // Initialize global CPU components
    static int clockCycles = 1;
    static int numInstructions = 7;
    static int IR;
    static int Accumulator;
    static int[] MEM = new int[2048];
    static int[] instrCount = {0, 0, 0, 0, 0};

    // Initialize components
    static Registers registers = new Registers();
    static ALU alu = new ALU();

    public static void main(String[] args) {
        // Parse text file
        // parse("");

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
            int instructionBinaryCode= i.getBinaryInt();
            
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
        // Only do writeback on odd cycles and after first memory completed
        if (clockCycles < 7 || clockCycles % 2 == 0 || clockCycles > (2 * numInstructions + 6)) return;
        
        // Log current instruction number
        instrCount[4]++;
        System.out.println(clockCycles + " - [WRITEBACK]: Instruction " + instrCount[4]);
    }

    private static void memory() {
        // Only do memory on even cycles and after first execute completed
        if (clockCycles < 6 || clockCycles % 2 == 1 || clockCycles > (2 * numInstructions + 5)) return;

        // Log current instruction number
        instrCount[3]++;
        System.out.println(clockCycles + " - [MEMORY]: Instruction " + instrCount[3]);
    }

    private static void execute() {
        // Only decode after first decode completed
        if (clockCycles < 4 || clockCycles > (2 * numInstructions + 3)) return;

        // Act based on ALU status, busy simulates the 2 cycles it takes to perform the operations
        if (alu.isBusy()) {
            // Log current instruction number
            System.out.println(clockCycles + " - [EXECUTE]: Instruction " + instrCount[2]);
            Accumulator = alu.free();
            return;
        }

        // Log current instruction number
        instrCount[2]++;
        System.out.println(clockCycles + " - [EXECUTE]: Instruction " + instrCount[2]);
        alu.execute();
    }

    private static void decode() {
        // Only decode after first fetch completed
        if (clockCycles < 2 || clockCycles > (2 * numInstructions + 1)) return;

        // Simulate decode on two cycles
        if (clockCycles % 2 == 0) {
            // Log current instruction number
            instrCount[1]++;
            System.out.println(clockCycles + " - [DECODE]: Instruction " + instrCount[1]);
        } else {
            // Log current instruction number
            System.out.println(clockCycles + " - [DECODE]: Instruction " + instrCount[1]);
        }
    }

    private static void fetch() {
        // Only fetch on odd cycles
        if (clockCycles % 2 == 0 || clockCycles > (2 * numInstructions - 1)) return;

        // Move PC to MAR, then increment it
        int AR = registers.getPC();
        registers.incPC();
        IR = MEM[AR];
        
        // Log current instruction number
        instrCount[0]++;
        System.out.println(clockCycles + " - [FETCH]: Instruction " + instrCount[0]);


        // Move current 
    }
}