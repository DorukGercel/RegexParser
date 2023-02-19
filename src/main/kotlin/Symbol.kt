// Any char
const val ANY_CHAR_REGEX = '.'
const val ANY_CHAR_POSTFIX = '@'
// Path
const val CONCAT = '.'
const val UNION = '|'
// Closure
const val KLEN_STAR = '*'
const val ZERO_OR_ONE = '?'
const val ONE_OR_MORE = '+'
// Parenthesis
const val PARENTHESIS_OPEN = '('
const val PARENTHESIS_CLOSE = ')'
const val SQUARE_PARENTHESIS_OPEN = '['
const val SQUARE_PARENTHESIS_CLOSE = ']'

fun isSpecialControlPostfixToken(token: Char): Boolean {
    return token == CONCAT || token == UNION || token == KLEN_STAR || token == ZERO_OR_ONE || token == ONE_OR_MORE
}

fun getSpecialTokenPrecedence(token: Char): Int {
    return when(token) {
        UNION -> 1
        CONCAT -> 2
        KLEN_STAR, ZERO_OR_ONE, ONE_OR_MORE -> 3
        else -> 0
    }
}