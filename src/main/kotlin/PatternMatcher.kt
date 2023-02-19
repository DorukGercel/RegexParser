class PatternMatcher {
    companion object {
        private fun addNextState(state: State, nextStates: MutableList<State>, visited: MutableList<State>) {
            if (state.epsilonTransitions.isNotEmpty()) {
                // add states with epsilon transitions
                for (st in state.epsilonTransitions) {
                    if (!visited.contains(st)) {
                        visited.add(st)
                        addNextState(st, nextStates, visited)
                    }
                }
            } else {
                nextStates.add(state)
            }
        }

        fun match(nfa: NFA, word: String): Boolean {
            var currentStates: MutableList<State> = ArrayList()
            // add initial possibly visited states
            addNextState(nfa.startState, currentStates, mutableListOf())
            for (token in word) {
                val nextStatesList: MutableList<State> = mutableListOf()
                for (st in currentStates) {
                    // find token transition
                    var nextState = st.transition[token]
                    if (nextState != null) {
                        addNextState(nextState, nextStatesList, mutableListOf())
                    }
                    // find any char token transition
                    nextState = st.transition[ANY_CHAR_POSTFIX]
                    if (nextState != null) {
                        addNextState(nextState, nextStatesList, mutableListOf())
                    }
                }
                // move to next states
                currentStates = nextStatesList
            }
            return currentStates.find { it.isEndState } != null
        }
    }
}