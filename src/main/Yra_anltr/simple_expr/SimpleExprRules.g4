lexer grammar SimpleExprRules;

ID: (LETTER | '_') WORD*;
INT: '0' | ([1-9] DIGIT*);
FLOAT: INT '.' DIGIT*
     | '.' DIGIT+
     ;

WS: [ \t\r\n]+ -> skip;

SL_COMMENT: '//' ~[\n]* '\n' -> skip;
DOC_COMMENT : '/**' .*? '*/' -> skip ; // ？的作用是取消贪婪匹配
ML_COMMENT : '/*' .*? '*/' -> skip ;

fragment LETTER: [a-zA-Z];
fragment DIGIT: [0-9];
fragment WORD: LETTER | DIGIT | '_';
