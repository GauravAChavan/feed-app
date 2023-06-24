package com.feedapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Document("stories")
public class Story implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    private String title;

    private Integer score;

    private String url;

    private LocalDateTime submissionTime;

    private String user;
}
