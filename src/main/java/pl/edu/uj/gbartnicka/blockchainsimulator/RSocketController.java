package pl.edu.uj.gbartnicka.blockchainsimulator;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.BlockEnvelope;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.NeighbourhoodService;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketController {

    private final Peer myself;
    private final NeighbourhoodService neighbourhoodService;
    private final BlockchainService blockchainService;

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
        return new Gson().toJson(blockchainService.getBlockchain());
    }

    @MessageMapping("new-block")
    String newBlockNotification(String envelope) {
        log.info("Received notification about new block: {}", envelope);
        blockchainService.onNewBlock(envelope);
        return new Gson().toJson(blockchainService.getBlockchain());
    }
}