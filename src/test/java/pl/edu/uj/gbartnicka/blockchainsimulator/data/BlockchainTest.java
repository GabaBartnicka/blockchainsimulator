package pl.edu.uj.gbartnicka.blockchainsimulator.data;


import org.junit.jupiter.api.Test;
import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
import pl.edu.uj.gbartnicka.blockchainsimulator.network.BlockchainEnvelope;

import static org.assertj.core.api.Assertions.assertThat;

class BlockchainTest {

    @Test
    void testMineBlocks() {
        Blockchain blockchain = new Blockchain();
        var difficulty = 4;
        blockchain.setDifficulty(difficulty);
        var zeroPrefix = "0".repeat(Math.max(0, difficulty));

        blockchain.addBlock(new Block(1, "some data 1"));
        blockchain.addBlock(new Block(2, "some data 2"));
        blockchain.addBlock(new Block(3, "some data 3"));

        blockchain.getChain().forEach(e -> System.out.println(e.toString()));

        assertThat(blockchain.getChain()).hasSize(4);
        assertThat(blockchain.getChain().stream()
                .skip(1)
                .map(Block::getHash)
                .filter(e -> e.substring(0, difficulty).equals(zeroPrefix))
                .count()).isEqualTo(3);

        Blockchain blockchain2 = new Blockchain();
        BlockchainEnvelope envelope1 = new BlockchainEnvelope(blockchain, new Peer());
        BlockchainEnvelope envelope2 = new BlockchainEnvelope(blockchain2, new Peer());

        assertThat(envelope1).isGreaterThan(envelope2);
    }
}