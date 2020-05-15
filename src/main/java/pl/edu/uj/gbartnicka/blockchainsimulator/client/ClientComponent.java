package pl.edu.uj.gbartnicka.blockchainsimulator.client;

//@Slf4j
//@ShellComponent
//public class ClientComponent {
//    private final RSocketRequester rsocketRequester;
//
//    @ShellMethod("Send one request. One response will be printed.")
//    public void requestResponse() throws InterruptedException {
//        log.info("\nSending one request. Waiting for one response...");
//        SimpleMessage message = this.rsocketRequester
//                .route("request-response")
//                .data(new SimpleMessage(""))
//                .retrieveMono(SimpleMessage.class)
//                .block();
//
//        log.info("\nResponse was: {}", message);
//    }
//
//    @Autowired
//    public ClientComponent(RSocketRequester.Builder rsocketRequesterBuilder) {
//        this.rsocketRequester = rsocketRequesterBuilder.connectTcp("localhost", 7000).block();
//    }
//}
