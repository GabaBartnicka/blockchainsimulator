package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionPoolTest {

    @Test
    void addOrUpdateTest() {
        var senderWallet = new Wallet();
        var address = "address";
        var transaction1 = new Transaction(senderWallet, address, BigDecimal.ONE);
        var transactionPool = new TransactionPool();

        // add first
        transactionPool.addOrUpdate(transaction1);
        assertThat(transactionPool.getTransactions()).hasSize(1);
        assertThat(transactionPool.getTransactions()).first().isEqualTo(transaction1);

        // update
        transaction1.update(senderWallet, address, BigDecimal.valueOf(2L));
        assertThat(transactionPool.getTransactions()).hasSize(1);
        assertThat(transactionPool.getTransactions()).first().isEqualTo(transaction1);

        // add second
        var transaction2 = new Transaction(new Wallet(), address, BigDecimal.ONE);
        transactionPool.addOrUpdate(transaction2);
        assertThat(transactionPool.getTransactions()).hasSize(2);
        assertThat(transactionPool.getTransactions()).first().isEqualTo(transaction1);
        assertThat(transactionPool.getTransactions()).last().isEqualTo(transaction2);
    }

    @Test
    void testWithWallet() {
        var wallet1 = new Wallet();
        var wallet2 = new Wallet();

        var pool = new TransactionPool();

        wallet1.createTransaction(wallet2.getPublicAddress(), BigDecimal.ONE, pool);
        wallet2.createTransaction(wallet1.getPublicAddress(), BigDecimal.ONE, pool);

        assertThat(pool.getTransactions()).hasSize(2);

        wallet1.createTransaction(wallet2.getPublicAddress(), BigDecimal.valueOf(2), pool);
        wallet2.createTransaction(wallet1.getPublicAddress(), BigDecimal.valueOf(4), pool);

        assertThat(pool.getTransactions()).hasSize(2);
    }
}