package com.feedapp.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDTO {

    private String message;
    private LocalDateTime dateTime;

}