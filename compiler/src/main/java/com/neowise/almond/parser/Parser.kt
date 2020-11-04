package com.neowise.almond.parser

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.parser.ast.*
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.parser.lexer.TokenType
import com.neowise.almond.parser.lexer.TokenType.*
import java.io.EOFException
import java.util.*

class Parser(private val location: String, private val name: String, private val tokens: List<Token>) {

    private val errors = mutableListOf<ParseException>()
    private var size = tokens.size
    private var pos = 0

    fun parse(): Program {

        val nodes = NodeList()

        try {
            //В начале определяются используемые модули
            while (match(USING)) {
                nodes += using()
            }
            //Вторыми структуры, функции и поля
            loop@ while(true) {
                nodes += when {
                    match(STRUCT) -> struct()
                    match(FUNC) -> function()
                    match(VAR) -> varDefine(isConst = false)
                    match(CONST) -> varDefine(isConst = true)
                    else -> {
                        val current = current()
                        errors += error(current, "${current.text} not allowed here.")
                        continue@loop
                    }
                }
            }
        }
        catch (e: ParseException) {
            errors += e
            throw e
        }
        catch (e: EOFException) {
            println("#parser: end of file reached!")
        }

        return Program(location, name, nodes, errors.toList())
    }

    private fun using() : Node = UsingStatement(consume(TEXT))

    private fun struct(): Node {
        val name = consume(WORD)
        val options = options()
        val functions = NodeList()
        // {
        if (match(LBRACE))
            while (!match(RBRACE)) {
                consume(FUNC)
                functions += function()
            }

        return StructStatement(name, options, functions)
    }

    private fun varDefine(isConst: Boolean): Node {
        val name = consume(WORD)
        consume(EQ)
        return VariableDefineStatement(name, expression(), isConst)
    }

    private fun function(): Node {
        val name = consume(WORD)
        return when {
//            match(TokenType.COLONCOLON) -> {
//                // Struct::fun(options) {}
//                StructFunctionDefineStatement(name, consume(TokenType.WORD), options(), statementBody())
//            }
            else -> {
                // fun(options) {}
                // fun(options) -> {}
                FunctionDefineStatement(name, options(), statementBody())
            }
        }
    }

    private fun statement(): Node {
        return try {
             when {
                match(IF) -> ifElse()
                match(FOR) -> forTo()
                match(FOREACH) -> foreach()
                match(REPEAT) -> repeat()
                match(DO) -> doUntil()
                match(MATCH) -> match()
                match(RETURN) -> ret()
                match(ERROR) -> error()
                match(EXTRACT) -> extract()
                else -> {
                    val variable = variable()
                    val current = current()

                    if (variable is FunctionalChainExpression || variable is UnaryExpression) {
                        ExpressionStatement(variable)
                    }
                    else if (AssignOperators.contains(current.type)) {
                        AssignmentStatement(variable, expression(), current)
                    }
                    else throw error(current, "$'{current.text}' not allowed here!")
                }
            }
        }
        catch (e: ParseException) {
            throw e
/*
            println(e)
            errors += e
            recover()
*/
        }
    }

    private fun recover(): Node {
        val preRecoverPosition = pos
        for (i in preRecoverPosition..size) {
            pos = i
            try {
                // successfully parsed,
                return statement() // return successfully result
            } catch (ex: Exception) {
                // fail
            }
        }
        throw EOFException()
    }

    private fun ifElse() : Node {
        val condition = expression()
        val ifBody = statementOrBlock()

        return if (match(ELSE)) {
            IfElseStatement(condition, ifBody)
        }
        else {
            val elseBody = statementOrBlock()
            IfElseStatement(condition, ifBody, elseBody)
        }
    }

    private fun forTo() : Node {
        val variable = consume(WORD)
        consume(EQ)
        val initialValue = expression()

        val type =
                if (match(TO)) ForStatement.Type.TO
                else ForStatement.Type.DOWN_TO

        val finalValue = expression()
        val body = statementOrBlock()

        return ForStatement(variable, type, initialValue, finalValue, body)
    }

    private fun foreach() : Node {

        val value = consume(WORD)

        if (match(COMMA)) {
            return foreachMap(value)
        }

        consume(COLON)
        val expr = expression()
        val body = statementOrBlock()
        return ForeachStatement(value, expr, body)
    }

    private fun foreachMap(value: Token) : Node {
        val key = consume(WORD)
        consume(COLON)
        val expr = expression()
        val body = statementOrBlock()
        return ForeachMapStatement(value, key, expr, body)
    }

