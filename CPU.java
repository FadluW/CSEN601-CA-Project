import components.*;

public class CPU {
    // Initiate global CPU components
    static int clockCycles = 0;
    static Registers registers = new Registers();

    public static void main(String[] args) {
        fetch();
        decode();
        execute();
        memory();
        writeback();

        clockCycles++;
    }

    private static void writeback() {
    }

    private static void memory() {
    }

    private static void execute() {
    }

    private static void decode() {
    }

    private static void fetch() {
    }
}