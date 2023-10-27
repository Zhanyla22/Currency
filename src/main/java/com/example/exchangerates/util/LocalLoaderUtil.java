package com.example.exchangerates.util;

import com.example.exchangerates.exceptions.FileException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocalLoaderUtil<T> {
    /**
     * директория файла
     */
    @Value("${store.dir:currency_data}")
    String path;

    final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    /**
     * сохранение файла
     * @param data -объект- данные
     * @param fileName - название файла
     */
    public void store(T data, String fileName) {
        try {
            try (FileWriter writer = new FileWriter(getFilePath(fileName).toFile(), false)) {
                mapper.writeValue(writer, data);
            }
        } catch (IOException e) {
            log.error(e.getMessage());

            throw new FileException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Для чтения файла
     * @param type класс-объект
     * @param fileName - название файла
     * @return
     */
    public T load(Class<T> type, String fileName) {
        try {
            try (FileReader reader = new FileReader(getFilePath(fileName).toFile())) {
                return mapper.readValue(reader, type);
            }
        } catch (IOException e) {
            log.error(e.getMessage());

            throw new FileException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * получение пути к файлу, создание файла
     */
    private Path getFilePath(String fileName) {
        File storedFile = new File(
                Paths.get(String.format("%s", path)).toFile().toURI()
        );

        if (!storedFile.isFile())
            storedFile.mkdir();

        try {
            File jsonDocument = new File(storedFile, fileName);

            if (!jsonDocument.exists())
                jsonDocument.createNewFile();

            return jsonDocument.toPath();
        } catch (IOException e) {
            log.error(e.getMessage());

            throw new FileException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}