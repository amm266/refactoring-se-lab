package parser.actionManager;

import facadeGenerator.Generator;
import parser.Action;
import parser.ParseTable;
import parser.Rule;

import java.util.ArrayList;
import java.util.Stack;

public interface ActionManager {
    public boolean manage(Stack<Integer> parsStack, Action action, Generator generator, ArrayList<Rule> rules, ParseTable parseTable);
}
