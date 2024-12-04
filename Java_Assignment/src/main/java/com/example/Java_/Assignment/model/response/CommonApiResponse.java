package com.example.Java_.Assignment.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
}
