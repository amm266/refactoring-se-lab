package parser.actionManager;

import facadeGenerator.Generator;
import parser.Action;
import parser.ParseTable;
import parser.Rule;

import java.util.ArrayList;
import java.util.Stack;

public class ShiftActionManager implements ActionManager{
    @Override
    public boolean manage(Stack<Integer> parsStack, Action action, Generator generator, ArrayList<Rule> rules, ParseTable parseTable) {
        parsStack.push(action.number);
        generator.nextToken();
        return false;
    }
}
