package com.neowise.almond

fun <T> List<T>.listItems(separator: String): String {
    return buildString {
        this@listItems.forEachIndexed { index, t ->
            if (index > 0) append(separator)
            append(t)
        }
    }
}