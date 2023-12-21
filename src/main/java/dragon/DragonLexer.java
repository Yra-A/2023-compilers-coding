package dragon;

import java.time.Period;

public class DragonLexer extends Lexer {
  // the last match position (beyond one)
  private int lastMatchPos = 0;

  // the longest match: position (beyond one) and token type
  int longestPrefixPos = 0;
  TokenType longestPrefixTokenType = null;

  private final KeywordTable kwTable = new KeywordTable();

  public DragonLexer(String input) {
    super(input);
  }

  @Override
  public Token nextToken() {
    if (peek == EOF) {
      return Token.EOF;
    }

    if (Character.isWhitespace(peek)) {
      return WS();
    }

    if (Character.isLetter(peek)) {
      return ID();
    }

    if (Character.isDigit(peek)) {
      return INT();
    }

    if (peek == '=') {
      advance();
      return Token.EQ;
    }

    if (peek == '<') {
      // 先往前走判断，看看是不是 <= 或者 <>
      advance();
      if (peek == '=') {
        advance();
        return Token.LE;
      } else if (peek == '>') {
        advance();
        return Token.NE;
      } else {
        return Token.LT;
      }

      if (peek == '>') {
        // 先往前走判断，看看是不是 >=
        advance();
        if (peek == '=') {
          advance();
          return Token.GE;
        } else {
          return Token.GT;
        }
      }
    }

    Token unknown = new Token(TokenType.UNKNOWN, Character.toString(peek));
    advance();
    return unknown;
  }


  private Token NUMBER() {
    advance();
    int state = 13;
    while (true) {
      switch (state) {
        case 13:
          longestPrefixPos = pos;
          longestPrefixTokenType = TokenType.INT;

          if (Character.isDigit(peek)) {
            advance();
            break;
          } else if (peek == '.') {
            advance();
            state = 14;
            break;
          } else if (peek == 'E') {
            advance();
            state = 16;
            break;
          } else { // an INT
            return backToTheLongestMatch();
          }
        case 14:
          if (Character.isDigit(peek)) {
            advance();
            state = 15;
            break;
          } else {
            return backToTheLongestMatch();
          }
        case 15:
          // the longest match
          longestPrefixPos = pos;
          longestPrefixTokenType = TokenType.REAL;

          if (Character.isDigit(peek)) {
            advance();
            break;
          } else if (peek == 'E') {
            advance();
            state = 16;
            break;
          } else { // a REAL
            return backToTheLongestMatch();
          }
        case 16:
          if (peek == '+' || peek == '-') {
            advance();
            state = 17;
            break;
          } else if (Character.isDigit(peek)) {
            advance();
            state = 18;
            break;
          } else {
            return backToTheLongestMatch();
          }
        case 17:
          if (Character.isDigit(peek)) {
            advance();
            state = 18;
            break;
          } else {
            return backToTheLongestMatch();
          }
        case 18:
          longestPrefixPos = pos;
          longestPrefixTokenType = TokenType.SCI;

          if (Character.isDigit(peek)) {
            advance();
            break;
          } else { // an SCI
            return backToTheLongestMatch();
          }
        default:
          System.err.println("Unreachable");
      }
    }
  }

  private Token backToTheLongestMatch() {
    this.reset(longestPrefixPos);
    return new Token(longestPrefixTokenType,
        this.input.substring(this.lastMatchPos, this.pos));
  }

  private Token WS() {
    while (Character.isWhitespace(peek)) {
      advance();
    }
    return Token.WS;
  }

  private Token ID() {
    StringBuilder sb = new StringBuilder();

    do {
      sb.append(peek);
      advance();
    } while (Character.isLetterOrDigit(peek));

    Token token = this.kwTable.getKeyword(sb.toString());
    if (token == null) {
      return new Token(TokenType.ID, sb.toString());
    }

    return token;
  }

  private Token INT() {
    StringBuilder sb = new StringBuilder();

    do {
      sb.append(peek);
      advance();
    } while (Character.isDigit(peek));

    return new Token(TokenType.INT, sb.toString());
  }
}