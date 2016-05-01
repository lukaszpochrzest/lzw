   ,--,                              
,---.'|          ,----,              
|   | :        .'   .`|        .---. 
:   : |     .'   .'   ;       /. ./| 
|   ' :   ,---, '    .'   .--'.  ' ; 
;   ; '   |   :     ./   /__./ \ : | 
'   | |__ ;   | .'  /.--'.  '   \' . 
|   | :.'|`---' /  ;/___/ \ |    ' ' 
'   :    ;  /  ;  / ;   \  \;      : 
|   |  ./  ;  /  /--,\   ;  `      | 
;   : ;   /  /  / .`| .   \    .\  ; 
|   ,/  ./__;       :  \   \   ' \ | 
'---'   |   :     .'    :   '  |--"  
        ;   |  .'        \   \ ;     
        `---'             '---"      
                                     

Build:
(DIR_WITH_build.gradle)$ gradle build

Use:

$ java -jar buid/libs/lzw-1.0.0.jar -h
$ java -jar buid/libs/lzw-1.0.0.jar encode -v -i file_to_encode -o encoded_output
$ java -jar buid/libs/lzw-1.0.0.jar decode -v -i file_to_decode -o decoded_output

Generate images
$ java -jar buid/libs/lzw-1.0.0.jar generate -o ImageGaussian.bmp -distribution gauss

-distribution:
	- gauss
	- uniform
	- laplace
