import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import components.*;

public class CPU {
    static final String dashes = "------------------------";

    // Initialize global CPU components
    static boolean done = false;
    static int clockCycles = 1;
    static int numInstructions;
    static int fetchStart;
    static int IR;
    static int[] MEM = new int[2048];
    static int[] instrCount = {0, 0, 0, 0, 0};

    // Global Registers for decode
    static int opcode;
    static int R1;
    static int shamt;
    static int imm;
    static int address;
    static int valueR1;
    static int valueR2;
    static int valueR3;

    // Global Register for execute
    static int Accumulator;
    static boolean memWrite;
    static boolean memRead;
    static boolean isJump;
    static boolean jumpValue;
    static int R1exec;
    static int R1exec1;
    static boolean NOPexec;
    
    // Global Register for memory
    static int Accumulator2;
    static int R1mem;
    static boolean memWrite2;
    static boolean isJump2;
    static boolean jumpValue2;
    static boolean NOPmem;

    // Initialize components
    static Registers registers = new Registers();
    static ALU alu = new ALU();

    public static void main(String[] args) {
        // Parse text file
        parse("./code.txt");

        // Calculate number of loops
        // final int numLoops = 7 + ((numInstructions - 1) * 2);
        fetchStart = clockCycles;

        // Iterate over loop counts
        while(!done) {
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
            
            int PC = registers.getPC();
            registers.incPC(); 
            MEM[PC] = instructionBinaryCode;
        }

        // Get total instructions from the value of the PC after finishing reading the text file
        numInstructions = registers.getPC();
        
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
        if (clockCycles < (fetchStart + 6) || clockCycles % 2 == ((fetchStart + 1) % 2)) return;
        

        // Check if done
        if (instrCount[4] >= numInstructions) {
            done = true;
            return;
        }
        
        // Log current instruction number
        instrCount[4]++;
        System.out.println(clockCycles + " - [WRITEBACK]: Instruction " + instrCount[4]);

        // Check if NOP
        if (NOPmem) {
            System.out.println("NOP instruction");
            System.out.println(dashes);
            return;
        }
        
        if (isJump2 && jumpValue2) {
            registers.setPC(Accumulator2);
            
            // Reset stats used
            fetchStart = clockCycles;
            jumpValue = false;
            jumpValue2 = false;
            isJump = false;
            isJump2 = false;

            for (int i = 0; i < instrCount.length; i++) {
                instrCount[i] = Accumulator2 - 1;
            }

            System.out.println("Jump to: " + Accumulator2);
        } else if (!memWrite2) {
            registers.setRegister(R1mem, Accumulator2);
            System.out.println("Writeback " + Accumulator2 + " to R" + R1mem);
        }
        System.out.println(dashes);
    }

    private static void memory() {
        // Only do memory on even cycles and after first execute completed
        if (clockCycles < (fetchStart + 5) || clockCycles % 2 == (fetchStart  % 2) || instrCount[3] >= numInstructions) return;
        
        memWrite2 = memWrite;
        isJump2 = isJump;
        jumpValue2 = jumpValue;
        Accumulator2 = Accumulator;
        R1mem = R1exec1;
        
        // Log current instruction number
        instrCount[3]++;
        System.out.println(clockCycles + " - [MEMORY]: Instruction " + instrCount[3]);

        if (isJump && jumpValue) return;

        // Check if NOP
        NOPmem = NOPexec;
        if (NOPexec) {
            System.out.println("NOP instruction");
            System.out.println(dashes);
            return;
        }

        if (memRead) {
            Accumulator2 = MEM[Accumulator];
            System.out.println("Read: " + Accumulator2);
        }
        else if (memWrite) {
            MEM[Accumulator]=R1exec1;
            System.out.println("Write: " + R1exec1 + " in " + Accumulator);
        }

        System.out.println(dashes);
    }

