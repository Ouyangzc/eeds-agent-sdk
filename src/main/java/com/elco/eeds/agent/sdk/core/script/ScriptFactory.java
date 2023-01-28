package com.elco.eeds.agent.sdk.core.script;

/**
 * @author ：ytl
 * @date ：Created in 2023/1/6 13:06
 * @description：
 */
public class ScriptFactory {
    public static final String MSG = "msg";
    public static final String METADATA = "metadata";
    public static final String MSG_TYPE = "msgType";
    public static final String RULE_NODE_FUNCTION_NAME = "ruleNodeFunc";

    private static final String JS_WRAPPER_PREFIX_TEMPLATE = "function %s(msgStr, metadataStr, msgType) { " +
            "    var msg = JSON.parse(msgStr); " +
            "    var metadata = JSON.parse(metadataStr); " +
            "    return JSON.stringify(%s(msg, metadata, msgType));" +
            "    function %s(%s, %s, %s) {";
    private static final String JS_WRAPPER_SUFFIX = "\n}" +
            "\n}";


    public static String generateScript(String functionName, String scriptBody, String... argNames) {
        String msgArg;
        String metadataArg;
        String msgTypeArg;
        if (argNames != null && argNames.length == 3) {
            msgArg = argNames[0];
            metadataArg = argNames[1];
            msgTypeArg = argNames[2];
        } else {
            msgArg = MSG;
            metadataArg = METADATA;
            msgTypeArg = MSG_TYPE;
        }
        String jsWrapperPrefix = String.format(JS_WRAPPER_PREFIX_TEMPLATE, functionName,
                RULE_NODE_FUNCTION_NAME, RULE_NODE_FUNCTION_NAME, msgArg, metadataArg, msgTypeArg);
        return jsWrapperPrefix + scriptBody + JS_WRAPPER_SUFFIX;
    }


    public static void main(String[] args) {
        System.out.println(generateScript("aaaa","sdfasdfas","aa","bb"));
    }
}
