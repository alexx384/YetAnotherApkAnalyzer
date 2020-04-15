package extract.parse.source;

import java.text.CharacterIterator;

enum Terminal {
    /* Terminal symbols */
    TS_UNKNOWN(-1, true),
    TS_PACKAGE(0, true),
    TS_IDENTIFIER(1, true),
    TS_DOT(2, true),
    TS_SEMICOLON(3, true),
    TS_IMPORT(4, true),
    TS_PUBLIC(5, true),
    TS_FINAL(6, true),
    TS_CLASS(7, true),
    TS_EXTENDS(8, true),
    TS_IMPLEMENTS(9, true),
    TS_OPEN_CURLY_BRACE(10, true),
    TS_COMMA(11, true),
    TS_CLOSE_CURLY_BRACE(12, true),
    TS_END(13, true),

    /* Non-terminal symbols */
    NTS_SOURCE(0, false),
    NTS_PACKAGE_NEXT_NAME(1, false),
    NTS_IMPORT_NEXT_NAME(2, false),
    NTS_HEADER_PUBLIC_STATEMENT(3, false),
    NTS_HEADER_FINAL_STATEMENT(4, false),
    NTS_CLASS_DECLARATION(5, false),
    NTS_IMPORT_STATEMENT(6, false),
    NTS_CLASS_EXTENDS(7, false),
    NTS_CLASS_IMPLEMENTS(8, false),
    NTS_CLASS_BLOCK(9, true);

    public final int value;
    public final boolean isTerminal;

    Terminal(int value, boolean isTerminal) {
        this.value = value;
        this.isTerminal = isTerminal;
    }

    public static boolean isIdentifierStartCharacter(char character) {
        return character == '$'
                || ('A' <= character && character <= 'Z')
                || character == '_'
                || ('a' <= character && character <= 'z');
    }

    public static boolean isIdentifierNextCharacter(char character) {
        return character == '$'
                || ('0' <= character && character <= '9')
                || ('A' <= character && character <= 'Z')
                || character == '_'
                || ('a' <= character && character <= 'z');
    }

