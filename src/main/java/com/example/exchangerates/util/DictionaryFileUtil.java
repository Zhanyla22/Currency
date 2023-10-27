package com.example.exchangerates.util;

import com.example.exchangerates.dto.ApilayerResponseDto;
import com.example.exchangerates.exceptions.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DictionaryFileUtil {
    final ObjectMapper mapper = new ObjectMapper();
    final EncryptionUtil encryptionUtil;
    final LocalLoaderUtil<String> localLoaderUtil;

    @Value("${secret.key.file}")
    String secretKey;

    public DictionaryFileUtil(EncryptionUtil encryptionUtil, LocalLoaderUtil<String> localLoaderUtil) {
        this.encryptionUtil = encryptionUtil;
        this.localLoaderUtil = localLoaderUtil;
    }

    public void load(Object models, String fileName) {
        try {
            localLoaderUtil.store(
                    encryptionUtil.encrypt(
                            mapper.writeValueAsString(models),
                            secretKey
                    ),
                    fileName
            );
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());

            throw new JsonParseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public <T> List<ApilayerResponseDto> getModelInFile(String fileName, Class<T> type) {
        String encryptedData = encryptionUtil.decrypt(
                localLoaderUtil.load(String.class, fileName),
                secretKey
        );

        try {
            return mapper.readValue(
                    new JSONArray(encryptedData).toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, type)
            );
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());

            throw new JsonParseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
