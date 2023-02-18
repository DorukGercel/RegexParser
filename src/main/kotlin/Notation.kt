import java.util.*
import kotlin.collections.ArrayList

class Notation {
    companion object {
        private fun getPrecedence(op: Char): Int {
            return when(op) {
                '|' -> 1
                '.' -> 2
                '?', '*', '+' -> 3
                else -> 0
            }
        }
        private fun getAlphabet(exp: String): List<Char> {
            val output = mutableSetOf<Char>()
            for (token in exp) {
                if (!(token == '.' || token == '|' || token == '*' || token == '?' || token == '+' || token == '(' || token == ')' || token == '[' || token == ']')) {
                    output += token
                }
            }
            return output.toList()
        }

        private fun getAlphabetStatement(alphabet: List<Char>): String {
            val output = ArrayList<Char>()
            var isFirst = true
            output += '('
            for(token in alphabet) {
                if(!isFirst) {
                    output += '|'
                } else {
                    isFirst = false
                }
                output += token
            }
            output += ')'
            return String(output.toCharArray())
        }

        private fun insertExplicitConcatOperator(exp: String): String {
            val output = ArrayList<Char>()
            for(i in exp.indices) {
                val token = exp[i]
                output += token
                if (token == '(' || token == '|') {
                    continue;
                }
                if (i < exp.length - 1) {
                    val lookahead = exp[i + 1];
                    if (lookahead == '*' || lookahead == '?' || lookahead == '+' || lookahead == '|' || lookahead == ')') {
                        continue;
                    }
                    output += '.';
                }
            }
            return String(output.toCharArray())
        };

        private fun regexToInfix(exp: String): String {
            val alphabetStatement = getAlphabetStatement(getAlphabet(exp))
            val infixNotation: MutableList<Char> = LinkedList()
            var isAnyOf = false
            var isFirstAnyOf = false

            // Sanitize the regex for the infix notation
            for (i in exp.indices) {
                if (exp[i] == '[') {
                    infixNotation += '('
                    isAnyOf = true
                    isFirstAnyOf = true
                } else if (exp[i] == ']') {
                    infixNotation += ')'
                    isAnyOf = false
                } else if(isAnyOf) {
                    if(!isFirstAnyOf) {
                        infixNotation += '|'
                    } else {
                        isFirstAnyOf = false
                    }
                    infixNotation += exp[i]
                } else if(exp[i] == '.') {
                    infixNotation += '@'
                } else {
                    infixNotation += exp[i]
                }
            }

            // Sanitize the output for or parenthesis
            var i = 0
            while (i < infixNotation.count()) {
                if(infixNotation[i] == '|') {
                    if(infixNotation[i-1] != ')') {
                        infixNotation.add(i-1, '(')
                    } else {
                        var j = i-1
                        while(j > 0 && infixNotation[j] != '(') {
                            j--
                        }
                        infixNotation.add(j, '(')
                    }
                    i += 1
                    if(infixNotation[i+1] != '(') {
                        infixNotation.add(i+2, ')')
                    } else {
                        var j = i+1
                        while(j < infixNotation.count() && infixNotation[j] != ')') {
                            j++
                        }
                        infixNotation.add(j+1, ')')
                    }
                }
                i += 1
            }

            return insertExplicitConcatOperator(String(infixNotation.toCharArray()))
        }

        private fun infixToPostfix(exp: String): String {
            val output = ArrayList<Char>()
            val operatorStack = ArrayList<Char>()
            for (token in exp) {
                if (token == '.' || token == '|' || token == '*' || token == '?' || token == '+') {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != '(' && (getPrecedence(operatorStack.last()) >= getPrecedence(token))) {
                        output += operatorStack.removeLast()
                    }
                    operatorStack.add(token)
                } else if (token == '(' || token == ')') {
                    if (token == '(') {
                        operatorStack.add(token)
                    } else {
                        while (operatorStack.last() != '(') {
                            output += operatorStack.removeLast()
                        }
                        operatorStack.removeLast()
                    }
                } else {
                    output += token
                }
            }
            while (operatorStack.isNotEmpty()) {
                output += operatorStack.removeLast()
            }
            return String(output.toCharArray())
        }

        fun regexToPostfix(exp: String): String {
            return infixToPostfix(regexToInfix(exp))
        }
    }
}
