package com.feedapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String text;

    private String user;

    private Integer kids;
}
