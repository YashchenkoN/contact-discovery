package io.contactdiscovery.service.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mykola Yashchenko
 */
public class IdRef {
    private final String id;

    @JsonCreator
    public IdRef(@JsonProperty("id") final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
