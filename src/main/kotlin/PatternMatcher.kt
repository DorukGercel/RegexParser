class PatternMatcher {
    companion object {
        private fun addNextState(state: State, nextStates: MutableList<State>, visited: MutableList<State>) {
            if (state.epsilonTransitions.isNotEmpty()) {
                for (st in state.epsilonTransitions) {
                    if (visited.find { it == st } == null) {
                        visited.add(st);
                        addNextState(st, nextStates, visited);
                    }
                }
            } else {
                nextStates.add(state);
            }
        }

        fun match(nfa: NFA, word: String): Boolean {
            var currentStates: MutableList<State> = ArrayList()
            addNextState(nfa.startState, currentStates, mutableListOf())
            for (symbol in word) {
                val nextStatesList: MutableList<State> = mutableListOf()
                for (st in currentStates) {
                    var nextState = st.transition[symbol];
                    if (nextState != null) {
                        addNextState(nextState, nextStatesList, mutableListOf());
                    }
                    nextState = st.transition['@'];
                    if (nextState != null) {
                        addNextState(nextState, nextStatesList, mutableListOf());
                    }
                }
                currentStates = nextStatesList
            }
            return currentStates.find { it.isEndState } != null
        }
    }
}