#!/bin/bash

run(){
    mvn compile
    mvn exec:java
}

run_db(){
    mvn compile
    mvn exec:java -X
}
run
