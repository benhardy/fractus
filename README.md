# fractus

A flame fractal generator written in scala. Vaguely inspired by Electric Sheep. Generates still
frames in a Swing GUI. Has a basic DSL for describing fractal rules. (See examples package for
how to write these).

## CI Status

[![Build Status](https://travis-ci.org/benhardy/fractus.png?branch=master)](https://travis-ci.org/benhardy/fractus)

## Running it

Use either "sbt run" or "mvn exec:exec" commands.

##BUILD NOTES

# JDK 1.7

Has been updated to require JDK 1.7

If building on Mac Mavericks, ensure JAVA_HOME is set correctly

export JAVA_HOME=`/usr/libexec/java_home`

### Test coverage

This project uses scoverage to measure coverage. This SBT goal will run that.

sbt scoverage:test