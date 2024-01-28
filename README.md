# Corso di Compilatori A.A. 2023/24

## Introduzione
Il seguente documento contiene le specifiche del linguaggio Toy2.
- La sezione delle specifiche lessicali contiene la lista dei token con i rispettivi pattern.
- La sezione delle specifiche sintattiche contiene la grammatica utilizzata con tutti i non terminali ed i terminali. Questa sezione contiene anche la tabella delle precedenze e i nodi dell'Abstract Syntax Tree.
- La sezione delle specifiche semantiche riporta le regole di Type Checking e le tabelle per gli operatori.

## Specifiche Lessicali
| Token            | Pattern                        |
|------------------|--------------------------------|
| VAR              | var                            |
| COLON            | :                              |
| ASSIGN           | \^\;=                           |
| SEMI             | ;                              |
| COMMA            | ,                              |
| TRUE             | true                           |
| FALSE            | false                          |
| REAL             | real                           |
| INTEGER          | integer                        |
| STRING           | string                         |
| BOOLEAN          | boolean                        |
| RETURN           | return                         |
| FUNCTION         | func                           |
| TYPERETURN       | $->$                           |
| LPAR             | (                              |
| RPAR             | )                              |
| PROCEDURE        | proc                           |
| WHILE            | while                          |
| ENDPROCEDURE     | endproc                        |
| ENDFUNCTION      | endfunc                        |
| OUT              | out                            |
| WRITE            | $-->                           |
| WRITERETURN      | $-->!                          |
| DOLLARSIGN       | \$                            |
| READ             | $<--                           |
| IF               | if                             |
| THEN             | then                           |
| ELSE             | else                           |
| ENDIF            | endif                          |
| ELIF             | elseif                         |
| DO               | do                             |
| ENDWHILE         | endwhile                       |
| PLUS             | +                              |
| MINUS            | -                              |
| TIMES            | *                              |
| DIV              | /                              |
| EQ               | =                              |
| NE               | $<>                            |
| LT               | $<                             |
| LE               | $<=$                           |
| GT               | $>                             |
| GE               | $>=$                           |
| AND              | \&\&                           |
| OR               | $||                            |
| NOT              | !                              |
| ENDVAR           | $\backslash \backslash$        |
| REF              | @                              |
| ID               | [a-zA-Z] ([a-zA-Z] $|$ [0-9] $|$ \_)*  |
| STRING\_CONST    | $\backslash$" $\sim$$\backslash$"| 
| INTEGER\_CONST   | [0-9]+                         |
| REAL\_CONST      | [0-9]+ ("." [0-9]+)?            |

## Specifiche Sintattiche
### Grammatica
```plaintext
Program ::= IterNoProcedure Procedure Iter

IterNoProcedure ::= VarDecls IterNoProcedure
    | Function IterNoProcedure
    | /* empty */

Iter ::= VarDecl Iter
      | Function Iter
      | Procedure Iter
      | /* empty */

VarDecl ::= VAR Decls

Decls ::= Ids COLON Type SEMI Decls
        | Ids ASSIGN Consts SEMI Decls
        | Ids COLON Type SEMI ENDVAR
        | Ids ASSIGN Consts SEMI ENDVAR

Ids ::= ID COMMA Ids
      | ID

Consts ::= Const COMMA Consts
        | Const

Const ::= REAL\_CONST
        | INTEGER\_CONST
        | STRING\_CONST
        | TRUE
        | FALSE

Type ::= REAL
        | INTEGER
        | STRING
        | BOOLEAN

Function ::= FUNCTION ID LPAR FuncParams RPAR TYPERETURN Types COLON Body ENDFUNCTION

FuncParams ::= ID COLON Type OtherFuncParams
            | /* empty */

OtherFuncParams ::= COMMA ID COLON Type OtherFuncParams
                | /* empty */

Types ::= Type COMMA Types
        | Type

Procedure ::= PROCEDURE ID LPAR ProcParams RPAR COLON Body ENDPROCEDURE

ProcParams ::= ProcParamId COLON Type OtherProcParams
            | /* empty */

OtherProcParams ::= COMMA ProcParamId COLON Type OtherProcParams
                | /* empty */

ProcParamId ::= ID
            | OUT ID

Body ::= VarDecl Body
        | Stat Body
        | /* empty */

Stat ::= Ids ASSIGN Exprs SEMI
        | ProcCall SEMI
        | RETURN Exprs SEMI
        | WRITE IOArgs SEMI
        | WRITERETURN IOArgs SEMI
        | READ IOArgs SEMI
        | IfStat SEMI
        | WhileStat SEMI

FunCall ::= ID LPAR Exprs RPAR
          | ID LPAR RPAR

ProcCall ::= ID LPAR ProcExprs RPAR
          | ID LPAR RPAR

IfStat ::= IF Expr THEN Body Elifs Else ENDIF

Elifs ::= Elif Elifs
        | /* empty */

Elif ::= ELIF Expr THEN Body

Else ::= ELSE Body
        | /* empty */

WhileStat ::= WHILE Expr DO Body ENDWHILE

IOArgs ::= IOArgsConcat IOArgs
        | DOLLARSIGN LPAR Expr RPAR IOArgs
        | /* empty */

IOArgsConcat ::= IOArgsConcat PLUS IOArgsConcat
        | STRING\_CONST

ProcExprs ::= Expr COMMA ProcExprs
          | REF ID COMMA ProcExprs
          | Expr
          | REF ID

Exprs ::= Expr COMMA Exprs
        | Expr

Expr ::= FunCall
        | REAL\_CONST
        | INTEGER\_CONST
        | STRING\_CONST
        | ID
        | TRUE
        | FALSE
        | Expr PLUS Expr
        | Expr MINUS Expr
        | Expr TIMES Expr
        | Expr DIV Expr
        | Expr AND Expr
        | Expr OR Expr
        | Expr GT Expr
        | Expr GE Expr
        | Expr LT Expr
        | Expr LE Expr
        | Expr EQ Expr
        | Expr NE Expr
        | LPAR Expr RPAR \%PAR
        | MINUS Expr \%UMINUS
        | NOT Expr
```


        
# Tabella delle precedenze
La priorità della seguente tabella viene specificata come nell'ordine fornito da Java CUP, riga più in basso equivale a priorità più alta.

| Token | Associatività |
|-------|---------------|
| OR    | SINISTRA      |
| AND   | SINISTRA      |
| NOT   | DESTRA        |
| AND   | SINISTRA      |
| EQ NE | SINISTRA      |
| EQ NE LE GE GT LT | NON ASSOCIATIVA |
| PLUS MINUS | SINISTRA |
| TIMES DIV   | SINISTRA |
| PROCEDURE   | SINISTRA |
