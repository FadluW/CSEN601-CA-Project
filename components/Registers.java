package components;

public class Registers {
    static final String dashes = "------------------------";
    static final int NUM_REGISTERS = 31;

    private int GPRS[] = new int[NUM_REGISTERS];   //R1 - R31
    private final static int zeroRegister = 0;
    private int PC = 0;

    /**
     * Initiate CPU Registers to be used, including the PC
     */
    public Registers() {

    }

    /**
     * Get value of register from R0 to R31
     * 
     * @param x register Rx
     * @return register Rx
     */
    public int getRegister(int x) {
        // Ensure within register range
        if (x < 0 || x > NUM_REGISTERS) return 0;

        if (x == 0) return zeroRegister;
        return GPRS[x - 1];
    }

    /**
     * Set a given register to a passed value
     * 
     * @param x register Rx
     * @param value new value for the register
     */
    public void setRegister(int x, int value) {
        // Ensure within register range
        if (x < 0 || x > NUM_REGISTERS) return;
        
        if (x == 0) return;
        GPRS[x - 1] = value;
        System.out.println("\nRegister R" + x + " reassigned to " + value + "\n");
    }

    /**
     * Gets the current value of the program counter
     * 
     * @return current value of the PC
     */
    public int getPC() {
        return PC;
    }

    /**
     * Returns incremented program counter
     * 
     * @return incremented value of the PC
     */
    public int incPC() {
        return ++PC;
    }

    /**
     * Modify the program counter to an immediate value
     * 
     * @param x is the new value of the PC
     * @return new PC
     */
    public int setPC(int x) {
        PC = x;
        return PC;
    }

    public void printRegisters() {
        System.out.println("\n[REG CONTENTS]");
        System.out.println("|_ [R0] - " + zeroRegister);
        for (int i = 0; i < NUM_REGISTERS; i++) {
            System.out.println("|_ [R" + (i + 1) + "] - " + GPRS[i]);
        }
    }
}