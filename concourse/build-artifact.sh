#!/bin/bash -e

version=`cat $2`
cd $1
./gradlew copyJar -Pversion=$version
