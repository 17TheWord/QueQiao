package com.github.theword.queqiao.utils;


import com.github.theword.queqiao.tool.GlobalContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.util.text.ITextComponent;

public class ParseJsonImpl {

    /** 调用原版方法反序列化Json文本组件, 并将异常输出到日志
     * @param jsonElement 消息体
     * @return 文本组件, 或null如果解析出错
     */
    public static ITextComponent parseJsonToTextWrapped(JsonElement jsonElement){
        try {
            return ITextComponent.Serializer.fromJsonLenient(jsonElement.toString());
        } catch (Throwable e) {
            GlobalContext.getLogger().error("Error handling Broadcast Message {} : ",jsonElement,e);
        }
        return null;
    }
}