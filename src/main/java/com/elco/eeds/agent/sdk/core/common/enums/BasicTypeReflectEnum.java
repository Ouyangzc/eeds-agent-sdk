package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName BasicTypeRelectEnum
 * @Description 基本包装映射枚举
 * @Author OuYang
 * @Date 2024/4/23 13:46
 * @Version 1.0
 */
public enum BasicTypeReflectEnum {
  BYTE("byte","Byte",byte.class, Byte.class),
  SHORT("short","Short",short.class, Short.class),
  INT("int","Integer",int.class,Integer.class),
  LONG("long","Long",long.class,Long.class),
  DOUBLE("double","Double",double.class,Double.class),
  FLOAT("float","Float",float.class, Float.class),
  BOOLEAN("boolean","Boolean",boolean.class,Boolean.class),
  CHAR("char","Character",char.class, Character.class),
  STRING("String","String",String.class,String.class)
  ;
  private String basicKey;
  private String wrapperKey;
  private Class basicClass;
  private Class wrapperClass;

  BasicTypeReflectEnum(String basicKey, String wrapperKey, Class basicClass,
      Class wrapperClass) {
    this.basicKey = basicKey;
    this.wrapperKey = wrapperKey;
    this.basicClass = basicClass;
    this.wrapperClass = wrapperClass;
  }

  public String getBasicKey() {
    return basicKey;
  }

  public String getWrapperKey() {
    return wrapperKey;
  }

  public Class getBasicClass() {
    return basicClass;
  }

  public Class getWrapperClass() {
    return wrapperClass;
  }
}
