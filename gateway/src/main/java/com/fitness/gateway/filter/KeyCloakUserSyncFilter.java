package com.fitness.gateway.filter;


import com.fitness.gateway.dto.UserRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeyCloakUserSyncFilter implements WebFilter {

    @Autowired
    private UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User_ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        UserRequest userRequest = getUserDetails(token);

        if (userId == null && token == null)
            userId = userRequest.getKeycloakId();

        if (userId != null && token != null) {
            String finalUserId = userId;
            return userService.validateUser(userId).flatMap(exist -> {
                if (!exist) {
                    // Register user
                    if (userRequest != null) {
                        return userService.registerUser(userRequest).then(Mono.empty());
                    } else {
                        return Mono.empty();
                    }

                } else {
                    log.info("User already exists, Skipping sync.");
                    return Mono.empty();
                }
            }).then(Mono.defer(() -> {
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-ID", finalUserId).build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }));
        }
        return chain.filter(exchange);
    }

    private UserRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            UserRequest userRequest = new UserRequest();
            userRequest.setEmail(claims.getStringClaim("email"));
            userRequest.setFirstName(claims.getStringClaim("given_name"));
            userRequest.setLastName(claims.getStringClaim("family_name"));
            userRequest.setKeycloakId(claims.getStringClaim("sub"));
            userRequest.setPassword("password");
            return userRequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
