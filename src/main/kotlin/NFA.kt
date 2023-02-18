class State(var isEndState: Boolean) {
    var transition: MutableMap<Char,State> = HashMap()
    val epsilonTransitions: MutableList<State> = ArrayList()

    fun addTransition(symbol: Char, to: State) {
        this.transition[symbol] = to
    }
    fun addEpsilonTransition(to: State) {
        this.epsilonTransitions.add(to)
    }
}

class NFA(var startState: State, var endState: State) {
    constructor(): this(State(false), State(true)) {
        this.startState.addEpsilonTransition(this.endState)
    }

    constructor(symbol: Char): this(State(false), State(true)) {
        this.startState.addTransition(symbol, this.endState)
    }

    companion object Operation {
        fun concat(first: NFA, second: NFA): NFA {
            first.endState.addEpsilonTransition(second.startState);
            first.endState.isEndState = false

            return NFA(first.startState, second.endState)
        }

        fun union(first: NFA, second: NFA): NFA {
            val start = State(false);
            start.addEpsilonTransition(first.startState);
            start.addEpsilonTransition(second.startState);

            val end = State(true);
            first.endState.addEpsilonTransition(end);
            second.endState.addEpsilonTransition(end);
            first.endState.isEndState = false;
            second.endState.isEndState = false;

            return NFA(start, end)
        }

        fun closure(nfa: NFA): NFA {
            val start = State(false);
            val end = State(true);

            start.addEpsilonTransition(end);
            start.addEpsilonTransition(nfa.startState);

            nfa.endState.addEpsilonTransition(end);
            nfa.endState.addEpsilonTransition(nfa.startState);
            nfa.endState.isEndState = false;

            return NFA(start, end)
        }

        fun zeroOrOne(nfa: NFA): NFA {
            val start = State(false)
            val end = State(true)

            start.addEpsilonTransition(end)
            start.addEpsilonTransition(nfa.startState)

            nfa.endState.addEpsilonTransition(end)
            nfa.endState.isEndState = false;

            return NFA(start, end)
        }

        fun oneOrMore(nfa: NFA): NFA {
            val start = State(false)
            val end = State(true)

            start.addEpsilonTransition(nfa.startState)
            nfa.endState.addEpsilonTransition(end)
            nfa.endState.addEpsilonTransition(nfa.startState)
            nfa.endState.isEndState = false;

            return NFA(start, end)
        }
    }
}

class NFABuilder {
    companion object {
        fun fromPostfixToNFA(postfixExp: String): NFA {
            if(postfixExp.isEmpty()) {
                return NFA()
            }
            val stack = ArrayList<NFA>()
            for (token in postfixExp) {
                when (token) {
                    '*' -> {
                        stack.add(NFA.closure(stack.removeLast()));
                    }
                    '+' -> {
                        stack.add(NFA.oneOrMore(stack.removeLast()));
                    }
                    '?' -> {
                        stack.add(NFA.zeroOrOne(stack.removeLast()));
                    }
                    '|' -> {
                        val right = stack.removeLast()
                        val left = stack.removeLast()
                        stack.add(NFA.union(left, right));
                    }
                    '.' -> {
                        val right = stack.removeLast()
                        val left = stack.removeLast()
                        stack.add(NFA.concat(left, right));
                    }
                    else -> {
                        stack.add(NFA(token));
                    }
                }
            }
            return stack.removeLast();
        }
    }
}
