package com.neowise.almond.parser.lexer

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.parser.lexer.TokenType.*
import com.neowise.almond.parser.source.Source

open class Lexer(private val source: Source) {

    companion object {
        private const val operatorChars = "+-*/%()[]{}=<>!&|.,^~?:;"
        private const val whitespaceChars = "\n \t\b\r"

        private val operators = tokenMapOf(
                PLUS, MINUS, STAR, SLASH, PERCENT,
                LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE,
                RBRACE, EQ, LT, GT, DOT, COMMA,
                CARET, TILDE, QUESTION, COLON,

                EXCL, AMP, BAR, EQEQ, EXCLEQ,
                LTEQ, GTEQ, PLUSEQ, MINUSEQ, STAREQ,
                SLASHEQ, PERCENTEQ, AMPEQ, CARETEQ,
                BAREQ, COLONCOLONEQ, LTLTEQ, GTGTEQ,

                GTGTGTEQ, PLUSPLUS, MINUSMINUS, COLONCOLON,
                AMPAMP, BARBAR, LTLT, GTGT, GTGTGT,
                FUNCTIONAL, SEMICOLON
        )

        private val keywords = tokenMapOf(
                USING, IF, ELSE, REPEAT, FOR, FOREACH, DO, UNTIL,
                BREAK, CONTINUE, ERROR, MATCH, STRUCT, FUNC,
                VAR, CONST, RETURN, CASE, DEFAULT, NEW, EXTRACT,
                TRUE, FALSE, THIS, IS, NIL
        )
    }

    private val tokens = mutableListOf<Token>()
    private val buffer = StringBuffer()

    fun tokenize(): List<Token> {
        while (!source.isEndReached) {
            try {
                val current: Char = source.peek(0)
                when {
                    Character.isDigit(current) -> {
                        tokenizeNumber()
                    }
                    Character.isJavaIdentifierStart(current) -> {
                        tokenizeWord()
                    }
                    current == '`' -> {
                        tokenizeExtendedWord()
                    }
                    current == '"' -> {
                        tokenizeText()
                    }
                    current == '#' -> {
                        source.next()
                        tokenizeHexNumber()
                    }
                    operatorChars.indexOf(current) != -1 -> {
                        tokenizeOperator()
                    }
                    whitespaceChars.indexOf(current) != -1 -> {
                        // whitespaces
                        source.next()
                    }
                    else -> {
                        throw error("illegal character '$current' at " + source.position())
                    }
                }
            } catch (e: ParseException) {
                source.next()
            }
        }
        return tokens
    }

