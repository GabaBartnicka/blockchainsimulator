#!/bin/bash
#/Users/gaba/Library/Java/JavaVirtualMachines/openjdk-14.0.1/Contents/Home/bin/java -jar  ./build/libs/blockchainsimulator-0.0.1.jar
/Users/gaba/Library/Java/JavaVirtualMachines/openjdk-14.0.1/Contents/Home/bin/java -jar ./build/libs/blockchainsimulator-0.0.1.jar --spring.rsocket.server.port=8083
# docker build . -f docker/Dockerfile -t gaba-blockchain