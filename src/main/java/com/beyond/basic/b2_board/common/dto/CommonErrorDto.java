package com.beyond.basic.b2_board.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CommonErrorDto {
    private int statusCode;
    private String statusMessage;
}
