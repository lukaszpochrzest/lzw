#!/bin/bash
if [ -e test_gen_imgs_results.txt ]
	then
		rm test_gen_imgs_results.txt
fi

for i in `seq 1 10`;
do
	java -jar ../build/libs/lzw-1.0.0.jar generate -o "gaussian$i.bmp" -distribution gauss
	java -jar ../build/libs/lzw-1.0.0.jar generate -o "laplace$i.bmp" -distribution laplace
	java -jar ../build/libs/lzw-1.0.0.jar generate -o "uniform$i.bmp" -distribution uniform
done

FILES="*.bmp"
for f in $FILES
do
        java -jar ../build/libs/lzw-1.0.0.jar encode -testlog -i $f -o trash_random12341234 | tee -a test_gen_imgs_results.txt
done

rm *.bmp

if [ -e trash_random12341234 ]
	then
		rm trash_random12341234
fi
