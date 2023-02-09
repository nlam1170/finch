import java_cup.runtime.*;

%%

%cup
%line
%column
%unicode
%class Lexer

%{

Symbol newSym(int tokenId) {
	return new Symbol(tokenId, yyline, yycolumn);
}

Symbol newSym(int tokenId, Object value) {
	return new Symbol(tokenId, yyline, yycolumn, value);
}

%}

slash	= \\
asterisk = "*"
letter	= [A-Za-z]
digit	= [0-9]
id	= {letter}[{letter}{digit}]*

intl	= {digit}+
floatl = {intl}\.{intl}
char = [^\\\n\t\"\']|\\.
str = {char}*
charl = \'{char}\'
strl = \"{str}\"

whitespace = [ \n\t\r]
comment	= {slash}{slash}.*\n
multiline_comment = {slash}{asterisk}(([^*\\]|[^\\][*]|[\\][^*])*{whitespace}*){asterisk}{slash}

%%

class					{return newSym(sym.CLASS, "class");}
else					{return newSym(sym.ELSE, "else");}
if						{return newSym(sym.IF, "if");}
while					{return newSym(sym.WHILE, "while");}
return 				{return newSym(sym.RETURN, "return");}
";"						{return newSym(sym.SEMICOLON, ";");}
"="   				{return newSym(sym.ASSIGN, "=");}
","						{return newSym(sym.COMMA, ",");}
"("						{return newSym(sym.L_PAREN, "(");}
")"						{return newSym(sym.R_PAREN, ")");}
"["						{return newSym(sym.L_SQUARE, "[");}
"]"						{return newSym(sym.R_SQUARE, "]");}
"{"						{return newSym(sym.L_CURLY, "{");}
"}"						{return newSym(sym.R_CURLY, "}");}
"~"						{return newSym(sym.NOT, "~");}
"?"						{return newSym(sym.Q_MARK, "?");}
":"						{return newSym(sym.COLON, ":");}
read					{return newSym(sym.READ, "read");}
print					{return newSym(sym.PRINT, "print");}
printline			{return newSym(sym.PRINTLN, "printline");}
"++"					{return newSym(sym.INCREASE, "++");}
"--"					{return newSym(sym.DECREASE, "--");}
"*"						{return newSym(sym.MULTIPLY, "*");}
"/"						{return newSym(sym.DIVIDE, "/");}
"+"						{return newSym(sym.ADD, "+");}
"-"						{return newSym(sym.SUBTRACT, "-");}
"<"						{return newSym(sym.LT, "<");}
">"						{return newSym(sym.GT, ">");}
"=="					{return newSym(sym.EQUALS, "==");}
"<="					{return newSym(sym.LTE, "<=");}
">="					{return newSym(sym.GTE, ">=");}
"<>"					{return newSym(sym.NOT_EQUALS, "<>");}
"||"					{return newSym(sym.OR, "||");}
"&&"					{return newSym(sym.AND, "&&");}
true					{return newSym(sym.TRUE, "true");}
false					{return newSym(sym.FALSE, "false");}
void					{return newSym(sym.VOID, "void");}
int						{return newSym(sym.INT, "int");}
float					{return newSym(sym.FLOAT, "float");}
bool					{return newSym(sym.BOOL, "bool");}
char					{return newSym(sym.CHAR, "char");}
final					{return newSym(sym.FINAL, "final");}
{intl}			{return newSym(sym.INTL, yytext());}
{floatl}		{return newSym(sym.FLOATL, yytext());}
{charl}			{return newSym(sym.CHARL, yytext());}
{strl}			{return newSym(sym.STRL, yytext());}
{id}			{return newSym(sym.ID, yytext());}
{whitespace}	{/* whitespace */}
{comment}			{/* comment */}
{multiline_comment}  {/* multiline comment */}

. {System.out.println("Illegal character, " + yytext() + " line:" + yyline + " col:" + yychar);}