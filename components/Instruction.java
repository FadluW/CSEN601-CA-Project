package components;

public class Instruction {
    private String bString;
    private String line;


    public Instruction(String Line) {
        this.line = Line;
        initialize();
    }

    public String getBinaryString() {
        return bString;
    }

    private void initialize() {
        String instructionBinaryCode = "";
    	String [] instruction = line.split(" ");
    	
        // Switch on the OP code
    	switch(instruction[0]) {
            case "ADD": {
                instructionBinaryCode += "0000";
                break;
            }
            case "SUB": {
                instructionBinaryCode += "0001";
                break;
            } 
            case "MUL": {
                instructionBinaryCode += "0010";
                break;
            }
            case "MOVI": {
                instructionBinaryCode += "0011";
                break;
            }
            case "JEQ": {
                instructionBinaryCode += "0100";
                break;
            }
            case "AND": {
                instructionBinaryCode += "0101";
                break;
            }
            case "XORI": {
                instructionBinaryCode += "0110";
                break;
            }
            case "LSL": {
                instructionBinaryCode += "0111";
                break;
            }
            case "LSR": {
                instructionBinaryCode += "1000";
                break;
            }
            case "MOVR": {
                instructionBinaryCode += "1001";
                break;
            }
            case "MOVM": {
                instructionBinaryCode += "1010";
                break;
            }
        }
    	
        // Based on the OP type, encode rest of the instruction
    	switch(instruction[0]) {
            case "ADD":
            case "SUB":
            case "MUL":
            case "AND": {
                instructionBinaryCode+= RegisterNumber(instruction[1]) + RegisterNumber(instruction[2]) + RegisterNumber(instruction[3])+"0000000000000";
                break;
            }
            case "LSL":
            case "LSR": {
                instructionBinaryCode += RegisterNumber(instruction[1]) + RegisterNumber(instruction[2]) + bitPadding(instruction[3], 13);
                break;
            }
            case "JEQ":
            case "XORI":
            case "MOVR":
            case "MOVM": {
                instructionBinaryCode += RegisterNumber(instruction[1]) + RegisterNumber(instruction[2]) + bitPadding(instruction[3], 18);
                break;
            }
            case "MOVI": {
                instructionBinaryCode += RegisterNumber(instruction[1]) + "00000" + bitPadding(instruction[3], 18);
                break;
            }
            case "JMP": {
                instructionBinaryCode += RegisterNumber(instruction[1]) + bitPadding(instruction[3], 28);
                break;
            }
    	}
        this.bString = instructionBinaryCode;
    }

    /**
     * Takes an integer number and translates it to string binary, adding zero padding to the left until it is required bits long.
     * @param num denary integer in the form of a String
     * @param bitLength number of bits binary string should end up as
     * @return binary string
     */
    public static String bitPadding(String num, int bitLength) {
        // Convert number to binary string
    	String out = Integer.toBinaryString(Integer.parseInt(num));

        // Iterate until string is of required length
    	while(out.length() < bitLength)
    	{
    		out = "0" + out;
    	}
    	
    	return out;
	}

    /**
     * Converts register string to binary string value of that register.
     * ex. R3 -> 00011
     * @param Register register string
     * @return binary string value of register index
     */
    public static String RegisterNumber(String Register)
    {
    	switch(Register)
    	{
    	case "R0" :return "00000";
    	case "R1" :return "00001";
    	case "R2" :return "00010";
    	case "R3" :return "00011";
    	case "R4" :return "00100";
    	case "R5" :return "00101";
    	case "R6" :return "00110";
    	case "R7" :return "00111";
    	case "R8" :return "01000";
    	case "R9" :return "01001";
    	case "R10":return "01010";
    	case "R11":return "01011";
    	case "R12":return "01100";
    	case "R13":return "01101";
    	case "R14":return "01110";
    	case "R15":return "01111";
    	case "R16":return "10000";
    	case "R17":return "10001";
    	case "R18":return "10010";
    	case "R19":return "10011";
    	case "R20":return "10100";
    	case "R21":return "10101";
    	case "R22":return "10110";
    	case "R23":return "10111";
    	case "R24":return "11000";
    	case "R25":return "11001";
    	case "R26":return "11010";
    	case "R27":return "11011";
    	case "R28":return "11100";
    	case "R29":return "11101";
    	case "R30":return "11110";
    	case "R31":return "11111";
    	 default: return "00000";
    	}	
    }
}