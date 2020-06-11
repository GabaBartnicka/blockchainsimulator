//package pl.edu.uj.gbartnicka.blockchainsimulator;
//
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.rsocket.RSocketRequester;
//import org.springframework.shell.Input;
//import org.springframework.shell.Shell;
//import org.springframework.shell.result.DefaultResultHandler;
//import pl.edu.uj.gbartnicka.blockchainsimulator.data.Blockchain;
//import pl.edu.uj.gbartnicka.blockchainsimulator.data.SimpleMessage;
//import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
//import pl.edu.uj.gbartnicka.blockchainsimulator.service.BlockchainService;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(classes =CliAppRunner.class)
//class RSocketControllerTest {
//    private static RSocketRequester requester;
//
////    @Autowired
////    private Shell shell;
//
////    @Autowired
////    private  RSocketRequester.Builder rsocketRequesterBuilder;
//
////    @BeforeAll
////    public static void setupOnce(@Autowired RSocketRequester.Builder builder, @Value("${spring.rsocket.server.port}") Integer port) {
//////        requester = builder
//////                .connectTcp("localhost", port)
//////                .block();
////    }
////
//
//    @Autowired
//    BlockchainService blockchainService;
//
//    @Test
//    void name() {
//        assertThat(Boolean.TRUE).isTrue();
//    }
////
////    @Test
////    @Disabled
////    public void testRequestGetsResponse() {
////        requester = rsocketRequesterBuilder.connectTcp("localhost", 7000)
////                .block();
//////        var resultShell =  shell.evaluate(() -> "wallet");
//////        DefaultResultHandler resulthandler=new DefaultResultHandler();
//////        resulthandler.handleResult(resultShell);
////
////        // Send a request message (1)
////        final var peer = new Peer(1000);
////        final var content = "TEST";
////        Mono<SimpleMessage> result = requester
////                .route("request-response")
////                .data(new SimpleMessage(content, peer))
////                .retrieveMono(SimpleMessage.class);
////
////        // Verify that the response message contains the expected data (2)
////        StepVerifier
////                .create(result)
////                .consumeNextWith(message -> {
////                    assertThat(message.getPeer()).isEqualTo(peer);
////                    assertThat(message.getContent()).isEqualTo(content);
//////                    assertThat(message.getOrigin()).isEqualTo(RSocketController.SERVER);
//////                    assertThat(message.getInteraction()).isEqualTo(RSocketController.RESPONSE);
//////                    assertThat(message.getIndex()).isEqualTo(0);
////                })
////                .verifyComplete();
////    }
//
////    @Autowired
////    private Shell shell;
//
////    @Test
////    public void playerCanRecordEntireScoreOfGame() {
////        assertThat(shell.evaluate(() -> "record 7")).isEqualTo("Your bowling game score is 7! Well done!");
////    }
//}