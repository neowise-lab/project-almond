package com.neowise.almond

fun <T> List<T>.listItems(separator: String): String {
    return buildString {
        for((index, item) in this@listItems.withIndex()) {
            if (index > 0) append(separator)
            append(item)
        }
    }
}