    private fun repeat() : Node {
        val condition = expression()
        val body = statementOrBlock()
        return RepeatStatement(condition, body)
    }

    private fun doUntil() : Node {
        val body = statementOrBlock()
        consume(UNTIL)
        val condition = expression()
        return DoUntilStatement(condition, body)
    }

    private fun extract() : Node {
        val options = options()
        consume(EQ)
        val extractingValue = expression()
        return ExtractStatement(options, extractingValue)
    }

    private fun error(): Node = ErrorStatement(expression())

    private fun ret(): Node = ReturnStatement(expression())

    private fun match() : Node {
        val expression = expression()
        val cases = ArrayList<MatchStatement.Case>()

        consume(LBRACE)

        while (!match(RBRACE)) {
            val value = expression()
            consume(FUNCTIONAL)
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
        if (match(QUESTION)) {
            val trueExpr = expression()
            consume(COLON)
            val falseExpr = expression()
            return TernaryExpression(result, trueExpr, falseExpr)
        }
        return result
    }

    private fun logicalOr(): Node {
        var result = logicalAnd()
        while (true) {
            if (match(BARBAR)) {
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
            if (match(AMPAMP)) {
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
            if (match(BAR)) {
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
            if (match(CARET)) {
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
            if (match(AMP)) {
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
            match(EQEQ) -> {
                BinaryExpression(Operator.EQUALS, result, conditional())
            }
            else -> if (match(EXCLEQ)) {
                BinaryExpression(Operator.NOT_EQUALS, result, conditional())
            }
            else result
        }
    }

    private fun conditional(): Node {
        var result = shift()
        loop@ while (true) {
            when {
                match(LT) -> {
                    result = BinaryExpression(Operator.LT, result, shift())
                    continue@loop
                }
                match(LTEQ) -> {
                    result = BinaryExpression(Operator.LTEQ, result, shift())
                    continue@loop
                }
                match(GT) -> {
                    result = BinaryExpression(Operator.GT, result, shift())
                    continue@loop
                }
                match(GTEQ) -> {
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
                match(LTLT) -> {
                    expression = BinaryExpression(Operator.LSHIFT, expression, additive())
                    continue@loop
                }
                match(GTGT) -> {
                    expression = BinaryExpression(Operator.RSHIFT, expression, additive())
                    continue@loop
                }
                match(GTGTGT) -> {
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
                match(PLUS) -> {
                    result = BinaryExpression(Operator.ADD, result, multiplicative())
                    continue@loop
                }
                match(MINUS) -> {
                    result = BinaryExpression(Operator.SUBTRACT, result, multiplicative())
                    continue@loop
                }
                match(COLONCOLON) -> {
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
                match(STAR) -> {
                    result = BinaryExpression(Operator.MULTIPLY, result, unary())
                    continue@loop
                }
                match(SLASH) -> {
                    result = BinaryExpression(Operator.DIVIDE, result, unary())
                    continue@loop
                }
                match(PERCENT) -> {
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
            match(MINUS) -> UnaryExpression(Operator.NEGATE, primary())
            match(EXCL) -> UnaryExpression(Operator.NOT, primary())
            match(TILDE) -> UnaryExpression(Operator.COMPLEMENT, primary())
            match(COLONCOLON) -> FunctionReferenceExpression(consume(WORD))
            match(PLUS) -> primary()
            else -> primary()
        }
    }

    private fun primary(): Node {
        val result = lambda()

        return when {
            // if returned result is Lambda, not a Empty value, then return result value
            (result != EmptyNode) -> result
            // (expr)
            match(LPAREN) -> variableSuffix(expressionInParens())
            // new SomeInstance()
            match(LBRACKET) -> variableSuffix(array())
            match(LBRACE) -> variableSuffix(map())

            else -> variable()
        }
    }

    private fun expressionInParens(): Node {
        val expression = expression()
        match(RPAREN)
        return variableSuffix(expression)
    }

    private fun array(): Node {
        // [1, 2, 3, 4, 5, ...]
        val elements = NodeList()
        // if not reach ']'
        if (!match(RBRACKET))
            while(true) {
                elements += expression()
                // after expression must be ] or comma
                if (match(RBRACKET)) break
                consume(COMMA)
            }

        return ArrayExpression(elements)
    }

    private fun map(): Node {
        //{key: value, key: value, ...}
        val keys = NodeList()
        val values = NodeList()
        // if not reach '}'
        if (!match(RBRACE))
            while(true) {
                // key : value
                keys += expression()
                consume(COLON)
                values += expression()

                // after key:value must be } or comma
                if (match(RBRACE)) break
                consume(COMMA)
            }

        return MapExpression(keys, values)
    }

    private fun lambda(): Node {
        val lastPos = pos
        // if an exception is thrown or no matches, then no lambda further down the code
        // recover last position and return EmptyNode
        try {
            when {
                // (args) ->
                lookMatch(0, LPAREN) -> {
                    val options = options()
                    if (lookMatch(0, FUNCTIONAL)) return LambdaExpression(options, statementBody())
                }
                // arg ->
                lookMatch(0, WORD) -> {
                    val options = Options(consume(WORD))
                    return LambdaExpression(options, statementBody())
                }
                // ->
                lookMatch(0, FUNCTIONAL) -> return LambdaExpression(Options(), statementBody())
            }
        }
        catch (e: ParseException) {}

        // recover position
        pos = lastPos
        return EmptyNode
    }

    private fun variable(): Node {
        return when {
            lookMatch(0, WORD) -> unknownWords()
            match(THIS) -> variableSuffix(ValueExpression(prev()))
            match(NEW) -> variableSuffix(newInstance())
            else -> value()
        }
    }

    private fun newInstance() : Node {
        val struct = consume(WORD)
        val arguments = arguments()
        return NewInstanceExpression(struct, arguments)
    }

    private fun unknownWords(): Node {
        val words: MutableList<Token> = ArrayList()
        while (true) {
            words += consume(WORD)
            if (!match(DOT)) break
        }
        return variableSuffix(UnknownWordsExpression(words))
    }

    private fun variableSuffix(expression: Node): Node {
        var result: Node = expression

        loop@ while (true) {
            result = when {
                lookMatch(0, LPAREN) -> {
                    FunctionalChainExpression(result, arguments())
                }
                match(DOT) && result is ValueExpression -> {
                    UnknownWordsExpression(mutableListOf(consume(WORD)))
                }
                match(DOT) -> {
                    FieldAccessExpression(result, consume(WORD))
                }
                match(LBRACKET) -> {
                    val index = expression()
                    consume(RBRACKET)
                    ArrayAccessExpression(result, index)
                }
                match(COLONCOLON) -> {
                    ObjectFunctionReferenceExpression(result, consume(WORD))
                }
                match(PLUSPLUS) -> {
                    UnaryExpression(Operator.INC, result)
                }
                match(MINUSMINUS) -> {
                    UnaryExpression(Operator.DEC, result)
                }
                else -> break@loop
            }
        }
        return result
    }

    private fun value(): Node {
        val current = get(0)
        if (match(INTEGER)
                || match(FLOAT)
                || match(INTEGER)
                || match(TEXT)
                || match(HEX_NUMBER)
                || match(TRUE)
                || match(NIL)
                || match(FALSE)
        ) {
            return ValueExpression(current)
        }
        throw error(current, "Unknown expression '" + current.text + "'")
    }

    private fun options(): Options {
        val options = Options()
        consume(LPAREN)

        if (!match(RPAREN))
            while (true) {
                options += consume(WORD)
                if (match(RPAREN)) break
                consume(COMMA)
            }

        return options
    }

    private fun arguments(): NodeList {
        val arguments = NodeList()
        consume(LPAREN)

        if (!match(RPAREN))
            while (true) {
                arguments.add(expression())
                if (match(RPAREN)) break
                match(COMMA)
            }

        return arguments
    }

    private fun block(): Block {
        val block = Block()
        consume(LBRACE)
        while (!match(RBRACE)) block += statement()
        return block
    }

    private fun statementBody(): Node {
        return if (match(FUNCTIONAL)) ReturnStatement(expression()) else block()
    }

    private fun statementOrBlock(): Node {
        return if (lookMatch(0, LBRACE)) block() else statement()
    }

    private fun prev(): Token {
        return get(-1)
    }

    private fun current(): Token {
        return get(0)
    }

    private fun skip() {
        pos++
    }

    private fun consume(type: TokenType): Token {
        val current = get(0)
        if (type !== current.type) {
            throw ParseException(current, "'${type.text}' expected, but found '${current.type.text}'")
        }
        pos++
        return current
    }

    private fun multipleMatches(vararg types: TokenType): Boolean {
        // Remember the last position
        val recoverPosition = pos;
        for(type in types) {
            if (!match(type)) {
                // if there is no match, return the last position and false
                pos = recoverPosition
                return false
            }
        }
        return true
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

    private fun error(token: Token, error: String): ParseException {
        return ParseException(token, error)
    }
}