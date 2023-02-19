import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PatternMatcherTest {
    @Test
    fun singleCharMatch() {
        var pattern = "a"
        var word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "."
        word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleCharNonMatch() {
        val pattern = "a"
        val word = "b"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleKlenStarMatch() {
        var pattern = "a*"
        var word = ""
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "aa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = ".*"
        word = ""
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ab"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleOneOrMoreMatch() {
        var pattern = "a+"
        var word = ""
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "aa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = ".+"
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ba"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleZeroOrOneMatch() {
        var pattern = "a?"
        var word = ""
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "aa"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = ".?"
        word = ""
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun rangeMatch() {
        val pattern = "[abc]"
        var word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "aa"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun simpleConcatMatch() {
        var pattern = "aaa"
        var word = "aaa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "..."
        word = "aaa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "aba"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "abc"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun simpleConcatNonMatch() {
        val pattern = "aaa"
        val word = "aab"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleCharUnionMatch() {
        var pattern = "a|b|c|d"
        var word = "a"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "c"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "d"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "aa|b|c|d"
        word = "aa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ab"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ac"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ad"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "aa|b|cd"
        word = "aad"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "abd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "acd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "a|bc|d"
        word = "ac"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "a|bc|d"
        word = "bd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun singleCharUnionNonMatch() {
        var pattern = "a|b|c|d"
        var word = "e"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "a|bc|d"
        word = "ab"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "aa|b|cd"
        word = "aac"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun multiCharUnionMatch() {
        var pattern = "(aa)|b|c|(dd)"
        var word = "aa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "b"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "c"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "dd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)"
        word = "axa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "exe"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|(ixi)"
        word = "axa"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "exe"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ixi"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|(ixi)ddd"
        word = "axaddd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "exeddd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ixiddd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|ddd"
        word = "axadd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "exedd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "ddd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "d(axa)|(exe)|ddd"
        word = "daxadd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "dexedd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "dddd"
        assertTrue(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }

    @Test
    fun multiCharUnionNonMatch() {
        var pattern = "(aa)|b|c|(dd)"
        var word = "aab"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "bdd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)"
        word = "axe"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "axe"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|(ixi)"
        word = "exa"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|(ixi)ddd"
        word = "dddd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "(axa)|(exe)|ddd"
        word = "dddd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "axedd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        pattern = "d(axa)|(exe)|ddd"
        word = "axadd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
        word = "dexeddd"
        assertFalse(PatternMatcher.match(NFABuilder.fromPostfixToNFA(Notation.regexToPostfix(pattern)), word))
    }
}