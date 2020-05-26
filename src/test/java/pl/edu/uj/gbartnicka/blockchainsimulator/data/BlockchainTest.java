package pl.edu.uj.gbartnicka.blockchainsimulator.data;


import org.junit.jupiter.api.Test;

class BlockchainTest {

    @Test
    void test() {
        Blockchain blockchain = new Blockchain();

        blockchain.addBlock(new Block(1, "some data"));

        // TODO: assertions
    }
}