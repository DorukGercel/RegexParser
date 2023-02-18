fun main() {
    // Custom input from app
    println("Enter pattern: ")
    val pattern = readln()
    println("Enter word: ")
    val word = readln()

    // Run app
    val match = PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word)
    println("Match: $match")
}
