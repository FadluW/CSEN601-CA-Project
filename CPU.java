import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import components.*;

public class CPU {
    // Initialize global CPU components
    static int clockCycles = 1;
    static int numInstructions = 0;
    static int IR;
    static int Accumulator;
    static String[] MEM = new String[2048];

    // Initialize components
    static Registers registers = new Registers();
    static ALU alu = new ALU();

    public static void main(String[] args) {
        // Parse text file
        // parse("");

        // Calculate number of loops
        // final int numLoops = 7 + ((numInstructions - 1) * 2);
        final int numLoops = 19;
        
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
        // Only do writeback on odd cycles and after first memory completed
        if (clockCycles < 7 || clockCycles % 2 == 0) return;
        
        // Log current instruction number
        System.out.println(clockCycles + "[WRITEBACK]: Instruction " + (registers.getPC() - 3));
    }

    private static void memory() {
        // Only do memory on even cycles and after first execute completed
        if (clockCycles < 6 || clockCycles % 2 == 1) return;

        // Log current instruction number
        System.out.println(clockCycles + "[MEMORY]: Instruction " + (registers.getPC() - 2));
    }

    private static void execute() {
        // Only decode after first decode completed
        if (clockCycles < 4) return;

        // Act based on ALU status, busy simulates the 2 cycles it takes to perform the operations
        if (alu.isBusy()) {
            Accumulator = alu.free();
            return;
        }

        // Log current instruction number
        System.out.println(clockCycles + "[EXECUTE]: Instruction " + (registers.getPC() - 1));
    }

    private static void decode() {
        // Only decode after first fetch completed
        if (clockCycles < 2) return;

        // Log current instruction number
        System.out.println(clockCycles + "[DECODE]: Instruction " + (registers.getPC()));
    }

    private static void fetch() {
        // Only fetch on odd cycles
        if (clockCycles % 2 == 0) return;

        // Log current instruction number
        System.out.println(clockCycles + "[FETCH]: Instruction " + (registers.getPC() + 1));

        // Move PC to MAR, then increment it
        int AR = registers.getPC();
        registers.incPC();
        


        // Move current 
    }
}