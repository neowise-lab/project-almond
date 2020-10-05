package com.neowise.almond.parser

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.parser.ast.*
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*
import com.neowise.almond.parser.lexer.Lexer
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.parser.lexer.TokenType
import com.neowise.almond.parser.source.Source
import java.io.EOFException
import java.util.*


class Parser {

    private lateinit var tokens: List<Token>

    private var pos = 0
    private var size = 0

    private val errors = ArrayList<ParseException>()

    fun parse(location: String, name: String, source: Source): Program {

        this.tokens = Lexer(source).tokenize()
        this.size = tokens.size

        val program = Program(location, name)
        try {
            //В начале определяются используемые модули
            while (match(TokenType.USING)) {
                program += using()
            }

            //Вторыми структуры, функции и поля
            loop@ while(true) {
                program += when {
                    match(TokenType.STRUCT) -> struct()
                    match(TokenType.DEFINE) -> functionOrVar()
                    else -> break@loop
                }
            }
        }
        catch(e: EOFException) {
            println("end of file reached!")
        }
//
//        //Дальше языковые конструкции
//        loop@ while(true) {
//            try {
//                program += statement()
//            }
//            catch (e: EOFException) {
//                break@loop
//            }
//        }
        return program
    }

    private fun using() : Node = UsingStatement(consume(TokenType.TEXT))

    private fun struct(): Node = StructStatement(consume(TokenType.TEXT), options())

    private fun functionOrVar(): Node {
        val name = consume(TokenType.WORD)
        return when {
            match(TokenType.EQ) -> {
                VariableDefineStatement(name, expression())
            }
            match(TokenType.COLONCOLON) -> {
                StructFunctionDefineStatement(name, consume(TokenType.WORD), options(), statementBody())
            }
            else -> {
                FunctionDefineStatement(name, options(), statementBody())
            }
        }
    }

    private fun statement(): Node {
        return when {
            match(TokenType.IF) -> ifElse()
            match(TokenType.FOR) -> forTo()
            match(TokenType.FOREACH) -> foreach()
            match(TokenType.REPEAT) -> repeat()
            match(TokenType.DO) -> doUntil()
            match(TokenType.MATCH) -> match()
            match(TokenType.RETURN) -> ret()
            match(TokenType.ERROR) -> error()
            match(TokenType.EXTRACT) -> extract()
            else -> ExpressionStatement(expression())
        }
    }

    private fun ifElse() : Node {
        val condition = expression()
        val ifBody = statementOrBlock()

        return if (match(TokenType.ELSE)) {
            IfElseStatement(condition, ifBody)
        }
        else {
            val elseBody = statementOrBlock()
            IfElseStatement(condition, ifBody, elseBody)
        }
    }

    private fun forTo() : Node {
        val variable = consume(TokenType.WORD)
        consume(TokenType.EQ)
        val initialValue = expression()

        val type =
                if (match(TokenType.TO)) ForStatement.Type.TO
                else ForStatement.Type.DOWN_TO

        val finalValue = expression()
        val body = statementOrBlock()

        return ForStatement(variable, type, initialValue, finalValue, body)
    }

    private fun foreach() : Node {

        val value = consume(TokenType.WORD)

        if (match(TokenType.COMMA)) {
            return foreachMap(value)
        }

        consume(TokenType.COLON)
        val expr = expression()
        val body = statementOrBlock()
        return ForeachStatement(value, expr, body)
    }

    private fun foreachMap(value: Token) : Node {
        val key = consume(TokenType.WORD)
        consume(TokenType.COLON)
        val expr = expression()
        val body = statementOrBlock()
        return ForeachMapStatement(key, value, expr, body)
    }

    private fun repeat() : Node {
        val condition = expression()
        val body = statementOrBlock()
        return RepeatStatement(condition, body)
    }

    private fun doUntil() : Node {
        val body = statementOrBlock()
        consume(TokenType.UNTIL)
        val condition = expression()
        return RepeatStatement(condition, body)
    }

    private fun extract() : Node {
        val options = options()
        consume(TokenType.EQ)
        val extractingValue = expression()
        return ExtractStatement(options, extractingValue)
    }

    private fun error(): Node = ErrorStatement(expression())

    private fun ret(): Node = ReturnStatement(expression())

    private fun match() : Node {
        val expression = expression()
        val cases = ArrayList<MatchStatement.Case>()

        consume(TokenType.LBRACE)

        while (!match(TokenType.RBRACE)) {
            val value = expression()
            consume(TokenType.FUNCTIONAL)
            val body = statementOrBlock()

            cases += MatchStatement.Case(value, body)
        }

        return MatchStatement(expression, cases)
    }

    private fun expression(): Node {
        return ternary()
    }

    private fun ternary(): Node {
        val result = logicalOr()
        if (match(TokenType.QUESTION)) {
            val trueExpr = expression()
            consume(TokenType.COLON)
            val falseExpr = expression()
            return TernaryExpression(result, trueExpr, falseExpr)
        }
        return result
    }

