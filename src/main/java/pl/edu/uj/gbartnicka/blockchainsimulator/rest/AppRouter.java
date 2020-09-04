package pl.edu.uj.gbartnicka.blockchainsimulator.rest;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AppRouter {
    @Bean
    public RouterFunction<ServerResponse> route(@NotNull AppHandler appHandler, @NotNull TransactionsHandler transactionsHandler, @NotNull PeersHandler peersHandler, @Value("classpath:/public/index.html") final Resource indexHtml) {
        // @formatter:off
        return RouterFunctions
                .resources("/**", new ClassPathResource("public/"))
                .andRoute(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml))
                .andRoute(GET("/blockchain"),  req -> ServerResponse.temporaryRedirect(URI.create("/")).build())
                .andRoute(GET("/transactions"),  req -> ServerResponse.temporaryRedirect(URI.create("/")).build())
                .andRoute(GET("/nodes"),  req -> ServerResponse.temporaryRedirect(URI.create("/")).build())
                .andRoute(GET("/v0/hello").and(accept(MediaType.TEXT_PLAIN)), appHandler::hello)
                .andRoute(GET("/v0/wallet").and(accept(MediaType.APPLICATION_JSON)), appHandler::wallet)
                .andRoute(GET("/v0/balance").and(accept(MediaType.APPLICATION_JSON)), appHandler::balance)
                .andRoute(GET("/v0/blocks").and(accept(MediaType.APPLICATION_JSON)), appHandler::blocks)
                .andRoute(GET("/v0/block/{index}").and(accept(MediaType.APPLICATION_JSON)), appHandler::blockByIndex)
                .andRoute(POST("/v0/transaction").and(accept(MediaType.APPLICATION_JSON)), transactionsHandler::performTransaction)
                .andRoute(GET("/v0/transactions").and(accept(MediaType.APPLICATION_JSON)), transactionsHandler::transactionPool)
                .andRoute(POST("/v0/mine").and(accept(MediaType.APPLICATION_JSON)), appHandler::mine)
                .andRoute(GET("/v0/mine/status").and(accept(MediaType.APPLICATION_JSON)), appHandler::mineStatus)
                .andRoute(GET("/v0/peers").and(accept(MediaType.APPLICATION_JSON)), peersHandler::all)
                .andRoute(GET("/v0/peer/{name}").and(accept(MediaType.APPLICATION_JSON)), peersHandler::byName)
                .andRoute(DELETE("/v0/peer/{name}").and(accept(MediaType.APPLICATION_JSON)), peersHandler::deletePeer)
                .andRoute(PUT("/v0/peer/new").and(accept(MediaType.APPLICATION_JSON)), peersHandler::addNewPeer)
                .andRoute(POST("/v0/peer/ping").and(accept(MediaType.APPLICATION_JSON)), peersHandler::ping)
                .andRoute(GET("/v0/blockchain").and(accept(MediaType.APPLICATION_JSON)), appHandler::blockchain);
        // @formatter:on
    }
}
