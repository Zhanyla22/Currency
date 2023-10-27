package com.example.exchangerates.dto;

import com.example.exchangerates.enums.ResultCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {

    Object data;

    ResultCode status;

    String message;
}
