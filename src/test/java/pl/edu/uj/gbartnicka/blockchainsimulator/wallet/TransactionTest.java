package pl.edu.uj.gbartnicka.blockchainsimulator.wallet;

import org.junit.jupiter.api.Test;
import pl.edu.uj.gbartnicka.blockchainsimulator.wallet.keys.Keys;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TransactionTest {

    @Test
    void testConstructAndUpdate() {
        final var senderWallet = new Wallet();
        final var initialBalance = senderWallet.getBalance();
        final var recipientAddress = new PublicAddress(Keys.generateKeys().getPublic());
        final var firstAmount = BigDecimal.ONE;
        var transaction = new Transaction(senderWallet, recipientAddress, firstAmount);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getOutputs()).hasSize(2);
        assertThat(transaction.getOutputs().get(0).getDeltaAmount()).isEqualTo(initialBalance.subtract(firstAmount));
        assertThat(transaction.verify()).isTrue();

        final var secondAmount = BigDecimal.valueOf(2L);
        transaction.update(senderWallet, new PublicAddress(Keys.generateKeys().getPublic()), secondAmount);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getOutputs()).hasSize(3);
        assertThat(transaction.getOutputs().get(0).getDeltaAmount()).isEqualTo(initialBalance.subtract(firstAmount.add(secondAmount)));
        assertThat(transaction.verify()).isTrue();
    }

    @Test
    void testConstructAndUpdateFail() {
        final var senderWallet = new Wallet();
        final var initialBalance = senderWallet.getBalance();
        final var recipientAddress = new PublicAddress(Keys.generateKeys().getPublic());
        final var firstAmount = BigDecimal.valueOf(4L);
        var transaction = new Transaction(senderWallet, recipientAddress, firstAmount);

        assertThat(transaction.isValid()).isTrue();
        assertThat(transaction).isNotNull();
        assertThat(transaction.getOutputs()).hasSize(2);
        assertThat(transaction.getOutputs().get(0).getDeltaAmount()).isEqualTo(initialBalance.subtract(firstAmount));
        assertThat(transaction.verify()).isTrue();

        final var secondAmount = BigDecimal.valueOf(7L);

        assertThatExceptionOfType(BalanceExceededException.class)
                .isThrownBy(() -> transaction.update(senderWallet, new PublicAddress(Keys.generateKeys().getPublic()), secondAmount));
    }

    @Test
    void testCorrupted() {
        var transaction = new Transaction(new Wallet(), new PublicAddress(Keys.generateKeys().getPublic()), BigDecimal.ONE);
        transaction.setOutputs(Collections.singletonList(new Transaction.Output(new PublicAddress(Keys.generateKeys().getPublic()), BigDecimal.ZERO)));

        assertThat(transaction).isNotNull();
        assertThat(transaction.verify()).isFalse();
    }

    @Test
    void testInvalid() {
        assertThatExceptionOfType(BalanceExceededException.class)
                .isThrownBy(() -> new Transaction(new Wallet(), new PublicAddress(Keys.generateKeys().getPublic()), BigDecimal.valueOf(100)));
    }
}