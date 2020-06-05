package pl.edu.uj.gbartnicka.blockchainsimulator.service;


import org.junit.jupiter.api.Test;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Block;
import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;

import java.util.ArrayList;

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
}