    private fun tokenizeNumber() {
        clearBuffer()
        var current: Char = source.peek(0)
        if (current == '0' && (source.peek(1) == 'x' || source.peek(1) == 'X')) {
            source.next()
            source.next()
            tokenizeHexNumber()
            return
        }
        var isFloat = false
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) {
                    throw error("Invalid float number")
                }
                isFloat = true
            } else if (!Character.isDigit(current)) {
                break
            }
            buffer.append(current)
            current = source.next()
        }
        addToken(if (isFloat) FLOAT else INTEGER, buffer.toString())
    }

    private fun tokenizeHexNumber() {
        clearBuffer()
        var current: Char = source.peek(0)
        while (isHexNumber(current) || current == '_') {
            if (current != '_') {
                // allow _ symbol
                buffer.append(current)
            }
            current = source.next()
        }
        if (buffer.isNotEmpty()) {
            addToken(HEX_NUMBER, buffer.toString())
        }
    }

    private fun isHexNumber(current: Char): Boolean {
        return (Character.isDigit(current) || current in 'a'..'f' || current in 'A'..'F')
    }

    private fun tokenizeOperator() {
        var current: Char = source.peek(0)
        if (current == '/') {
            if (source.peek(1) == '/') {
                source.next()
                source.next()
                tokenizeComment()
                return
            } else if (source.peek(1) == '*') {
                source.next()
                source.next()
                tokenizeMultilineComment()
                return
            }
        }
        clearBuffer()
        while (true) {
            val text = buffer.toString()
            if (!operators.containsKey(text + current) && text.isNotEmpty()) {
                addToken(operators[text]!!)
                return
            }
            buffer.append(current)
            current = source.next()
        }
    }

    private fun tokenizeWord() {
        clearBuffer()
        var current: Char = source.peek(0)
        while (true) {
            if (!Character.isLetterOrDigit(current) && current != '_' && current != '$') {
                break
            }
            buffer.append(current)
            current = source.next()
        }
        val word = buffer.toString()
        if (keywords.containsKey(word)) {
            addToken(keywords[word]!!)
        } else {
            addToken(WORD, word)
        }
    }

    private fun tokenizeExtendedWord() {
        source.next() // skip `
        clearBuffer()
        var current: Char = source.peek(0)
        while (true) {
            if (current == '\u0000') {
                throw error("Reached end of file while parsing extended word.")
            }
            if (current == '\n' || current == '\r') {
                throw error("Reached end of line while parsing extended word.")
            }
            if (current == '`') {
                break
            }
            buffer.append(current)
            current = source.next()
        }
        source.next() // skip closing `
        addToken(WORD, buffer.toString())
    }

    private fun tokenizeText() {
        source.next() // skip "
        clearBuffer()
        var current: Char = source.peek(0)
        loop@ while (true) {
            if (current == '\u0000') {
                throw error("Reached end of file while parsing text string.")
            }
            if (current == '\\') {
                current = source.next()
                when (current) {
                    '"' -> {
                        current = source.next()
                        buffer.append('"')
                        continue@loop
                    }
                    '0' -> {
                        current = source.next()
                        buffer.append('\u0000')
                        continue@loop
                    }
                    'b' -> {
                        current = source.next()
                        buffer.append('\b')
                        continue@loop
                    }
//                    'f' -> {
//                        current = source.next()
//                        buffer.append('\f')
//                        continue@loop
//                    }
                    'n' -> {
                        current = source.next()
                        buffer.append('\n')
                        continue@loop
                    }
                    'r' -> {
                        current = source.next()
                        buffer.append('\r')
                        continue@loop
                    }
                    't' -> {
                        current = source.next()
                        buffer.append('\t')
                        continue@loop
                    }
                    'u' -> {
                        val rollbackPosition = source.rollback()
                        while (current == 'u') {
                            current = source.next()
                        }
                        var escapedValue = 0
                        var i = 12
                        while (i >= 0 && escapedValue != -1) {
                            escapedValue = if (isHexNumber(current)) {
                                escapedValue or (Character.digit(current, 16) shl i)
                            } else {
                                -1
                            }
                            current = source.next()
                            i -= 4
                        }
                        if (escapedValue >= 0) {
                            buffer.append(escapedValue.toChar())
                        } else {
                            // rollback
                            buffer.append("\\u")
                            source.initRollback(rollbackPosition)
                        }
                        continue@loop
                    }
                }
                buffer.append('\\')
                continue
            }
            if (current == '"') {
                break
            }
            buffer.append(current)
            current = source.next()
        }
        source.next() // skip closing "
        addToken(TEXT, buffer.toString())
    }

    private fun tokenizeComment() {
        var current: Char = source.peek(0)
        while ("\r\n\u0000".indexOf(current) == -1) {
            current = source.next()
        }
    }

    private fun tokenizeMultilineComment() {
        var current: Char = source.peek(0)
        while (true) {
            if (current == '\u0000') {
                throw error("Reached end of file while parsing multiline comment")
            }
            if (current == '*' && source.peek(1) == '/') {
                break
            }
            current = source.next()
        }
        source.next() // *
        source.next() // /
    }

    private fun clearBuffer() {
        buffer.setLength(0)
    }

    private fun addToken(type: TokenType) {
        addToken(type, "")
    }

    private fun addToken(type: TokenType, text: String) {
        tokens.add(Token(text, type, source.row, source.col))
    }
}