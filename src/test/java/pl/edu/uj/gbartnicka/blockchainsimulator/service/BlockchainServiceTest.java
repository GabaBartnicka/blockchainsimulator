package pl.edu.uj.gbartnicka.blockchainsimulator.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockchainServiceTest {

    @Test
    void name() {
        Blockchain olderBlockchain = new Blockchain();
        olderBlockchain.setDifficulty(1);
        olderBlockchain.addBlock(new Block(1, "1"));
        olderBlockchain.addBlock(new Block(2, "2"));
        var chain = olderBlockchain.getChain();

        Blockchain newerBlockchain = new Blockchain();
        newerBlockchain.setDifficulty(1);

        var chain2 = new ArrayList<>(chain);
        chain2.remove(2);

        newerBlockchain.replaceChains(chain2);
        newerBlockchain.addBlock(new Block(2, "invalid"));

        var blockReconciler = new BlockReconciler();

    }

    @Test
    void testGetRangedNegativeValues() {
        Blockchain blockchain = mock(Blockchain.class);
        BlockchainService blockchainService = spy(new BlockchainService(
                blockchain, null, null, null, null, null
        ));

        var ranged = blockchainService.ranged(-51, -1);

        assertThat(ranged).isEmpty();
    }

    @Test
    void testGetRanged() {
        Blockchain blockchain = mock(Blockchain.class);
        BlockchainService blockchainService = spy(new BlockchainService(
                blockchain, null, null, null, null, null
        ));

        var block2 = new Block(2, "a");
        var block3 = new Block(3, "a");
        when(blockchain.getChain()).thenReturn(Arrays.asList(new Block(1, "d"), block2, block3));

        var ranged = blockchainService.ranged(1, 2);

        assertThat(ranged).containsExactly(block3, block2);
    }
}