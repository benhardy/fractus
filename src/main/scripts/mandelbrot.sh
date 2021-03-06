#!/usr/bin/env bash
SELF=`readlink -f $0`
#BINDIR="${SELF%/*}"
#LIBDIR="${BINDIR%/*}/lib"
LIBDIR=../lib
CLASSPATH=`find ${LIBDIR} -type f -name "*jar" | tr '\n' ':'`
MAIN_CLASS="net.aethersanctum.fractus.Mandelbrot"
JAVA_OPTS="-Xmx1024m"
echo "java -cp $CLASSPATH $JAVA_OPTS $MAIN_CLASS $*"
java -cp $CLASSPATH $JAVA_OPTS "$MAIN_CLASS" $*
