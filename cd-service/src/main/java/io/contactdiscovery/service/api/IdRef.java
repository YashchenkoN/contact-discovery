package io.contactdiscovery.service.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * @author Mykola Yashchenko
 */
@Getter
public class IdRef {
    private final String id;

    @JsonCreator
    public IdRef(@JsonProperty("id") final String id) {
        this.id = id;
    }

}
