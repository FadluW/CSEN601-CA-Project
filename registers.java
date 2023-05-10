public class registers {

    private int GPRS[] = new int[31];   //R1 - R31
    private final static int zeroRegister = 0;
    private int PC = 0;

    public registers() {

    }

    /**
     * Get register from R0 to R31
     * 
     */
    public int getRegister(int x) {
        // Ensure within register range
        if (x < 0 || x > 31) return 0;

        if (x == 0) return zeroRegister;
        return GPRS[x - 1];
    }

    public void setRegister(int x, int value) {
        // Ensure within register range
        if (x < 0 || x > 31) return;

        if (x == 0) return;
        GPRS[x] = value;
    }

    /**
     * Gets the current value of the program counter
     */
    public int getPC() {
        return PC;
    }

    /**
     * Returns incremented program counter
     */
    public int incPC() {
        return ++PC;
    }

    public int setPC(int x) {
        PC = x;
        return PC;
    }
}