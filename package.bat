mkdir out\production\Microcraft
mkdir out\artifacts\Microcraft_jar
javac -sourcepath src src\com\jdh\microcraft\Main.java -d out\production\Microcraft
xcopy /s /e res out\production\
jar -cmvf src\META-INF\MANIFEST.MF out\artifacts\Microcraft_jar\Microcraft.jar -C out\production\Microcraft\ .
jpackage --name MICROCRAFT --input out\artifacts\Microcraft_jar\ --main-jar Microcraft.jar --main-class com.jdh.microcraft.Main --resource-dir res --icon res\icon.ico