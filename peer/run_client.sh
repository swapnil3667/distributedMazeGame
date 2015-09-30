#!/bin/bash
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:simple.jar PeerImpl 127.0.0.1
