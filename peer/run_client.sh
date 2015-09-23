#!/bin/bash
rm *.class
javac PeerImpl.java
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:/home/harshul/workspace/distributedMazeGame/peer/simple.jar PeerImpl 127.0.0.1
