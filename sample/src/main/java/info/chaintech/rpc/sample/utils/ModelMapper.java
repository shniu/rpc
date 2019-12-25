package info.chaintech.rpc.sample.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shniu on 2018/11/18.
 */

@Slf4j
public class ModelMapper {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Object make(Class<?> targetType, String fromJson) {
        try {
            init();
            return objectMapper.readValue(fromJson, targetType);
        } catch (IOException e) {
            log.error("JSON转化为对象类型 {} 时出错，json：{}", targetType, fromJson, e);
        }
        return null;
    }

    public List<?> makeList(Class<?> targetType, String fromJson) {
        try {
            init();
            JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, targetType);
            return objectMapper.readValue(fromJson, javaType);
        } catch (IOException e) {
            log.error("JSON转化为对象类型 List of {} 时出错，json：{}", targetType, fromJson, e);
        }
        return null;
    }

    private void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String writeString(Object targetObject) {

        if (targetObject == null) {
            return null;
        }

        String toJson = null;

        try {
            init();
            toJson = objectMapper.writeValueAsString(targetObject);
        } catch (JsonProcessingException e) {
            log.error("将对象装换成JSON字符串出错", e);
        }

        return toJson;
    }

    public String prettify(Object obj) {
        try {
            init();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // swallow
            log.warn(e.getMessage());
        }
        return "";
    }
}
