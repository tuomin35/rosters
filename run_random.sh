#!/bin/sh
java -cp Rosters.jar com.github.tuomin35.rosters --random
if [ "$?" -eq 0 ]
then
  cat rosters.txt
else
  echo "ERROR_CODE: $?"
fi
echo ""
read -p "Press Enter to continue..."
