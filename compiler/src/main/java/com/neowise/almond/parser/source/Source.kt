package com.neowise.almond.parser.source

class Source(private val input: String) {

     var row: Int = 0
     var col: Int = 0

     var position: Int = 0
     val length: Int

    init {
        position = 0
        length = input.length
    }

     val isEndReached: Boolean
        get() = position >= length

     fun next(): Char {
        position++
        val result = peek(0)
        if (result == '\n') {
            row++
            col = 1
        } else {
            col++
        }
        return result
    }

     fun peek(relative: Int): Char {
        val position = this.position + relative
        if (position >= length) {
            return '\u0000'
        }
        return input[position];
    }

     fun rollback(): PositionRollback {
        return PositionRollback(position, row, col)
    }

     fun initRollback(rollback: PositionRollback) {
        this.position = rollback.position
        this.row = rollback.row
        this.col = rollback.col
    }
}