    private fun logicalOr(): Node {
        var result = logicalAnd()
        while (true) {
            if (match(TokenType.BARBAR)) {
                result = BinaryExpression(Operator.OR, result, logicalAnd())
                continue
            }
            break
        }
        return result
    }

    private fun logicalAnd(): Node {
        var result = bitwiseOr()
        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = BinaryExpression(Operator.AND, result, bitwiseOr())
                continue
            }
            break
        }
        return result
    }

    private fun bitwiseOr(): Node {
        var expression = bitwiseXor()
        while (true) {
            if (match(TokenType.BAR)) {
                expression = BinaryExpression(Operator.OR, expression, bitwiseXor())
                continue
            }
            break
        }
        return expression
    }

    private fun bitwiseXor(): Node {
        var expression = bitwiseAnd()
        while (true) {
            if (match(TokenType.CARET)) {
                expression = BinaryExpression(Operator.XOR, expression, bitwiseAnd())
                continue
            }
            break
        }
        return expression
    }

    private fun bitwiseAnd(): Node {
        var expression = equality()
        while (true) {
            if (match(TokenType.AMP)) {
                expression = BinaryExpression(Operator.AND, expression, equality())
                continue
            }
            break
        }
        return expression
    }

    private fun equality(): Node {
        val result = conditional()
        return when {
            match(TokenType.EQEQ) -> {
                BinaryExpression(Operator.EQUALS, result, conditional())
            }
            else -> if (match(TokenType.EXCLEQ)) {
                BinaryExpression(Operator.NOT_EQUALS, result, conditional())
            }
            else result
        }
    }

    private fun conditional(): Node {
        var result = shift()
        loop@ while (true) {
            when {
                match(TokenType.LT) -> {
                    result = BinaryExpression(Operator.LT, result, shift())
                    continue@loop
                }
                match(TokenType.LTEQ) -> {
                    result = BinaryExpression(Operator.LTEQ, result, shift())
                    continue@loop
                }
                match(TokenType.GT) -> {
                    result = BinaryExpression(Operator.GT, result, shift())
                    continue@loop
                }
                match(TokenType.GTEQ) -> {
                    result = BinaryExpression(Operator.GTEQ, result, shift())
                    continue@loop
                }
            }
            break
        }
        return result
    }

    private fun shift(): Node {
        var expression = additive()
        loop@ while (true) {
            when {
                match(TokenType.LTLT) -> {
                    expression = BinaryExpression(Operator.LSHIFT, expression, additive())
                    continue@loop
                }
                match(TokenType.GTGT) -> {
                    expression = BinaryExpression(Operator.RSHIFT, expression, additive())
                    continue@loop
                }
                match(TokenType.GTGTGT) -> {
                    expression = BinaryExpression(Operator.URSHIFT, expression, additive())
                    continue@loop
                }
            }
            break
        }
        return expression
    }

    private fun additive(): Node {
        var result = multiplicative()
        loop@ while (true) {
            when {
                match(TokenType.PLUS) -> {
                    result = BinaryExpression(Operator.ADD, result, multiplicative())
                    continue@loop
                }
                match(TokenType.MINUS) -> {
                    result = BinaryExpression(Operator.SUBTRACT, result, multiplicative())
                    continue@loop
                }
                match(TokenType.COLONCOLON) -> {
                    result = BinaryExpression(Operator.PUSH, result, multiplicative())
                    continue@loop
                }
            }
            break
        }
        return result
    }

    private fun multiplicative(): Node {
        var result = unary()
        loop@ while (true) {
            when {
                match(TokenType.STAR) -> {
                    result = BinaryExpression(Operator.MULTIPLY, result, unary())
                    continue@loop
                }
                match(TokenType.SLASH) -> {
                    result = BinaryExpression(Operator.DIVIDE, result, unary())
                    continue@loop
                }
                match(TokenType.PERCENT) -> {
                    result = BinaryExpression(Operator.REMAINDER, result, unary())
                    continue@loop
                }
            }
            break
        }
        return result
    }

    private fun unary(): Node {
        return when {
            match(TokenType.MINUS) -> UnaryExpression(Operator.NEGATE, primary())
            match(TokenType.EXCL) -> UnaryExpression(Operator.NOT, primary())
            match(TokenType.TILDE) -> UnaryExpression(Operator.COMPLEMENT, primary())
            match(TokenType.COLONCOLON) -> FunctionReferenceExpression(consume(TokenType.WORD))
            match(TokenType.PLUS) -> primary()
            else -> primary()
        }
    }

    private fun primary(): Node {
        val result = lambda()
        return when {
            // if lambda returned result is Lambda, not a Empty value, then return result value
            (result != EmptyNode) -> result
            // this...
            lookMatch(0, TokenType.THIS) -> variableSuffix(ValueExpression(consume(TokenType.THIS)))
            // (expr)
            match(TokenType.LPAREN) -> expressionInParens()
            // new SomeInstance() or new [10][20] (multi arrays)
            match(TokenType.NEW) -> when {
                lookMatch(0, TokenType.LBRACKET) -> multiArray()
                lookMatch(0, TokenType.WORD) -> newInstance()
                else -> throw error(current(), "Expected '[' or Identifier" )
            }
            else -> variable()
        }
    }

    private fun expressionInParens(): Node {
        val expression = expression()
        match(TokenType.RPAREN)
        return variableSuffix(expression)
    }


    private fun newInstance() : Node {
        TODO("Realise the newInstance declaration parsing construction")
    }

    private fun multiArray() : Node {
        TODO("Realise the multiArray declaration parsing construction")
    }

    private fun array(): Node {
        //
        TODO("Realise the array parsing construction")
    }

    private fun map(): Node {
        TODO("Realise the map parsing construction")
    }

    private fun lambda(): Node {
        TODO("Realise the lambda parsing construction")
    }

    private fun variable(): Node {
        return when {
            lookMatch(0, TokenType.WORD) -> unknownWords()
            lookMatch(0, TokenType.THIS) -> variableSuffix(ValueExpression(prev()))
            else -> value()
        }
    }

    private fun unknownWords(): Node {
        val words: MutableList<Token> = ArrayList()
        while (true) {
            words += consume(TokenType.WORD)
            if (!match(TokenType.DOT)) break
        }
        return variableSuffix(UnknownWordsExpression(words))
    }

    private fun variableSuffix(expression: Node): Node {
        var result: Node = expression

        loop@ while (true) {
            result = when {
                lookMatch(0, TokenType.LPAREN) -> {
                    FunctionalChainExpression(result, arguments())
                }
                match(TokenType.DOT) && result is ValueExpression -> {
                    UnknownWordsExpression(mutableListOf(consume(TokenType.WORD)))
                }
                match(TokenType.DOT) -> {
                    FieldAccessExpression(result, consume(TokenType.WORD))
                }
                match(TokenType.LBRACKET) -> {
                    val index = expression()
                    consume(TokenType.RBRACKET)
                    ArrayAccessExpression(result, index)
                }
                match(TokenType.COLONCOLON) -> {
                    ObjectFunctionReferenceExpression(result, consume(TokenType.WORD))
                }
                match(TokenType.PLUSPLUS) -> {
                    UnaryExpression(Operator.INC, result)
                }
                match(TokenType.MINUSMINUS) -> {
                    UnaryExpression(Operator.DEC, result)
                }
                else -> break@loop
            }
        }
        return result
    }

    private fun value(): Node {
        val current = get(0)
        if (match(TokenType.INTEGER)
                || match(TokenType.FLOAT)
                || match(TokenType.INTEGER)
                || match(TokenType.TEXT)
                || match(TokenType.HEX_NUMBER)
                || match(TokenType.TRUE)
                || match(TokenType.NIL)
                || match(TokenType.FALSE)
        ) {
            return ValueExpression(current)
        }
        throw error(current, "Unknown expression '" + current.text + "'")
    }

    private fun options(): Options {
        val options = Options()
        consume(TokenType.LPAREN)
        while (match(TokenType.RPAREN)) {
            options += consume(TokenType.WORD)
            consume(TokenType.COMMA)
        }
        return options
    }

    private fun arguments(): NodeList {
        val arguments = NodeList()
        consume(TokenType.LPAREN)

        while (true) {
            if (match(TokenType.RPAREN)) break

            arguments.add(expression())
            match(TokenType.COMMA)
        }
        return arguments
    }

    private fun block(): Block {
        val block = Block()
        consume(TokenType.LBRACE)
        while (!match(TokenType.RBRACE)) block += statement()
        return block
    }

    private fun statementBody(): Node {
        return if (match(TokenType.FUNCTIONAL)) ReturnStatement(expression()) else block()
    }

    private fun statementOrBlock(): Node {
        return if (lookMatch(0, TokenType.LBRACE)) block() else statement()
    }

    private fun prev(): Token {
        return get(-1)
    }

    private fun current(): Token {
        return get(0)
    }

    private fun consume(type: TokenType): Token {
        val current = get(0)
        if (type !== current.type) {
            throw ParseException(current, "'" + type.text + "' expected")
        }
        pos++
        return current
    }

    private fun match(type: TokenType): Boolean {
        val current = get(0)
        if (type !== current.type) {
            return false
        }
        pos++
        return true
    }

    private fun lookMatch(pos: Int, type: TokenType): Boolean = get(pos).type === type

    private operator fun get(relativePosition: Int): Token {
        val position: Int = pos + relativePosition
        if (position >= size) {
            throw EOFException()
        }
        return tokens[position]
    }

    private fun error(error: String): Throwable {
        return error(current(), error)
    }

    private fun error(token: Token, error: String): Throwable {
        val err = ParseException(token, error)
        errors += err
        return err
    }
}