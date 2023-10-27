package com.example.exchangerates.external;

import com.example.exchangerates.dto.ApilayerResponseDto;
import com.example.exchangerates.dto.CombinedDto;
import com.example.exchangerates.dto.ExchangeRatesResponseDto;
import com.example.exchangerates.dto.FixerResponseDto;
import com.example.exchangerates.enums.Currency;
import com.example.exchangerates.enums.Literatura;
import com.example.exchangerates.exceptions.BaseException;
import com.example.exchangerates.util.ConverUtil;
import com.example.exchangerates.util.DictionaryFileUtil;
import com.example.exchangerates.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    @Value("${api.openexchangerates.org}")
    String urlSite2;

    @Value("${api.fixer.io}")
    String urlSite3;

    @Value("${file.name}")
    String fileName;

    @Value("${access.key.apilayer}")
    String accessKey;

    @Value("${access.key.openexchangerates}")
    String accessKey2;

    @Value("${access.key.fixer}")
    String accessKey3;


    @Scheduled(fixedRate = 60000 * 30)
    public void currencyUpdate() throws Exception {
        List<Currency> currencyList = Arrays.asList(Currency.USD, Currency.EUR, Currency.RUB, Currency.CNY, Currency.KZT);
        List<ApilayerResponseDto> resultList = new ArrayList<>();
        List<ExchangeRatesResponseDto> resultListSite2 = new ArrayList<>();
        List<FixerResponseDto> resultListSite3 = new ArrayList<>();
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
                    apilayerResponseDto.setSite(Literatura.APILAYER);
                    apilayerResponseDto.setLocalDate(LocalDate.now().toString());
                    resultList.add(apilayerResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                HttpUrl.Builder url2 = HttpUrl.parse(urlSite2).newBuilder();
                url2.addQueryParameter("app_id", accessKey2)
                        .addQueryParameter("symbols", x.toString());
                try {
                    String response2 = requestUtil.executeRequest(url2.build());
                    Thread.sleep(3000);
                    ExchangeRatesResponseDto exchangeRatesResponseDto = ConverUtil.toObject(response2, ExchangeRatesResponseDto.class);
                    exchangeRatesResponseDto.setSite(Literatura.OPENEXCHANGE);
                    exchangeRatesResponseDto.setLocalDate(LocalDate.now().toString());
                    resultListSite2.add(exchangeRatesResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
                }

                HttpUrl.Builder url3 = HttpUrl.parse(urlSite3).newBuilder();
                url3.addQueryParameter("access_key", accessKey3)
                        .addQueryParameter("symbols", x.toString());
                try {
                    String response3 = requestUtil.executeRequest(url3.build());
                    Thread.sleep(3000);
                    FixerResponseDto fixerResponseDto = ConverUtil.toObject(response3, FixerResponseDto.class);
                    fixerResponseDto.setSite(Literatura.FIXER);
                    fixerResponseDto.setLocalDate(LocalDate.now().toString());
                    resultListSite3.add(fixerResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
                }
            });
            List<CombinedDto> combinedDtos = new ArrayList<>();
            CombinedDto combinedDto = new CombinedDto();

            combinedDto.setApilayerResponseDto(resultList);
            combinedDto.setExchangeRatesResponseDto(resultListSite2);
            combinedDto.setFixerResponseDtos(resultListSite3);

            combinedDtos.add(combinedDto);

            try {
                List<CombinedDto> fileData = dictionaryFileUtil.getModelInFile(fileName, CombinedDto.class);

                if (fileData == null && fileData.isEmpty()) {
                    dictionaryFileUtil.load(combinedDtos, fileName);
                } else {
                    combinedDtos.addAll(fileData);
                    dictionaryFileUtil.load(combinedDtos, fileName);
                }

            } catch (Exception e) {
                throw new BaseException(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BaseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<CombinedDto> getAll() {
        return dictionaryFileUtil.getModelInFile(fileName, CombinedDto.class);
    }

    public List<CombinedDto> getByDate(String date) {
        List<CombinedDto> allData = dictionaryFileUtil.getModelInFile(fileName, CombinedDto.class);
        List<CombinedDto> filteredDate = allData.stream()
                .filter(x -> {
                    return x.getExchangeRatesResponseDto().contains(date)
                            ||
                            x.getApilayerResponseDto().contains(date)
                            ||
                            x.getFixerResponseDtos().contains(date);
                })
                .collect(Collectors.toList());
        return filteredDate;
    }

    public List<CombinedDto> getCurrent() {
        List<Currency> currencyList = Arrays.asList(Currency.USD, Currency.EUR, Currency.RUB, Currency.CNY, Currency.KZT);
        List<ApilayerResponseDto> resultList = new ArrayList<>();
        List<ExchangeRatesResponseDto> resultListSite2 = new ArrayList<>();
        List<FixerResponseDto> resultListSite3 = new ArrayList<>();

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
                    apilayerResponseDto.setSite(Literatura.APILAYER);
                    apilayerResponseDto.setLocalDate(LocalDate.now().toString());
                    resultList.add(apilayerResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                HttpUrl.Builder url2 = HttpUrl.parse(urlSite2).newBuilder();
                url2.addQueryParameter("app_id", accessKey2)
                        .addQueryParameter("symbols", x.toString());
                try {
                    String response2 = requestUtil.executeRequest(url2.build());
                    Thread.sleep(3000);
                    ExchangeRatesResponseDto exchangeRatesResponseDto = ConverUtil.toObject(response2, ExchangeRatesResponseDto.class);
                    exchangeRatesResponseDto.setSite(Literatura.OPENEXCHANGE);
                    exchangeRatesResponseDto.setLocalDate(LocalDate.now().toString());
                    resultListSite2.add(exchangeRatesResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
                }

                HttpUrl.Builder url3 = HttpUrl.parse(urlSite3).newBuilder();
                url3.addQueryParameter("access_key", accessKey3)
                        .addQueryParameter("symbols", x.toString());
                try {
                    String response3 = requestUtil.executeRequest(url3.build());
                    Thread.sleep(3000);
                    FixerResponseDto fixerResponseDto = ConverUtil.toObject(response3, FixerResponseDto.class);
                    fixerResponseDto.setSite(Literatura.FIXER);
                    fixerResponseDto.setLocalDate(LocalDate.now().toString());
                    resultListSite3.add(fixerResponseDto);
                } catch (Exception e) {
                    throw new BaseException(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
                }
            });
            List<CombinedDto> combinedDtos = new ArrayList<>();
            CombinedDto combinedDto = new CombinedDto();

            combinedDto.setApilayerResponseDto(resultList);
            combinedDto.setExchangeRatesResponseDto(resultListSite2);
            combinedDto.setFixerResponseDtos(resultListSite3);

            combinedDtos.add(combinedDto);
            try {
                List<CombinedDto> fileData = dictionaryFileUtil.getModelInFile(fileName, CombinedDto.class);

                if (fileData == null && fileData.isEmpty()) {
                    dictionaryFileUtil.load(combinedDtos, fileName);
                } else {
                    combinedDtos.addAll(fileData);
                    dictionaryFileUtil.load(combinedDtos, fileName);
                }

            } catch (Exception e) {
                throw new BaseException(e.getMessage(), HttpStatus.NOT_FOUND);
            }

            return combinedDtos;

        } catch (Exception e) {
            throw new BaseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
