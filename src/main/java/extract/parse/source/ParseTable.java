package extract.parse.source;

final class ParseTable {
    private final static byte UNK = Byte.MIN_VALUE;

    private final static byte[][] table = {
            {  0, UNK, UNK, UNK,   1,   2,   3,   4, UNK, UNK, UNK, UNK, UNK},
            {UNK, UNK,   5,   6, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK},
            {UNK, UNK,   7,   8, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK},
            {UNK, UNK, UNK, UNK, UNK, UNK,   9,  10, UNK, UNK, UNK, UNK, UNK},
            {UNK, UNK, UNK, UNK, UNK,  11, UNK,  12, UNK, UNK, UNK, UNK, UNK},
            {UNK,  15, UNK, UNK, UNK, UNK, UNK, UNK,  13,  14, UNK, UNK, UNK},
            {UNK, UNK, UNK, UNK,  16,  17,  18,  19, UNK, UNK, UNK, UNK, UNK},
            {UNK,  22,  20, UNK, UNK, UNK, UNK, UNK, UNK,  21, UNK, UNK, UNK},
            {UNK,  25,  23, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK,  24, UNK},
            {UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK, UNK,  26}
    };

    public static int getCase(Terminal nonTerminalSymbol, Terminal terminalSymbol) {
//        if (nonTerminalSymbol.isTerminal || !terminalSymbol.isTerminal) {
//            return UNK;
//        }
        byte tableRow = (byte) nonTerminalSymbol.value;
        byte tableColumn = (byte) terminalSymbol.value;

        return ParseTable.table[tableRow][tableColumn];
    }
}
