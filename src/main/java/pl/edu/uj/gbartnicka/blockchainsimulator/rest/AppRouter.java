package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AppRouter {
    @Bean
    public RouterFunction<ServerResponse> route(@NotNull AppHandler appHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), appHandler::hello)
                .andRoute(RequestPredicates.GET("/wallet").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appHandler::wallet)
                .andRoute(RequestPredicates.GET("/balance").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appHandler::balance)
                .andRoute(RequestPredicates.GET("/blocks").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appHandler::blocks)
                .andRoute(RequestPredicates.GET("/blockchain").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appHandler::blockchain);
    }
}
