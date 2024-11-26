package com.github.theword.queqiao.config

import java.util.regex.Pattern

class RegexConfig(
    regex: String,
    val playerGroup: Int,
    val messageGroup: Int?
) {
    val pattern: Pattern = Pattern.compile(regex)
}
