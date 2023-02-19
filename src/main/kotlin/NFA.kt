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
            first.endState.addEpsilonTransition(second.startState)
            first.endState.isEndState = false

            return NFA(first.startState, second.endState)
        }

        fun union(first: NFA, second: NFA): NFA {
            val startState = State(false)
            startState.addEpsilonTransition(first.startState)
            startState.addEpsilonTransition(second.startState)

            val endState = State(true)
            first.endState.addEpsilonTransition(endState)
            second.endState.addEpsilonTransition(endState)
            first.endState.isEndState = false
            second.endState.isEndState = false

            return NFA(startState, endState)
        }

        fun closure(nfa: NFA): NFA {
            val startState = State(false)
            val endState = State(true)

            startState.addEpsilonTransition(endState)
            startState.addEpsilonTransition(nfa.startState)

            nfa.endState.addEpsilonTransition(endState)
            nfa.endState.addEpsilonTransition(nfa.startState)
            nfa.endState.isEndState = false

            return NFA(startState, endState)
        }

        fun zeroOrOne(nfa: NFA): NFA {
            val startState = State(false)
            val endState = State(true)

            startState.addEpsilonTransition(endState)
            startState.addEpsilonTransition(nfa.startState)

            nfa.endState.addEpsilonTransition(endState)
            nfa.endState.isEndState = false

            return NFA(startState, endState)
        }

        fun oneOrMore(nfa: NFA): NFA {
            val startState = State(false)
            val endState = State(true)

            startState.addEpsilonTransition(nfa.startState)
            nfa.endState.addEpsilonTransition(endState)
            nfa.endState.addEpsilonTransition(nfa.startState)
            nfa.endState.isEndState = false

            return NFA(startState, endState)
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
                    KLEN_STAR -> {
                        stack.add(NFA.closure(stack.removeLast()))
                    }
                    ONE_OR_MORE -> {
                        stack.add(NFA.oneOrMore(stack.removeLast()))
                    }
                    ZERO_OR_ONE -> {
                        stack.add(NFA.zeroOrOne(stack.removeLast()))
                    }
                    UNION -> {
                        val rightState = stack.removeLast()
                        val leftState = stack.removeLast()
                        stack.add(NFA.union(leftState, rightState))
                    }
                    CONCAT -> {
                        val rightState = stack.removeLast()
                        val leftState = stack.removeLast()
                        stack.add(NFA.concat(leftState, rightState))
                    }
                    else -> {
                        stack.add(NFA(token))
                    }
                }
            }
            return stack.removeLast()
        }
    }
}
