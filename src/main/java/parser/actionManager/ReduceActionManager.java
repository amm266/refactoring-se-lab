package parser.actionManager;

import facadeGenerator.Generator;
import parser.Action;
import parser.ParseTable;
import parser.Rule;

import java.util.ArrayList;
import java.util.Stack;

public class ReduceActionManager implements ActionManager{
    @Override
    public boolean manage(Stack<Integer> parsStack, Action action, Generator generator, ArrayList<Rule> rules, ParseTable parseTable) {
        Rule rule = rules.get(action.number);
        for (int i = 0; i < rule.RHS.size(); i++) {
            parsStack.pop();
        }
        parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
        try {
            generator.generate(rule);
        } catch (Exception e) {
        }
        return false;
    }
}
