#!/usr/bin/bash -e

version = `cat $1`
cd $2
./gradlew copyJar -Pversion=$version
