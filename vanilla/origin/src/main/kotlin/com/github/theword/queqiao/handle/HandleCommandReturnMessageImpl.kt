package com.github.theword.queqiao.handle

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService

class HandleCommandReturnMessageImpl : HandleCommandReturnMessageService() {
    override fun handleCommandReturnMessage(o: Any, s: String) {
    }

    override fun hasPermission(o: Any, s: String): Boolean {
        return false
    }
}