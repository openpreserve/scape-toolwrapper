#!/bin/bash

usage(){
	echo -e "Usage: 
$0
Run with default values, where toolspecs are in 'developmentExamples', output directory is 'output_dir' and maintainer is info@scape-project.eu

$0 TOOLSPECS TOOLWRAPPER_BASE_DIR DEBIAN_OUTPUT_DIRECTORY MAINTAINER_EMAIL
\twhere:
\t  TOOLSPECS                         > path to the directory where the toolspecs are or path to a specific toolspec
\t  TOOLWRAPPER_BASE_DIR              > path to the toolwrapper base directory
\t  DEBIAN_OUTPUT_DIRECTORY           > path to the directory where the Debian packages are going to be copied
\t  MAINTAINER_EMAIL                  > e-mail of the Debian package maintainer"
}

if [ $# -eq 0 ]; then
cd "$(dirname "$0")"
TOOLSPECS="../developmentExamples"
TOOLWRAPPER_BASE_DIR=$(readlink -m "..")
DEBIAN_OUTPUT_DIRECTORY=$(readlink -m "../output_dir")
MAINTAINER_EMAIL="info@scape-project.eu"
else
    if [ $# -ne 4 ]; then
        echo -e "\e[31mERROR: Wrong command syntax\e[0m"
    	usage
    	exit 1
    else 
    TOOLSPECS=$(readlink -m $1)
    TOOLWRAPPER_BASE_DIR=$(readlink -m $2)
    DEBIAN_OUTPUT_DIRECTORY=$(readlink -m $3)
    MAINTAINER_EMAIL=$4
    cd "$(dirname "$0")"
    fi
fi


if [ -d $TOOLSPECS ]; then
	LS_EXPRESSION="$TOOLSPECS/*.xml"
else
	if [ -f $TOOLSPECS ]; then
		LS_EXPRESSION="$TOOLSPECS"
	else
        echo -e "\e[31mERROR: $TOOLSPECS is not an existing file or directory\e[0m"
		usage
		exit 2
	fi
fi

echo -e "\e[33m[WARN] This script only aplies to toolspecs with 1 operation, as the others need extra parameters for Debian package generation.\e[0m"

rm -rf $DEBIAN_OUTPUT_DIRECTORY
mkdir -p $DEBIAN_OUTPUT_DIRECTORY

COUNT=0
OK=0

for i in $(ls $LS_EXPRESSION);
do
	TOOLSPEC_PATH=$i
	TOOLSPEC_SCRIPT_PATH=`echo $TOOLSPEC_PATH | sed 's#\.[a-zA-Z]\+#.sh#'`
	CHANGELOG_PATH=`echo $TOOLSPEC_PATH | sed 's#\.[a-zA-Z]\+#.changelog#'`
	COMPONENT_PATH=`echo $TOOLSPEC_PATH | sed 's#\.[a-zA-Z]\+#.component#'`
	TEMP_FOLDER=$(mktemp -d)

    COUNT=$((COUNT+1))
    echo -e "\e[32mTOOLSPEC $TOOLSPEC_PATH\e[0m"

	$TOOLWRAPPER_BASE_DIR/toolwrapper-bash-generator/bin/generate.sh -t $TOOLSPEC_PATH -o $TEMP_FOLDER -c $COMPONENT_PATH
    if [ $? -ne 0 ]; then
        continue
    fi
	if [ -e $TOOLSPEC_SCRIPT_PATH ]; then
		cp $TOOLSPEC_SCRIPT_PATH "$TEMP_FOLDER/install/"
	fi
	$TOOLWRAPPER_BASE_DIR/toolwrapper-bash-debian-generator/bin/generate.sh -t $TOOLSPEC_PATH -i $TEMP_FOLDER -o $TEMP_FOLDER -e $MAINTAINER_EMAIL -ch $CHANGELOG_PATH
    if [ $? -eq 0 ]; then
        OK=$((OK+1))
    fi
	if [ $? -eq 0 ]; then
		for i in $(find $TEMP_FOLDER -name *.deb);
		do
			cp $i $DEBIAN_OUTPUT_DIRECTORY
		done
	fi
	rm -rf $TEMP_FOLDER
done

if [ $COUNT -eq $OK ]; then
    echo -e "\e[32mALL SUCCESSFUL [$OK/$COUNT]\e[0m"
    exit 0
else
    echo -e "\e[31mSOME FAILURES [$OK/$COUNT]\e[0m"
    exit 1
fi

