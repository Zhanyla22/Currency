package com.example.exchangerates.external;

import com.example.exchangerates.dto.ApilayerResponseDto;
import com.example.exchangerates.enums.Currency;
import com.example.exchangerates.enums.Literatura;
import com.example.exchangerates.util.ConverUtil;
import com.example.exchangerates.util.DictionaryFileUtil;
import com.example.exchangerates.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyUpdateService {

    private final RequestUtil requestUtil;

    private final DictionaryFileUtil dictionaryFileUtil;

    @Value("${api.apilayer.net}")
    String urlSite;

    @Value("${file.name}")
    String fileName;

    @Value("${access.key.apilayer}")
    String accessKey;

    @Scheduled(fixedRate = 60000 * 30)
    public void currencyUpdate() throws Exception {
        List<Currency> currencyList = Arrays.asList(Currency.USD, Currency.EUR, Currency.RUB, Currency.CNY, Currency.KZT);
        List<ApilayerResponseDto> resultList = new ArrayList<>();
//        List<ApilayerResponseDto> resultList = new ArrayList<>();
//        List<ApilayerResponseDto> resultList = new ArrayList<>();
//
        try {
            currencyList.forEach(x -> {
                HttpUrl.Builder uri = HttpUrl.parse(urlSite).newBuilder();
                uri.addQueryParameter("access_key", accessKey)
                        .addQueryParameter("currencies", Currency.KGS.toString())
                        .addQueryParameter("source", x.toString());
                try {
                    String response = requestUtil.executeRequest(uri.build());
                    Thread.sleep(3000);
                    ApilayerResponseDto apilayerResponseDto = ConverUtil.toObject(response, ApilayerResponseDto.class);
                    apilayerResponseDto.setSite(Literatura.SAIT1);
                    resultList.add(apilayerResponseDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            resultList.addAll(dictionaryFileUtil.getModelInFile(fileName, ApilayerResponseDto.class));

            dictionaryFileUtil.load(resultList, fileName);
        } catch (Exception e) {
            throw new Exception();
        }
    }


    public List<ApilayerResponseDto> getAll(String year) {
        return dictionaryFileUtil.getModelInFile(fileName, ApilayerResponseDto.class)
                .stream()
                .filter(x -> {
                    Instant instant = Instant.ofEpochMilli(x.getTimestamp());
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Almaty"));
                    return zonedDateTime.getYear() == Integer.parseInt(year);
                })
                .collect(Collectors.toList());
    }



}
