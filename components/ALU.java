package components;

public class ALU {
    private boolean busy = false;
    private int result = 0;

    private int opcode;
    private int shamt;
    private int imm;
    private int address;
    public int valueR1;
    private int valueR2;
    private int valueR3;
    private int pc;
    public int R1;

    public boolean memRead; 
    public boolean memWrite; 
    public boolean isJump; 
    public boolean isNOP;
    public boolean jumpvalue=false;
    
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
        memRead = false;
        memWrite = false;
        isJump = false;
        jumpvalue = false;
        isNOP = false;

        switch(opcode) {
            // ADD
            case 0: {
                result = valueR2 + valueR3;
                break;
            }
            // SUB
            case 0b1: {
                result = valueR2 - valueR3;
                break;
            }
            // MUL
            case 0b10: {
                result = valueR2 * valueR3;
                break;
            }
            // MOVI
            case 0b11: {
                result = imm;
                break;
            }
            // JEQ
            case 0b100: {
                // Jump flag to know we will jump
                result = pc;
                isJump = true;
                if (valueR1 == valueR2) {
                    jumpvalue=true;
                    result += imm;
                }
                break;
            }
            // AND 
            case 0b101: {
                result = valueR2 & valueR3;
                break;
            }
            // XOR
            case 0b110: {
                result = (valueR2 ^ imm);
                break;
            }
            // JMP
            case 0b111: {
                result = (pc & 0b11110000000000000000000000000000) + address;
                isJump = true;
                jumpvalue=true;
                break;
            }
            // LSL
            case -8: {
                result = valueR2 << shamt;
                break;
            }
            // LSR 
            case -7: {
                result = valueR2 >> shamt;
                break;
            }
            // MOVR
            case -6: {
                memRead = true;
                result = valueR2 + imm - 1;
                break;
            }
            // MOVM
            case -5: {
                memWrite = true;
                result = valueR2 + imm - 1;
                break;
            }
            // NOP
            case -4: {
                isNOP = true;
            }
        }
    }

    public void loadData(int opcode, int R1, int shamt, int imm, int address, int valueR1, int valueR2, int valueR3, int pc) {
        // Don't load data if NOP
        this.opcode = opcode;
        if (opcode == -4) return;
        this.R1 = R1;
        this.shamt = shamt;
        this.imm = imm;
        this.address = address;
        this.valueR1 = valueR1;
        this.valueR2 = valueR2;
        this.valueR3 = valueR3;   
        this.pc = pc;
    }

    public int getResult() {
        return result;
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean isNOP() {
        return isNOP;
    }
}
