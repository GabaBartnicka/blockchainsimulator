package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AppRouter {
    @Bean
    public RouterFunction<ServerResponse> route(@NotNull AppHandler appHandler, @NotNull TransactionsHandler transactionsHandler, @NotNull PeersHandler peersHandler, @Value("classpath:/static/index.html") final Resource indexHtml) {
        // @formatter:off
        return RouterFunctions
                .route(GET("/hello").and(accept(MediaType.TEXT_PLAIN)), appHandler::hello)
                .andRoute(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml))
                .andRoute(GET("/wallet").and(accept(MediaType.APPLICATION_JSON)), appHandler::wallet)
                .andRoute(GET("/balance").and(accept(MediaType.APPLICATION_JSON)), appHandler::balance)
                .andRoute(GET("/blocks").and(accept(MediaType.APPLICATION_JSON)), appHandler::blocks)
                .andRoute(GET("/block/{index}").and(accept(MediaType.APPLICATION_JSON)), appHandler::blockByIndex)
                .andRoute(POST("/transaction").and(accept(MediaType.APPLICATION_JSON)), transactionsHandler::performTransaction)
                .andRoute(POST("/mine").and(accept(MediaType.APPLICATION_JSON)), appHandler::mine)
                .andRoute(GET("/mine/status").and(accept(MediaType.APPLICATION_JSON)), appHandler::mineStatus)
                .andRoute(GET("/transactions").and(accept(MediaType.APPLICATION_JSON)), transactionsHandler::transactionPool)
                .andRoute(GET("/peers").and(accept(MediaType.APPLICATION_JSON)), peersHandler::all)
                .andRoute(GET("/peer/{name}").and(accept(MediaType.APPLICATION_JSON)), peersHandler::byName)
                .andRoute(GET("/blockchain").and(accept(MediaType.APPLICATION_JSON)), appHandler::blockchain);
        // @formatter:on
    }
}
