//package pl.edu.uj.gbartnicka.blockchainsimulator.utils;
//
//import lombok.RequiredArgsConstructor;
//import org.jline.utils.AttributedString;
//import org.jline.utils.AttributedStyle;
//import org.springframework.shell.jline.PromptProvider;
//import org.springframework.stereotype.Component;
//import pl.edu.uj.gbartnicka.blockchainsimulator.neighbourhood.Peer;
//
//@Component
//@RequiredArgsConstructor
//public class CustomPromptProvider implements PromptProvider {
//
//    private final Peer me;
//
//    @Override
//    public AttributedString getPrompt() {
//        return new AttributedString(me.getName() + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
//    }
//
//}
//
//
