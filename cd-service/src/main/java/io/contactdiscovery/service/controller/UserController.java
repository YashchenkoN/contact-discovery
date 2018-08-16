package io.contactdiscovery.service.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.contactdiscovery.service.api.ActivateDeviceRequest;
import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.api.RegisterUserResponse;
import io.contactdiscovery.service.service.UserService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<RegisterUserResponse> register(@RequestBody @Valid final Mono<RegisterUserRequest> request) {
        return request.flatMap(userService::register);
    }

    @PutMapping("/{id}")
    public Mono<Void> activate(@PathVariable("id") final String deviceId,
                               @Valid final Mono<ActivateDeviceRequest> request) {
        return request.flatMap(b -> userService.activate(deviceId, b));
    }
}
