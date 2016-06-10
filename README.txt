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
                                     

Budowanie:

# do zbudowania potrzebny jest zainstalowany program gradle
# w folderze z plikiem build.gradle nalezy wykonac polecenie:
$ gradle build


Użytkowanie:

$ java -jar buid/libs/lzw-1.0.0.jar -h
$ java -jar buid/libs/lzw-1.0.0.jar encode -v -i file_to_encode -o encoded_output
$ java -jar buid/libs/lzw-1.0.0.jar decode -v -i file_to_decode -o decoded_output


Generacja danych z zadanym rozkładem:

# -distribution:
#	- gauss
#	- uniform
#	- laplace

$ java -jar buid/libs/lzw-1.0.0.jar generate -o ImageGaussian.bmp -distribution gauss