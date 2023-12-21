grammar SimpleExpr;

import SimpleExprRules;

@header {
package simple_expr;
}

prog: stat* EOF;

stat: expr ';'
    | ID '=' expr ';'
    | 'if' expr ';'
    ;

expr: expr ('*' | '/') expr
    | expr ('+' | '-') expr
    | '(' expr ')'
    | ID
    | INT
    | FLOAT
    ;