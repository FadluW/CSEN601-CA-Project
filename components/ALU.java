package components;

public class ALU {
    private boolean busy = false;
    private int result = 0;
    
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

    }

    public int getResult() {
        return result;
    }

    public boolean isBusy() {
        return busy;
    }
}
