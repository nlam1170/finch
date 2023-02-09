JAVA=java 
JAVAC=javac 
JFLEX=$(JAVA) -jar ./resources/jflex-full-1.8.2.jar
CUPJAR=./resources/java-cup-11b.jar 
CUP=$(JAVA) -jar $(CUPJAR)
CP=.:$(CUPJAR)

default: run

.SUFFIXES: $(SUFFIXES) .class .java 

.java.class: 
			$(JAVAC) -cp $(CP) $*.java

FILE = Lexer.java parser.java sym.java \
       TypeChecking.java LexerTest.java \
	   AllType.java Arg.java ArgDecl.java BinaryOp.java Check.java CheckException.java Expression.java FieldDecl.java MemberDecl.java MethodDecl.java Name.java Program.java \
	   Statement.java SymbolTable.java Token.java Type.java Variable.java 

run: build
	   @for f in ./tests/*.as; do \
	   		echo "File Tested: $$f" >> output.txt; \
	   		$(JAVA) -cp $(CP) TypeChecking $$f >> output.txt; \
		done;


build: Lexer.java parser.java $(FILE:java=class)

dump: Lexer.java parserD.java $(FILE:java=class)

clean: 
	rm -f *.class *.bak Lexer.java parser.java sym.java output.txt

Lexer.java: grammar.jflex
			$(JFLEX) grammar.jflex

parser.java: tokens.cup
			 $(CUP) -interface < tokens.cup 

parserD.java: tokens.cup
			  $(CUP) -interface -dump < tokens.cup