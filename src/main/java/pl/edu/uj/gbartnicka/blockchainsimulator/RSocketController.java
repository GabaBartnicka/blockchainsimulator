package pl.edu.uj.gbartnicka.blockchainsimulator;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewBlockReceived;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainRequest;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.SimpleMessage;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.TransactionService;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.TransactionPool;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketController {

    private final Peer myself;
    private final NeighbourhoodService neighbourhoodService;
    private final BlockchainService blockchainService;
    private final TransactionService transactionService;
    private final TransactionPool transactionPool;

    private final ApplicationEventPublisher publisher;

    @MessageMapping("request-response")
    SimpleMessage requestResponse(SimpleMessage request) {
        log.info("Received request-response request: {}", request);
        neighbourhoodService.addPeer(request.getPeer());
        return new SimpleMessage("pong", myself);
    }

    BlockEnvelope lastBlock(BlockEnvelope block) {
        log.info("Received block {}", block);

        return new BlockEnvelope(blockchainService.getLastBlock(), myself);
    }

    @MessageMapping("request-blockchain")
    String requestBlockchain(SimpleMessage request) {
        log.info("Received request-blockchain request: {}", request);
        return blockchainService.getBlockchain().toJson();
    }

    @MessageMapping("request-blockchain-sync")
    @NotNull
    BlockchainEnvelope requestBlockchainSync(@NotNull BlockchainRequest request) {
        log.info("Received request-blockchain request: {}", request);
        return blockchainService.envelope();
    }

    @MessageMapping("new-block")
    String newBlockNotification(String envelope) {
        log.info("Received notification about new block: {}", envelope);

        publisher.publishEvent(new NewBlockReceived(this, new Gson().fromJson(envelope, BlockEnvelope.class)));

        return blockchainService.getBlockchain().toJson();
    }

    @MessageMapping("new-transaction")
    String newTransactionNotification(String transaction) {
        log.info("Received notification about new transaction: {}", transaction);
        transactionService.handleIncomingTransaction(transaction);
        return "ok";
    }

    @MessageMapping("clear-pool")
    String clearTransactionPool(String message) {
        log.info("Received {} message", message);
        transactionPool.clear();
        return "ok";
    }

    @MessageMapping("ping-pong")
    String pingPong(String request) {
        log.info("Received request-response request: {}", request);
        return "pong";
    }
}