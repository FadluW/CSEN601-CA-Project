package components;

public class ALU {
    private boolean busy = false;
    private int result = 0;
    private static int opcode;
    private static int rd;
    private static int shamt;
    private static int imm;
    private static int address;
    private static int valueRS;
    private static int valueRT;
    
    public ALU() {
        
    }

    /**
     * Declare ALU no longer busy, and return the intermediate value for the result that was calculated in the previous cycle.
     * @return integer result of instruction
     */
    public int free() {
        busy = false;
        return result;
    }

    public void execute() {
        busy = true;

        switch(opcode) {
            
        }
    }

    public void loadData(int opcode, int rd, int shamt, int imm, int address, int valueRS, int valueRT) {
        ALU.opcode = opcode;
        ALU.rd = rd;
        ALU.shamt = shamt;
        ALU.imm = imm;
        ALU.address = address;
        ALU.valueRS = valueRS;
        ALU.valueRT = valueRT;   
    }

    public int getResult() {
        return result;
    }

    public boolean isBusy() {
        return busy;
    }
}
