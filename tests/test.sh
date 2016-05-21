#!/bin/bash
if [ -e test_results.txt ]
	then
		rm test_results.txt
fi

FILES=$1
for f in $FILES
do
	java -jar ../build/libs/lzw-1.0.0.jar encode -testlog -i $f -o trash_random129312380 | tee -a test_results.txt
done

if [ -e trash_random129312380 ]
	then
		rm trash_random129312380
fi
