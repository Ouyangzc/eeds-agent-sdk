package com.elco.eeds.agent.sdk.core.util;

import static com.elco.eeds.agent.sdk.core.common.constant.DateConstant.DATE_TIME_FORMAT;

import com.elco.eeds.agent.sdk.core.common.constant.DateConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName JSONUtils
 * @Description fastjson工具类
 * @Author OuYang
 * @Date 2024/4/19 9:18
 * @Version 1.0
 */
public class JSONUtils {


  private JSONUtils() {
  }

  private static final ObjectMapper mapper = new ObjectMapper();


  /**
   * 默认日期格式化对象
   */
  private static ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(
      () -> new SimpleDateFormat(DATE_TIME_FORMAT));

  static {
    //json中可以有注释
    mapper.enable(Feature.ALLOW_COMMENTS);
    //重复检测
    mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
    //POJO无public的属性或方法时，不报错
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    //序列化时候，只序列化非空字段
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    //在序列化时日期格式默认为 yyyy-MM-dd HH:mm:ss.SSS
    mapper.setDateFormat(sdf.get());
    //针对于JDK新时间类。序列化时带有T的问题，自定义格式化字符串
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    javaTimeModule.addSerializer(LocalDate.class,
        new LocalDateSerializer(DateTimeFormatter.ofPattern(DateConstant.DATE_FORMAT)));
    javaTimeModule.addSerializer(LocalTime.class,
        new LocalTimeSerializer(DateTimeFormatter.ofPattern((DateConstant.TIME_FORMAT))));
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    javaTimeModule.addDeserializer(LocalDate.class,
        new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateConstant.DATE_FORMAT)));
    javaTimeModule.addDeserializer(LocalTime.class,
        new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateConstant.TIME_FORMAT)));
    mapper.registerModule(javaTimeModule);

    // 	当反序列化出现未定义字段时候，不出现错误
    //忽略不知道的 JSON 字段
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //Null 值对应 Java 基本数据类型
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        //忽略空Bean转json错误
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  }

  /**
   * 判断字符串是否为JSON字符串
   *
   * @param jsonStr
   * @return
   */
  public static boolean isJson(String jsonStr) {
    try {
      mapper.readValue(jsonStr, JsonNode.class);
    } catch (JsonProcessingException e) {
      return false;
    }
    return true;
  }

  /*******************************对象转Json*********************************************/
  /**
   * 对象转JSON字符串
   *
   * @param obj
   * @return
   */
  public static String toJsonStr(Object obj) {
    if (null == obj) {
      return null;
    }
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("object format to json error:" + obj, e);
    }
  }

  /**
   * 对象转JSON字符串，格式美化
   *
   * @param obj
   * @return
   */
  public static String toPrettyJson(Object obj) {
    if (null == obj) {
      return null;
    }
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("object format to json error:" + obj, e);
    }
  }

  /*******************************Json转对象*********************************************/
  /**
   * 反序列化json
   *
   * @param json
   * @param beanClass
   * @param <T>
   * @return
   */
  public static <T> T toBean(String json, Class<T> beanClass) {
    try {
      return mapper.readValue(json == null ? "{}" : json, beanClass);
    } catch (Exception e) {
      throw new RuntimeException("json parse to object [" + json + "] error:" + json, e);
    }
  }

  public static <T> T toBean(String json, JavaType javaType) {
    try {
      return mapper.readValue(json == null ? "{}" : json, javaType);
    } catch (Exception e) {
      throw new RuntimeException("json parse to object [" + json + "] error:" + json, e);
    }
  }

  public static <T> T toBeanReference(String str, TypeReference<T> tr) {
    try {
      return mapper.readValue(str, tr);
    } catch (Exception e) {
      throw new RuntimeException("json parse to object [" + tr + "] error:" + str, e);
    }
  }

  /**
   * 反序列化json，转换成集合，集合中的元素为泛型对象
   *
   * @param json
   * @param clz
   * @param <T>
   * @return
   */
  public static <T> List<T> toList(String json, Class<T> clz) {
    return toBean(json, getCollectionType(List.class, clz));
  }


  public static Map<String, Object> toMap(String jsonString) {
    return toMap(jsonString, String.class, Object.class);
  }

  /**
   * 反序列化json，转换成Map，集合中的元素为泛型对象
   *
   * @param json
   * @param keyType
   * @param valueType
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
    return toBean(json, getCollectionType(Map.class, keyType, valueType));
  }

  public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return mapper.getTypeFactory().
        constructParametricType(collectionClass, elementClasses);
  }

  /**
   * JsonNode转对象
   *
   * @param treeNode
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T toBean(JsonNode treeNode, Class<T> clazz) {
    try {
      return mapper.treeToValue(treeNode, clazz);
    } catch (Exception e) {
      throw new RuntimeException("json parse to object [" + treeNode + "] error:" + treeNode, e);
    }
  }

  /**
   * 将字符串转成JsonNode
   *
   * @param str
   * @return
   */
  public static JsonNode toJsonNode(String str) {
    try {
      return mapper.readTree(str);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json parse to object [" + str + "] error:" + str, e);
    }
  }

  /**
   * 对象转JsonNode
   *
   * @param obj
   * @return
   */
  public static JsonNode toJsonNode(Object obj) {
    try {
      return mapper.valueToTree(obj);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("object parse to JosnNode [" + obj + "] error:", e);
    }
  }

  /**
   * json转JsonArrat
   * @param json
   * @return
   */
  public static ArrayNode toJsonArray(String json) {
    try {
      return (ArrayNode) mapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("string parse to JsonArray [" + json + "] error:", e);
    }
  }

  //============================================== 获取value值方法封装 =============================================================

  /**
   * 获取JSON对象 ObjectNode
   *
   * @param jsonNode
   * @param key      JSON key
   * @return
   */
  public static ObjectNode getJSONObject(JsonNode jsonNode, String key) {
    if (jsonNode == null) {
      return null;
    }
    return (ObjectNode) Optional.ofNullable(jsonNode.get(key)).orElse(null);
  }

  /**
   * 获取JSON数组 ArrayNode
   *
   * @param jsonNode json对象
   * @param key      JSON key
   * @return ArrayNode
   */
  public static ArrayNode getJSONArray(JsonNode jsonNode, String key) {
    if (jsonNode == null) {
      return null;
    }
    return (ArrayNode) Optional.ofNullable(jsonNode.get(key)).orElse(null);
  }


  /**
   * 获取String类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return String
   */
  public static String getString(JsonNode jsonNode, String key, String defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(JSONUtils::getJsonNodeString).orElse(defaultValue);
  }

  public static String getString(JsonNode jsonNode, String key) {
    return getString(jsonNode, key, null);
  }

  /**
   * 获取Byte类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return Byte
   */
  public static Byte getByte(JsonNode jsonNode, String key, Byte defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(x -> Byte.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static Byte getByte(JsonNode jsonNode, String key) {
    return getByte(jsonNode, key, null);
  }

  /**
   * 获取Short类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return Short
   */
  public static Short getShort(JsonNode jsonNode, String key, Short defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull()).filter(x -> !x.isNull())
        .map(x -> Short.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static Short getShort(JsonNode jsonNode, String key) {
    return getShort(jsonNode, key, null);
  }

  /**
   * 获取Integer类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return Integer
   */
  public static Integer getInteger(JsonNode jsonNode, String key, Integer defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(x -> Integer.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static Integer getInteger(JsonNode jsonNode, String key) {
    return getInteger(jsonNode, key, null);
  }

  /**
   * 获取Long类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return Long
   */
  public static Long getLong(JsonNode jsonNode, String key, Long defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(x -> Long.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static Long getLong(JsonNode jsonNode, String key) {
    return getLong(jsonNode, key, null);
  }

  /**
   * 获取Double类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return Double
   */
  public static Double getDouble(JsonNode jsonNode, String key, Double defaultValue) {
    if (jsonNode == null) {
      return null;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(x -> Double.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static Double getDouble(JsonNode jsonNode, String key) {
    return getDouble(jsonNode, key, null);
  }

  /**
   * 获取Boolean类型的value
   *
   * @param jsonNode     json对象
   * @param key          JSON key
   * @param defaultValue 默认value
   * @return boolean
   */
  public static boolean getBoolean(JsonNode jsonNode, String key, boolean defaultValue) {
    if (jsonNode == null) {
      return false;
    }
    return Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(x -> Boolean.valueOf(getJsonNodeString(x))).orElse(defaultValue);
  }

  public static boolean getBoolean(JsonNode jsonNode, String key) {
    return getBoolean(jsonNode, key, false);
  }

  /**
   * 获取Date类型的value
   *
   * @param jsonNode json对象
   * @param key      JSON key
   * @param format   日期格式对象
   * @return Date
   */
  public static Date getDate(JsonNode jsonNode, String key, SimpleDateFormat format) {
    if (jsonNode == null || format == null) {
      return null;
    }
    String dateStr = Optional.ofNullable(jsonNode.get(key)).filter(x -> !x.isNull())
        .map(JSONUtils::getJsonNodeString).orElse(null);
    if (dateStr == null) {
      return null;
    }
    try {
      return format.parse(dateStr);
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Date getDate(JsonNode jsonNode, String key) {
    return getDate(jsonNode, key, sdf.get());
  }

  /**
   * 去掉JsonNode toString后字符串的两端的双引号（直接用JsonNode的toString方法获取到的值做后续处理，Jackson去获取String类型的方法有坑）
   *
   * @param jsonNode JsonNode
   * @return String
   */
  private static String getJsonNodeString(JsonNode jsonNode) {
    if (jsonNode == null) {
      return null;
    }
    //去掉字符串左右的引号
    String quotesLeft = "^[\"]";
    String quotesRight = "[\"]$";
    return jsonNode.toString().replaceAll(quotesLeft, "").replaceAll(quotesRight, "");
  }

}
