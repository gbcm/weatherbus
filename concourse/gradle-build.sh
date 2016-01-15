#!/bin/bash

export TERM=xterm
pushd $1
./gradlew build
popd
