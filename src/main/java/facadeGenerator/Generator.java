package facadeGenerator;

import codeGenerator.CodeGenerator;
import parser.Rule;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

public class Generator {
    private Token token;
    private scanner.lexicalAnalyzer lexicalAnalyzer;
    private CodeGenerator codeGenerator;

    public Generator(java.util.Scanner scanner) {
        codeGenerator = new CodeGenerator();
        lexicalAnalyzer = new lexicalAnalyzer(scanner);
    }

    public Token getToken() {

        return token;
    }
    public void nextToken(){
        token = lexicalAnalyzer.getNextToken();
    }

    public void generate(Rule rule) {
        codeGenerator.semanticFunction(rule.semanticAction, token);
    }
    public void printMemory(){
        codeGenerator.printMemory();
    }
}
