#!/bin/bash

SCRIPT=$(readlink -m $0)
SCRIPT_DIR=$(dirname $SCRIPT)

if [ `find $SCRIPT_DIR/../target -name toolwrapper-bash-generator-*-jar-with-dependencies.jar | wc -l` -ne "1" ]; then
	echo "The jar file doesn't exists! You need to compile it!"
	exit 1
fi

if [[ $# -eq 0 ]]; then
	java -jar $SCRIPT_DIR/../target/toolwrapper-bash-generator-*-jar-with-dependencies.jar
	exit 2
fi

java -jar $SCRIPT_DIR/../target/toolwrapper-bash-generator-*-jar-with-dependencies.jar "$@"
exit $?
