package pl.edu.uj.gbartnicka.blockchainsimulator.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.edu.uj.gbartnicka.blockchainsimulator.events.types.NewBlockReceived;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.PeerConnector;
import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageReceivedListener {

    private final Peer myself;
    private final BlockchainService blockchainService;
    private final PeerConnector peerConnector;

    @EventListener
    public void onNewBlockReceived(@NotNull NewBlockReceived event) {
        final var eventBlock = event.getBlock();
        if (eventBlock.getSender().equals(myself)) {
            log.warn("Skipping message");
        }

        log.info("Received new block with index {} from peer {}", eventBlock.getBlock().getIndex(), event.getBlock().getSender().getName());

        blockchainService.onNewBlock(eventBlock);

        peerConnector.sendNewBlockInfoToAll(eventBlock.toJson());
    }
}
