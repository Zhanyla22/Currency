package com.example.exchangerates.util;

import com.example.exchangerates.dto.CombinedDto;
import com.example.exchangerates.exceptions.BaseException;
import com.example.exchangerates.exceptions.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DictionaryFileUtil {

    final ObjectMapper mapper = new ObjectMapper();
    final AesUtil aesUtil;
    final LocalLoaderUtil<String> localLoaderUtil;

    @Value("${secret.key.file}")
    String secretKey;

    public DictionaryFileUtil(AesUtil aesUtil, LocalLoaderUtil<String> localLoaderUtil) {
        this.aesUtil = aesUtil;
        this.localLoaderUtil = localLoaderUtil;
    }

    public void load(Object models, String fileName) {
        try {
            localLoaderUtil.store(
                    aesUtil.encrypt(
                            mapper.writeValueAsString(models),
                            secretKey
                    ),
                    fileName
            );
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public <T> List<CombinedDto> getModelInFile(String fileName, Class<T> type) {
        String encryptedData = aesUtil.decrypt(
                localLoaderUtil.load(String.class, fileName),
                secretKey
        );

        try {
            return mapper.readValue(
                    new JSONArray(encryptedData).toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, type)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException e) {
            throw new BaseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
