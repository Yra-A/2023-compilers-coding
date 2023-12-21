package dragon;

public abstract class Lexer {
  public static final char EOF = (char) -1;

  final String input;
  char peek; // 当前字符
  int pos; // 当前字符的下标

  public Lexer(String input) {
    this.input = input;
    this.pos = 0;
    this.peek = input.charAt(pos);
  }

  public abstract Token nextToken();

  public void advance() {
    this.pos++;
    if (this.pos >= this.input.length()) {
      this.peek = EOF;
    } else {
      this.peek = input.charAt(this.pos);
    }
  }

  // 将当前字符重置为指定下标的字符
  public void reset(int pos) {
    this.pos = pos;
    this.peek = input.charAt(pos);
  }
}