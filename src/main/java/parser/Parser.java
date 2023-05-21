package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import errorHandler.ErrorHandler;
import facadeGenerator.Generator;
import parser.actionManager.AcceptActionManager;
import parser.actionManager.ActionManager;
import parser.actionManager.ReduceActionManager;
import parser.actionManager.ShiftActionManager;

public class Parser {
    private ArrayList<Rule> rules;
    private Stack<Integer> parsStack;
    private ParseTable parseTable;

    public Parser() {
        parsStack = new Stack<Integer>();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<Rule>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startParse(java.util.Scanner sc) {
        Generator generator = new Generator(sc);
        generator.nextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                ActionManager actionManager;
                currentAction = parseTable.getActionTable(parsStack.peek(), generator.getToken());

                switch (currentAction.action) {
                    case shift:
                        actionManager = new ShiftActionManager();
                        break;
                    case reduce:
                        actionManager = new ReduceActionManager();
                        break;
                    case accept:
                        actionManager = new AcceptActionManager();
                        break;
                    default:
                        actionManager = new ShiftActionManager();
                }
                finish = actionManager.manage(parsStack, currentAction, generator, rules, parseTable);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        if (!ErrorHandler.hasError) generator.printMemory();
    }
}
