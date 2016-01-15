#!/bin/sh

export TERM=xterm
pushd ..
./gradlew build
popd
