package com.example.exchangerates.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverUtil {

    /**
     * переобразование с json на объект
     * @param json
     * @param tClass
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public static <T> T toObject(String json, Class<T> tClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, tClass);
    }
}
