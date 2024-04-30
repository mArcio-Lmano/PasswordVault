#!/bin/bash

run(){
    mvn compile
    mvn exec:java
}

run
