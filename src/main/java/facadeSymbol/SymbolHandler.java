package facadeSymbol;

import codeGenerator.*;
import errorHandler.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.Stack;

import static semantic.symbol.SymbolType.Bool;

public class SymbolHandler {
    private Memory memory;
    public SymbolTable symbolTable;
    public Stack<String> symbolStack = new Stack<>();

    public SymbolHandler(Memory memory) {
        symbolTable = new SymbolTable(memory);
        this.memory = memory;
    }

    public void addMethod(String methodeName) {
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodeName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodeName);
    }

    public void addMethod() {
        String methodeName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodeName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodeName);
    }

    public void addPid(Stack<Address> ss, Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {
                Symbol s = symbolTable.get(className, methodName, next.value);
                varType t = s.type == Bool ? varType.Bool : varType.Int;


                ss.push(new Address(s.address, t));

            } catch (Exception e) {
                ss.push(new Address(0, varType.Non));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    public void pop() {
        symbolStack.pop();
    }

    public Symbol getSymbol() {
        return symbolTable.get(symbolStack.pop(), symbolStack.pop());
    }

    public Address getAddress(Token next) {
        return symbolTable.get(next.value);
    }
    public Symbol getNextParam(String className, String methodName){
        return symbolTable.getNextParam(className, methodName);
    }
    public void setLastType(SymbolType type){
        symbolTable.setLastType(type);
    }
    public void addMethodParameter(){
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }
    public void methodReturn(Stack<Address> ss){
        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolTable.getMethodReturnType(symbolStack.peek(), methodName);
        varType temp = varType.Int;
        switch (t) {
            case Int:
                break;
            case Bool:
                temp = varType.Bool;
        }
        if (s.varType != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memory.add3AddressCode(Operation.ASSIGN, s, new Address(symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address, TypeAddress.Indirect), null);
        memory.add3AddressCode(Operation.JP, new Address(symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address), null, null);
    }
    public void addMethodLocalVariable(){
        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }
    public void addField(){
        symbolTable.addField(symbolStack.pop(), symbolStack.peek());
    }
    public void setSuperClass(){
        symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }
    public void addClass(){
        symbolTable.addClass(symbolStack.peek());
    }
    public varType call(String methodName, String className){
        try {
            symbolTable.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        varType t = varType.Int;
        switch (symbolTable.getMethodReturnType(className, methodName)) {
            case Int:
                t = varType.Int;
                break;
            case Bool:
                t = varType.Bool;
                break;
        }
        return t;
    }
    public int getMethodReturnAddress(String className, String methodName){
        return symbolTable.getMethodReturnAddress(className, methodName);
    }
    public int getMethodCallerAddress(String className, String methodName){
        return symbolTable.getMethodCallerAddress(className, methodName);
    }
    public int getMethodAddress(String className, String methodName){
        return symbolTable.getMethodAddress(className, methodName);
    }
    public void startCall(Stack<String> callStack){
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTable.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);
    }
}
