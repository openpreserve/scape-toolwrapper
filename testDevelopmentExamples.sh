#! /bin/bash

TOOLSPECS=`find developmentExamples -name \*.xml`

COUNT=0
OK=0

for TOOLSPEC in $TOOLSPECS 
do
  COUNT=$((COUNT+1))

  echo -e "\e[31mTOOLSPEC $TOOLSPEC\e[0m"
  echo  -e "\e[32mValidate\e[0m"
  xmllint --noout --schema toolwrapper-data/src/main/resources/tool-1.1_draft.xsd $TOOLSPEC

  if [ $? -ne 0 ]; then
    continue
  fi

  BASENAME="${TOOLSPEC%.*}"
  COMPONENTSPEC="$BASENAME.component"
  if [ ! -f $COMPONENTSPEC ]; then
    echo -e "\e[31mComponent Spec not found!\e[0m Should be at: $COMPONENTSPEC"
    continue
  fi

  OUTPUTDIR="/tmp/$BASENAME"
  if [ -f $OUTPUTDIR ]; then
    rm -rf $OUTPUTDIR
  fi

  echo
  echo  -e "\e[32mGenerating bash wrapper and preservation component into $OUTPUTDIR\e[0m"
  toolwrapper-bash-generator/bin/generate.sh -o $OUTPUTDIR -t $TOOLSPEC -c $COMPONENTSPEC

  if [ $? -ne 0 ]; then
    continue
  fi

  echo
  echo  -e "\e[32mGenerating debian package into $OUTPUTDIR\e[0m"
  CHANGELOG="$BASENAME.changelog"
  if [ ! -f $CHANGELOG ]; then
    echo -e "\e[31mChangelog not found!\e[0m Should be at: $CHANGELOG"
    continue
  fi
  toolwrapper-bash-debian-generator/bin/generate.sh -e info@scape-project.eu -t $TOOLSPEC -ch $CHANGELOG -i $OUTPUTDIR -o $OUTPUTDIR

  if [ $? -eq 0 ]; then
   OK=$((OK+1))
  fi

  echo
done

if [ $COUNT -eq $OK ]; then
  echo -e "\e[32mTEST SUCCESSFUL [$OK/$COUNT]\e[0m"
else
  echo -e "\e[31mTEST FAILED [$OK/$COUNT]\e[0m"
fi