    private static void execute() {
        // Only decode after first decode completed
        if (clockCycles < (fetchStart + 3) || instrCount[2] >= numInstructions) return;
        if (isJump && jumpValue) return;

        // Act based on ALU status, busy simulates the 2 cycles it takes to perform the operations
        if (alu.isBusy()) {
            // Log current instruction number
            System.out.println(clockCycles + " - [EXECUTE]: Instruction " + instrCount[2]);
            Accumulator = alu.free();

            // Check if this is a NOP
            NOPexec = alu.isNOP();
            if (NOPexec) {
                System.out.println("NOP instruction");
                System.out.println(dashes);
                return;
            }
            R1exec1 = R1exec;
            jumpValue = alu.jumpvalue;
            System.out.println("Accumulator: " + Accumulator);
            System.out.println(dashes);
            return;
        }

        // Log current instruction number
        instrCount[2]++;
        System.out.println(clockCycles + " - [EXECUTE]: Instruction " + instrCount[2]);
        
        alu.execute();
        
        R1exec = alu.R1;
        memRead = alu.memRead;
        memWrite = alu.memWrite;
        isJump = alu.isJump;
        System.out.println("Flags = [MEMREAD: " + memRead + ", MEMWRITE: " + memWrite + ", ISJUMP: " + isJump + "]");
        System.out.println(dashes);
    }

    private static void decode() {
        // Only decode after first fetch completed
        if (clockCycles < (fetchStart + 1) || instrCount[1] >= numInstructions) return;

        // Simulate decode on two cycles
        if (clockCycles % 2 == ((fetchStart + 1) % 2)) {
            opcode =  (IR & 0b11110000000000000000000000000000) >> 28;  // bits31:28
            R1 =      (IR & 0b00001111100000000000000000000000) >> 23;  // bits27:23
            int R2 =  (IR & 0b00000000011111000000000000000000) >> 18;  // bits22:18
            int R3 =  (IR & 0b00000000000000111110000000000000) >> 13;  // bits17:13
            shamt =   (IR & 0b00000000000000000001111111111111);         // bits17:10
            imm =     (IR & 0b00000000000000111111111111111111);         // bits17:0
            address = (IR & 0b00001111111111111111111111111111);         // bits27:0
            
            valueR1 = registers.getRegister(R1);
            valueR2 = registers.getRegister(R2);
            valueR3 = registers.getRegister(R3);

            
            // Log current instruction number
            instrCount[1]++;
            System.out.println(clockCycles + " - [DECODE]: Instruction " + instrCount[1]);
            System.out.println("opcode: " + opcode);
            System.out.println("R1: " + R1);
            System.out.println("R2: " + R2);
            System.out.println("R3: " + R3);
            System.out.println("shamt: " + shamt);
            System.out.println("imm: " + imm);
            System.out.println("address: " + Integer.toBinaryString(address));
            System.out.println("valueR1: " + valueR1);
            System.out.println("valueR2: " + valueR2);
            System.out.println("valueR3: " + valueR3);
            System.out.println(dashes);
        } else {
            // Send data to ALU
            alu.loadData(opcode, R1, shamt, imm, address, valueR1, valueR2, valueR3, instrCount[1]);

            // Log current instruction number
            System.out.println(clockCycles + " - [DECODE]: Instruction " + instrCount[1]);
            System.out.println(dashes);
        }
    }

    private static void fetch() {
        // Only fetch on odd cycles
        if (clockCycles % 2 == ((fetchStart + 1) % 2) || instrCount[0] >= numInstructions) return;

        // Move PC to MAR, then increment it
        int AR = registers.getPC();
        registers.incPC();
        IR = MEM[AR];
        
        // Log current instruction number
        instrCount[0]++;
        System.out.println(clockCycles + " - [FETCH]: Instruction " + instrCount[0]);
        System.out.println("PC: " + AR);
        System.out.println("Instruction " + Integer.toBinaryString(IR));
        System.out.println(dashes);
    }
}