    public static boolean isDelimiter(char character) {
        return character == ' ' || character == '\n' || character == '\t';
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static Terminal getTerminal(CharacterIterator iterator) {
        char character = iterator.current();
        while (character == ' ' || character == '\n' || character == '\t') {
            character = iterator.next();
        }

        Terminal result = Terminal.TS_UNKNOWN;
        switch (character) {
            case ',': {
                result = TS_COMMA;
            } break;
            case '.': {
                result = TS_DOT;
            } break;
            case ';': {
                result = TS_SEMICOLON;
            } break;
            case 'a': {     // abstract, assert
                char nextCharacter = iterator.next();
                if (nextCharacter == 'b' && iterator.next() == 's' && iterator.next() == 't' && iterator.next() == 'r'
                        && iterator.next() == 'a' && iterator.next() == 'c' && iterator.next() == 't'
                        && isDelimiter(iterator.next())) {
                    result = TS_UNKNOWN;
                    iterator.setIndex(iterator.getIndex() - 1);
                } else if (nextCharacter == 's' && iterator.next() == 's' && iterator.next() == 'e'
                        && iterator.next() == 'r' && iterator.next() == 't' && isDelimiter(iterator.next())) {
                    result = TS_UNKNOWN;
                    iterator.setIndex(iterator.getIndex() - 1);
                } else if (isIdentifierNextCharacter(iterator.current())) {
                    while (isIdentifierNextCharacter(iterator.next()));
                    iterator.setIndex(iterator.getIndex() - 1);
                    result = TS_IDENTIFIER;
                }
            } break;
            case 'b': {     // boolean, break, byte
                char nextCharacter = iterator.next();
                if (nextCharacter == 'o' && iterator.next() == 'o' && iterator.next() == 'l' && iterator.next() == 'e'
                        && iterator.next() == 'a' && iterator.next() == 'n') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'r' && iterator.next() == 'e' && iterator.next() == 'a'
                        && iterator.next() == 'k') {
                    result = TS_UNKNOWN;
                } else if (iterator.next() == 'y' && iterator.next() == 't' && iterator.next() == 'e') {
                    result = TS_UNKNOWN;
                }
            } break;
            case 'c': {     // case, catch, char, class, const
                switch (iterator.next()) {
                    case 'a': {     // case, catch
                        char nextCharacter = iterator.next();
                        if (nextCharacter == 's' && iterator.next() == 'e') {
                            result = TS_UNKNOWN;
                        } else if (nextCharacter == 't' && iterator.next() == 'c' && iterator.next() == 'h') {
                            result = TS_UNKNOWN;
                        }
                    }
                    break;
                    case 'h': {     // char
                        if (iterator.next() == 'a' && iterator.next() == 'r') {
                            result = TS_UNKNOWN;
                        }
                    }
                    break;
                    case 'l': {     // class
                        if (iterator.next() == 'a' && iterator.next() == 's' && iterator.next() == 's') {
                            result = TS_CLASS;
                        }
                    }
                    break;
                    case 'o': {     // const, continue
                        char nextCharacter = iterator.next();
                        if (nextCharacter == 'n') {
                            char nextNextCharacter = iterator.next();
                            if (nextNextCharacter == 's' && iterator.next() == 't') {
                                result = TS_UNKNOWN;
                            } else if (nextNextCharacter == 't' && iterator.next() == 'i' && iterator.next() == 'n'
                                    && iterator.next() == 'u' && iterator.next() == 'e') {
                                result = TS_UNKNOWN;
                            }
                        }
                    }
                    break;
                }
            }
            case 'd': {     // default, double, do
                char nextCharacter = iterator.next();
                if (nextCharacter == 'e' && iterator.next() == 'f' && iterator.next() == 'a' && iterator.next() == 'u'
                        && iterator.next() == 'l' && iterator.next() == 't') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'o') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 'u' && iterator.next() == 'b' && iterator.next() == 'l'
                            && iterator.next() == 'e') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == CharacterIterator.DONE || nextNextCharacter == ' '
                            || nextNextCharacter == '\n' || nextNextCharacter == '\t') {
                        result = TS_UNKNOWN;
                        iterator.setIndex(iterator.getIndex() - 1);
                    }
                }
            } break;
            case 'e': {     // else, enum, extends
                char nextCharacter = iterator.next();
                if (nextCharacter == 'l' && iterator.next() == 's' && iterator.next() == 'e') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'n' && iterator.next() == 'u' && iterator.next() == 'm') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'x' && iterator.next() == 't' && iterator.next() == 'e'
                        && iterator.next() == 'n' && iterator.next() == 'd' && iterator.next() == 's') {
                    result = TS_EXTENDS;
                }
            } break;
            case 'f': {     // false, final, finally, float, for
                switch (iterator.next()) {
                    case 'a': {     // false
                        if (iterator.next() == 'l' && iterator.next() == 's' && iterator.next() == 'e') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 'i': {     // finally, final
                        if (iterator.next() == 'n' && iterator.next() == 'a' && iterator.next() == 'l') {
                            char nextCharacter = iterator.next();
                            if (nextCharacter == 'l' && iterator.next() == 'y') {
                                result = TS_UNKNOWN;
                            } else if (nextCharacter == CharacterIterator.DONE || isDelimiter(nextCharacter)) {
                                result = TS_FINAL;
                                iterator.setIndex(iterator.getIndex() - 1);
                            }
                        }
                    } break;
                    case 'l': {     // float
                        if (iterator.next() == 'o' && iterator.next() == 'a' && iterator.next() == 't') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 'o': {     // for
                        if (iterator.next() == 'r') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                }
            } break;
            case 'g': {     // goto
                if (iterator.next() == 'o' && iterator.next() == 't' && iterator.next() == 'o') {
                    result = TS_UNKNOWN;
                }
            } break;
            case 'i': {     // if, implements, import, instanceof, interface, int
                char nextCharacter = iterator.next();
                if (nextCharacter == 'f') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'm' && iterator.next() == 'p') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 'l' && iterator.next() == 'e' && iterator.next() == 'm'
                            && iterator.next() == 'e' && iterator.next() == 'n' && iterator.next() == 's') {
                        result = TS_IMPLEMENTS;
                    } else if (nextNextCharacter == 'o' && iterator.next() == 'r' && iterator.next() == 't') {
                        result = TS_IMPORT;
                    }
                } else if (nextCharacter == 'n') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 's' && iterator.next() == 't' && iterator.next() == 'a'
                            && iterator.next() == 'n' && iterator.next() == 'c' && iterator.next() == 'e'
                            && iterator.next() == 'o' && iterator.next() == 'f') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == 't') {
                        char nextNextNextCharacter = iterator.next();
                        if (nextNextNextCharacter == 'e' && iterator.next() == 'r' && iterator.next() == 'f'
                                && iterator.next() == 'a' && iterator.next() == 'c' && iterator.next() == 'e') {
                            result = TS_UNKNOWN;
                        } else if (nextNextNextCharacter == CharacterIterator.DONE || nextNextNextCharacter == ' '
                                || nextNextNextCharacter == '\n' || nextNextNextCharacter == '\t') {
                            result = TS_UNKNOWN;
                            iterator.setIndex(iterator.getIndex() - 1);
                        }
                    }
                }
            } break;
            case 'l': {     // long
                if (iterator.next() == 'o' && iterator.next() == 'n' && iterator.next() == 'g') {
                    result = TS_UNKNOWN;
                }
            } break;
            case 'n': {     // native, new, null
                char nextCharacter = iterator.next();
                if (nextCharacter == 'a' && iterator.next() == 't' && iterator.next() == 'i' && iterator.next() == 'v'
                        && iterator.next() == 'e') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'e' && iterator.next() == 'w') {
                    result = TS_UNKNOWN;
                } else if (nextCharacter == 'u' && iterator.next() == 'l' && iterator.next() == 'l') {
                    result = TS_UNKNOWN;
                }
            } break;
            case 'p': {     // package, private, protected, public
                char nextCharacater = iterator.next();
                if (nextCharacater == 'a' && iterator.next() == 'c' && iterator.next() == 'k' && iterator.next() == 'a'
                        && iterator.next() == 'g' && iterator.next() == 'e') {
                    result = TS_PACKAGE;
                } else if (nextCharacater == 'r') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 'i' && iterator.next() == 'v' && iterator.next() == 'a'
                            && iterator.next() == 't' && iterator.next() == 'e') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == 'o' && iterator.next() == 't' && iterator.next() == 'e'
                            && iterator.next() == 'c' && iterator.next() == 't' && iterator.next() == 'e'
                            && iterator.next() == 'd') {
                        result = TS_UNKNOWN;
                    }
                } else if (nextCharacater == 'u' && iterator.next() == 'b' && iterator.next() == 'l'
                        && iterator.next() == 'i' && iterator.next() == 'c') {
                    result = TS_PUBLIC;
                }
            } break;
            case 'r': {     // return
                if (iterator.next() == 'e' && iterator.next() == 't' && iterator.next() == 'u'
                        && iterator.next() == 'r' && iterator.next() == 'n') {
                    result = TS_UNKNOWN;
                }
            } break;
            case 's': {     // short, static, strictfp, super, switch, synchronized
                switch (iterator.next()) {
                    case 'h': {     // short
                        if (iterator.next() == 'o' && iterator.next() == 'r' && iterator.next() == 't') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 't': {     // static, strictfp
                        char nextCharacter = iterator.next();
                        if (nextCharacter == 'a' && iterator.next() == 't' && iterator.next() == 'i'
                                && iterator.next() == 'c') {
                            result = TS_UNKNOWN;
                        } else if (nextCharacter == 'r' && iterator.next() == 'i' && iterator.next() == 'c'
                                && iterator.next() == 't' && iterator.next() == 'f' && iterator.next() == 'p') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 'u': {     // super
                        if (iterator.next() == 'p' && iterator.next() == 'e' && iterator.next() == 'r') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 'w': {     // switch
                        if (iterator.next() == 'i' && iterator.next() == 't' && iterator.next() == 'c'
                                && iterator.next() == 'h') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                    case 'y': {     // synchronized
                        if (iterator.next() == 'n' && iterator.next() == 'c' && iterator.next() == 'r'
                                && iterator.next() == 'o' && iterator.next() == 'n' && iterator.next() == 'i'
                                && iterator.next() == 'z' && iterator.next() == 'e' && iterator.next() == 'd') {
                            result = TS_UNKNOWN;
                        }
                    } break;
                }
            } break;
            case 't': {     // this, throw, throws, transient, true, try
                char nextCharacter = iterator.next();
                if (nextCharacter == 'h') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 'i' && iterator.next() == 's') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == 'r' && iterator.next() == 'o' && iterator.next() == 'w') {
                        char nextNextNextCharacter = iterator.next();
                        if (nextNextNextCharacter == 's') {
                            result = TS_UNKNOWN;
                        } else if (nextNextNextCharacter == CharacterIterator.DONE || nextNextNextCharacter == ' '
                                || nextNextNextCharacter == '\n' || nextNextNextCharacter == '\t') {
                            result = TS_UNKNOWN;
                        }
                    }
                } else if (nextCharacter == 'r') {
                    char nextNextCharacter = iterator.next();
                    if (nextNextCharacter == 'a' && iterator.next() == 'n' && iterator.next() == 's'
                            && iterator.next() == 'i' && iterator.next() == 'e' && iterator.next() == 'n'
                            && iterator.next() == 't') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == 'u' && iterator.next() == 'e') {
                        result = TS_UNKNOWN;
                    } else if (nextNextCharacter == 'y') {
                        result = TS_UNKNOWN;
                    }
                }
            } break;
            case 'v': {     // void, volatile
                if (iterator.next() == 'o') {
                    char nextCharacter = iterator.next();
                    if (nextCharacter == 'i' && iterator.next() == 'd') {
                        result = TS_UNKNOWN;
                    } else if (nextCharacter == 'l' && iterator.next() == 'a' && iterator.next() == 't'
                            && iterator.next() == 'i' && iterator.next() == 'l' && iterator.next() == 'e') {
                        result = TS_UNKNOWN;
                    }
                }
            } break;
            case 'w': {     // while
                if (iterator.next() == 'i' && iterator.next() == 'l' && iterator.next() == 'e') {
                    result = TS_UNKNOWN;
                }
            } break;
            case '{': {
                result = TS_OPEN_CURLY_BRACE;
            } break;
            case '}': {
                result = TS_CLOSE_CURLY_BRACE;
            } break;
            case CharacterIterator.DONE: {
                result = TS_END;
            } break;
            default: {
                if (isIdentifierStartCharacter(iterator.current())) {
                    while (isIdentifierNextCharacter(iterator.next()));
                    iterator.setIndex(iterator.getIndex() - 1);
                    result = TS_IDENTIFIER;
                } else {
                    throw new IllegalArgumentException("Can't parse '" + character + "' at position "
                            + iterator.getIndex());
                }
            }
        }

        if (Terminal.TS_UNKNOWN.equals(result)) {
            throw new IllegalArgumentException("Can't parse '" + character + "' at position "
                    + iterator.getIndex());
        } else if (Terminal.TS_END.equals(result)) {
            return result;
        } else {
            iterator.setIndex(iterator.getIndex() + 1);
            return result;
        }
    }
}
