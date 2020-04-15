package extract.parse.source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

public class Parser {
    public static boolean parse(String content) {
//        char[] charJavaSource = content.toCharArray();
        CharacterIterator contentIterator = new StringCharacterIterator(content);
        final Stack<Terminal> expectedTerminalStack = new Stack<>();
        expectedTerminalStack.push(Terminal.NTS_SOURCE);
        Terminal actualTerminal = Terminal.getTerminal(contentIterator);

        while (!expectedTerminalStack.isEmpty()) {
            if (expectedTerminalStack.peek() == actualTerminal) {
                expectedTerminalStack.pop();
                actualTerminal = Terminal.getTerminal(contentIterator);
            } else {
                switch (ParseTable.getCase(expectedTerminalStack.pop(), actualTerminal)) {
                    case 0: {       // 0.  SOURCE -> "package" identifier PACKAGE_NEXT_NAME
                        expectedTerminalStack.push(Terminal.NTS_PACKAGE_NEXT_NAME);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_PACKAGE);
                    } break;
                    case 1:         // 1.  SOURCE -> "import" identifier IMPORT_NEXT_NAME
                    case 16: {      // 16. IMPORT_STATEMENT -> "import" identifier IMPORT_NEXT_NAME
                        expectedTerminalStack.push(Terminal.NTS_IMPORT_NEXT_NAME);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_IMPORT);
                    } break;
                    case 2:         // 2.  SOURCE -> "public" HEADER_PUBLIC_STATEMENT
                    case 17: {      // 17. IMPORT_STATEMENT -> "public" HEADER_PUBLIC_STATEMENT
                        expectedTerminalStack.push(Terminal.NTS_HEADER_PUBLIC_STATEMENT);
                        expectedTerminalStack.push(Terminal.TS_PUBLIC);
                    } break;
                    case 3:         // 3.  SOURCE -> "final" HEADER_FINAL_STATEMENT
                    case 18: {      // 18. IMPORT_STATEMENT -> "final" HEADER_FINAL_STATEMENT
                        expectedTerminalStack.push(Terminal.NTS_HEADER_FINAL_STATEMENT);
                        expectedTerminalStack.push(Terminal.TS_FINAL);
                    } break;
                    case 4:         // 4.  SOURCE -> "class" identifier CLASS_DECLARATION
                    case 19: {		// 19. IMPORT_STATEMENT -> "class" identifier CLASS_DECLARATION
                        expectedTerminalStack.push(Terminal.NTS_CLASS_DECLARATION);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_CLASS);
                    } break;
                    case 5: {		// 5.  PACKAGE_NEXT_NAME -> '.' identifier PACKAGE_NEXT_NAME
                        expectedTerminalStack.push(Terminal.NTS_PACKAGE_NEXT_NAME);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                    } break;
                    case 6:         // 6.  PACKAGE_NEXT_NAME -> ';' IMPORT_STATEMENT
                    case 8: {		// 8.  IMPORT_NEXT_NAME -> ';' IMPORT_STATEMENT
                        expectedTerminalStack.push(Terminal.NTS_IMPORT_STATEMENT);
                        expectedTerminalStack.push(Terminal.TS_SEMICOLON);
                    } break;
                    case 7: {		// 7.  IMPORT_NEXT_NAME -> '.' identifier IMPORT_NEXT_NAME
                        expectedTerminalStack.push(Terminal.NTS_IMPORT_NEXT_NAME);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_DOT);
                    } break;
                    case 9: {		// 9.  HEADER_PUBLIC_STATEMENT -> "final" "class" CLASS_DECLARATION
                        expectedTerminalStack.push(Terminal.NTS_CLASS_DECLARATION);
                        expectedTerminalStack.push(Terminal.TS_CLASS);
                        expectedTerminalStack.push(Terminal.TS_FINAL);
                    } break;
                    case 10:        // 10. HEADER_PUBLIC_STATEMENT -> "class" CLASS_DECLARATION
                    case 12: {		// 12. HEADER_FINAL_STATEMENT -> "class" CLASS_DECLARATION
                        expectedTerminalStack.push(Terminal.NTS_CLASS_DECLARATION);
                        expectedTerminalStack.push(Terminal.TS_CLASS);
                    } break;
                    case 11: {		// 11. HEADER_FINAL_STATEMENT -> "public" "class" CLASS_DECLARATION
                        expectedTerminalStack.push(Terminal.NTS_CLASS_DECLARATION);
                        expectedTerminalStack.push(Terminal.TS_CLASS);
                        expectedTerminalStack.push(Terminal.TS_PUBLIC);
                    } break;
                    case 13: {		// 13. CLASS_DECLARATION -> "extends" identifier CLASS_EXTENDS
                        expectedTerminalStack.push(Terminal.NTS_CLASS_EXTENDS);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_EXTENDS);
                    } break;
                    case 14:        // 14. CLASS_DECLARATION -> "implements" identifier CLASS_IMPLEMENTS
                    case 21: {		// 21. CLASS_EXTENDS -> "implements" identifier CLASS_IMPLEMENTS
                        expectedTerminalStack.push(Terminal.NTS_CLASS_IMPLEMENTS);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_IMPLEMENTS);
                    } break;
                    case 15:        // 15. CLASS_DECLARATION -> identifier "{" CLASS_BLOCK
                    case 25:        // 25. CLASS_IMPLEMENTS -> identifier "{" CLASS_BLOCK
                    case 22: {		// 22. CLASS_EXTENDS -> identifier "{" CLASS_BLOCK
                        expectedTerminalStack.push(Terminal.NTS_CLASS_BLOCK);
                        expectedTerminalStack.push(Terminal.TS_OPEN_CURLY_BRACE);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                    } break;
                    case 20: {		// 20. CLASS_EXTENDS -> "." identifier CLASS_EXTENDS
                        expectedTerminalStack.push(Terminal.NTS_CLASS_EXTENDS);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_DOT);
                    } break;
                    case 23: {		// 23. CLASS_IMPLEMENTS -> "." identifier CLASS_IMPLEMENTS
                        expectedTerminalStack.push(Terminal.NTS_CLASS_IMPLEMENTS);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_DOT);
                    } break;
                    case 24: {		// 24. CLASS_IMPLEMENTS -> "," identifier CLASS_IMPLEMENTS
                        expectedTerminalStack.push(Terminal.NTS_CLASS_IMPLEMENTS);
                        expectedTerminalStack.push(Terminal.TS_IDENTIFIER);
                        expectedTerminalStack.push(Terminal.TS_COMMA);
                    } break;
                    case 26: {		// 26. CLASS_BLOCK -> "}" $
                        expectedTerminalStack.push(Terminal.TS_CLOSE_CURLY_BRACE);
                    } break;
                    default: {
                        throw new IllegalArgumentException("Unknown terminal");
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                Parser.parse(Files.readString(Path.of("Test.java")))
        );
    }
}
