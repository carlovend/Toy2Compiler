/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**
   This is a small example of a standalone text substitution scanner
   It reads a name after the keyword name and substitutes all occurences
   of "hello" with "hello <name>!". There is a sample input file
   "sample.inp" provided in this directory
*/

import java_cup.runtime.*;
import java.util.HashMap;



%%

%public
%class Lexer
%cupsym sym
%cup
%unicode
%line
%column

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comments = %.*?%
Number = (\d+(\.\d*)?|\.\d+)([eE][-+]?\d+)?
RealConst = (\d+\d+)
StringConst = \"([^\"\\]|\\.)*\"
StringError = " [^"]
CommentError = "%" [^"%"]
Error = [^]
Id = [A-Za-z][A-Za-z0-9]*
%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol installID(String lexem){
        Symbol token;
        token=symbol(sym.ID,lexem);
        return token;
    }
%}

%%
<YYINITIAL> {

    "var" { return symbol(sym.VAR); }
    ":" { return symbol(sym.COLON); }
    "^=" { return symbol(sym.ASSIGN); }
    ";" { return symbol(sym.SEMI); }
    "," { return symbol(sym.COMMA); }
    [0-9]+ { return symbol(sym.INTEGER_CONST, yytext()); }
    [0-9]+(\.[0-9]+)? { return symbol(sym.REAL_CONST, yytext()); }
    {StringError}  { throw new Error("String error at Line " + yyline + ", Column " + yycolumn); }
    {CommentError}  { throw new Error("Comment error at Line " + yyline + ", Column " + yycolumn); }
    \"([^\"]|\\\"|\\s)*\" { return symbol(sym.STRING_CONST, yytext()); }
    "true" { return symbol(sym.TRUE); }
    "false" { return symbol(sym.FALSE); }
    "real" { return symbol(sym.REAL); }
    "integer" { return symbol(sym.INTEGER); }
    "string" { return symbol(sym.STRING); }
    "boolean" { return symbol(sym.BOOLEAN); }
    "return" { return symbol(sym.RETURN); }
    "func" { return symbol(sym.FUNCTION); }
    "->" { return symbol(sym.TYPERETURN); }
    "endfunc" { return symbol(sym.ENDFUNCTION); }
    "(" { return symbol(sym.LPAR); }
    ")" { return symbol(sym.RPAR); }
    "proc" { return symbol(sym.PROCEDURE); }
    "endproc" { return symbol(sym.ENDPROCEDURE); }
    "out" { return symbol(sym.OUT); }
    "-->" { return symbol(sym.WRITE); }
    "-->!" { return symbol(sym.WRITERETURN); }
    "$" { return symbol(sym.DOLLARSIGN); }
    "<--" { return symbol(sym.READ); }
    "if" { return symbol(sym.IF); }
    "then" { return symbol(sym.THEN); }
    "else" { return symbol(sym.ELSE); }
    "endif" { return symbol(sym.ENDIF); }
    "elseif" { return symbol(sym.ELIF); }
    "while" { return symbol(sym.WHILE); }
    "do" { return symbol(sym.DO); }
    "endwhile" { return symbol(sym.ENDWHILE); }
    "+" { return symbol(sym.PLUS); }
    "-" { return symbol(sym.MINUS); }
    "*" { return symbol(sym.TIMES); }
    "/" { return symbol(sym.DIV); }
    "=" { return symbol(sym.EQ); }
    "<>" { return symbol(sym.NE); }
    "<" { return symbol(sym.LT); }
    "<=" { return symbol(sym.LE); }
    ">" { return symbol(sym.GT); }
    ">=" { return symbol(sym.GE); }
    "&&" { return symbol(sym.AND); }
    "||" { return symbol(sym.OR); }
    "!" { return symbol(sym.NOT); }
    "\\"  { return symbol(sym.ENDVAR);}
    "@" { return symbol(sym.REF); }
    [A-Za-z]([A-Za-z_]|[0-9])* { return symbol(sym.ID, yytext()); }
}

<YYINITIAL> {

      {WhiteSpace}                   { /* ignore */ }
      {Comments}                     { /* ignore */ }
}

[^]  {
    throw new Error("Illegal character <"+yytext()+">"+" Linea "+Integer.parseInt(String.valueOf(yyline+1))+" Colonna "+Integer.parseInt(String.valueOf(yycolumn+1)));
}
