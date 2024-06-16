package com.example.client.contorller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Response<T> {
    T data;
    String message;
}
