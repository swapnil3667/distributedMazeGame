#!/bin/bash
rm *.class
javac PeerImpl.java
jar cvf simple.jar *.class
java PeerImpl Primary 127.0.0.1 
