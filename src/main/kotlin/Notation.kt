import java.util.*
import kotlin.collections.ArrayList

class Notation {
    companion object {
        private fun insertExplicitConcatOperator(exp: String): String {
            val output = ArrayList<Char>()
            for(i in exp.indices) {
                val token = exp[i]
                output += token
                if (token == '(' || token == '|') {
                    continue
                }
                if (i < exp.length - 1) {
                    val lookahead = exp[i + 1]
                    if (lookahead == '*' || lookahead == '?' || lookahead == '+' || lookahead == '|' || lookahead == ')') {
                        continue
                    }
                    output += '.'
                }
            }
            return String(output.toCharArray())
        }

        private fun regexToInfix(exp: String): String {
            val infixNotation: MutableList<Char> = LinkedList()
            var isAnyOf = false
            var isFirstAnyOf = false

            // Sanitize the regex for the infix notation
            for (i in exp.indices) {
                if (exp[i] == SQUARE_PARENTHESIS_OPEN) {
                    infixNotation += PARENTHESIS_OPEN
                    isAnyOf = true
                    isFirstAnyOf = true
                } else if (exp[i] == SQUARE_PARENTHESIS_CLOSE) {
                    infixNotation += PARENTHESIS_CLOSE
                    isAnyOf = false
                } else if(isAnyOf) {
                    if(!isFirstAnyOf) {
                        infixNotation += UNION
                    } else {
                        isFirstAnyOf = false
                    }
                    infixNotation += exp[i]
                } else if(exp[i] == ANY_CHAR_REGEX) {
                    infixNotation += ANY_CHAR_POSTFIX
                } else {
                    infixNotation += exp[i]
                }
            }

            // Sanitize the output for or parenthesis
            var i = 0
            while (i < infixNotation.count()) {
                if(infixNotation[i] == UNION) {
                    if(infixNotation[i-1] != PARENTHESIS_CLOSE) {
                        infixNotation.add(i-1, PARENTHESIS_OPEN)
                    } else {
                        var j = i-1
                        while(j > 0 && infixNotation[j] != PARENTHESIS_OPEN) {
                            j--
                        }
                        infixNotation.add(j, PARENTHESIS_OPEN)
                    }
                    i += 1
                    if(infixNotation[i+1] != PARENTHESIS_OPEN) {
                        infixNotation.add(i+2, PARENTHESIS_CLOSE)
                    } else {
                        var j = i+1
                        while(j < infixNotation.count() && infixNotation[j] != PARENTHESIS_CLOSE) {
                            j++
                        }
                        infixNotation.add(j+1, PARENTHESIS_CLOSE)
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
                if (isSpecialControlPostfixToken(token)) {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != PARENTHESIS_OPEN && (getSpecialTokenPrecedence(operatorStack.last()) >= getSpecialTokenPrecedence(token))) {
                        output += operatorStack.removeLast()
                    }
                    operatorStack.add(token)
                } else if (token == PARENTHESIS_OPEN || token == PARENTHESIS_CLOSE) {
                    if (token == PARENTHESIS_OPEN) {
                        operatorStack.add(token)
                    } else {
                        while (operatorStack.last() != PARENTHESIS_OPEN) {
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
