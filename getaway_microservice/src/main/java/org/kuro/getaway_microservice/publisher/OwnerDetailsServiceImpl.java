package org.kuro.getaway_microservice.publisher;

import org.kuro.entity.dto.OwnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OwnerDetailsServiceImpl implements UserDetailsService {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.password.key}")
    private String routingPasswordKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerDetailsServiceImpl.class);

    private RabbitTemplate rabbitTemplate;

    public OwnerDetailsServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info(String.format("Checking user: %s", username));

        OwnerDto ownerDto = (OwnerDto) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive(exchange, routingPasswordKey, username));

        if (ownerDto.getName().isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> auth = Arrays.stream(ownerDto.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(ownerDto.getName(), ownerDto.getPassword(), auth);
    }
}