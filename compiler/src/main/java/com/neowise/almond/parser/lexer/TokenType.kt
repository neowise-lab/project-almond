package com.neowise.almond.parser.lexer

enum class TokenType(val text: String) {

    INTEGER("<INTEGER>"),
    FLOAT("<FLOAT>"),
    HEX_NUMBER("<HEX>"),
    WORD("<WORD>"),
    TEXT("<TEXT>"),

    TRUE("true"),
    FALSE("false"),

    IS("is"),
    // keyword
    IF("if"),
    ELSE("else"),
    FOR("for"),
    TO("to"),
    DOWN_TO("downto"),

    FOREACH("foreach"),
    REPEAT("repeat"),

    DO("do"),
    UNTIL("until"),

    BREAK("break"),
    CONTINUE("continue"),

    STRUCT("struct"),
    FUNC("func"),
    VAR("var"),
    CONST("const"),
    RETURN("return"),
    USING("using"),

    ERROR("error"),
    MATCH("match"),

    DEFAULT("default"),
    CASE("case"),

    NEW("new"),
    THIS("this"),
    EXTRACT("extract"),

    PLUS("+"), //  +
    MINUS("-"), //  -
    STAR("*"), //  *
    SLASH("/"), //  /
    PERCENT("%"),// %

    EQ("="), //  =
    EQEQ("=="), //  ==
    EXCL("!"), //  !
    EXCLEQ("!="), //  !=
    LTEQ("<="), //  <=
    LT("<"), //  <
    GT(">"), //  >
    GTEQ(">="), //  >=

    PLUSEQ("+="), //  +=
    MINUSEQ("-="), //  -=
    STAREQ("*="), //  *=
    SLASHEQ("/="), //  /=
    PERCENTEQ("%="), //  %=
    AMPEQ("&="), //  &=
    CARETEQ("^="), //  ^=
    BAREQ("|="), //  |=
    COLONCOLONEQ(":="), //  ::=
    LTLTEQ("<<="), //  <<=
    GTGTEQ(">>="), //  >>=
    GTGTGTEQ(">>>="), //  >>>=

    PLUSPLUS("++"), //  ++
    MINUSMINUS("--"), //  --

    LTLT("<<"), //  <<
    GTGT(">>"), //  >>
    GTGTGT(">>>"), //  >>>

    TILDE("~"), //  ~
    CARET("^"), //  ^
    BAR("|"), //  |
    BARBAR("||"), //  ||
    AMP("&"), //  &
    AMPAMP("&&"), //  &&

    QUESTION("?"), //  ?
    COLON(":"), //  :
    SEMICOLON(";"), //  ;
    COLONCOLON("::"), //  ::
    FUNCTIONAL("->"), //  ->

    LPAREN("("), //  (
    RPAREN(")"), //  )
    LBRACKET("["), //  [
    RBRACKET("]"), //  ]
    LBRACE("{"), //  {
    RBRACE("}"), //  }
    COMMA(","), //  ,
    DOT("."), //  .

    NIL("nil"),

    EOF("<EOF>"),


}