package pl.edu.uj.gbartnicka.blockchainsimulator.hooks.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.uj.gbartnicka.blockchainsimulator.hooks.DataGenerator;

@Profile("default")
@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultDataGeneratorImpl implements DataGenerator {
}
