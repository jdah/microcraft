do_build=true
do_icon=true

while getopts 'bi' flag; do
  case "${flag}" in
    b) do_build=false ;;
    i) do_icon=false ;;
    *) exit 1 ;;
  esac
done

# make icon set
if [ "$do_icon" = true ] ; then
  # MAC ICON
  ICONSET=res/icon.iconset
  ICON=res/icon.png
  mkdir $ICONSET
  sips -z 16 16     $ICON --out $ICONSET/icon_16x16.png
  sips -z 32 32     $ICON --out $ICONSET/icon_16x16@2x.png
  sips -z 32 32     $ICON --out $ICONSET/icon_32x32.png
  sips -z 64 64     $ICON --out $ICONSET/icon_32x32@2x.png
  sips -z 128 128   $ICON --out $ICONSET/icon_128x128.png
  sips -z 256 256   $ICON --out $ICONSET/icon_128x128@2x.png
  sips -z 256 256   $ICON --out $ICONSET/icon_256x256.png
  sips -z 512 512   $ICON --out $ICONSET/icon_256x256@2x.png
  sips -z 512 512   $ICON --out $ICONSET/icon_512x512.png
  sips -z 1024 1024 $ICON --out $ICONSET/icon_512x512@32x.png
  iconutil -c icns $ICONSET

  # WINDOWS ICON
  ICON_WIN=res/icon.ico
  convert $ICONSET/icon_16x16.png\
    $ICONSET/icon_32x32.png\
    $ICONSET/icon_32x32@2x.png\
    $ICONSET/icon_128x128.png\
    $ICONSET/icon_256x256.png\
    $ICON_WIN

  rm -R $ICONSET
fi

if [ "$do_build" =  true ] ; then
  mkdir -p out/production/Microcraft
  mkdir -p out/artifacts/Microcraft_jar
  $JAVA_HOME/bin/javac -sourcepath src src/com/jdh/microcraft/Main.java -d out/production/Microcraft
  cp -r res/ out/production/Microcraft/
  $JAVA_HOME/bin/jar -cmvf src/META-INF/MANIFEST.MF out/artifacts/Microcraft_jar/Microcraft.jar -C out/production/Microcraft/ .
  $JAVA_HOME/bin/jpackage --name MICROCRAFT --input out/artifacts/Microcraft_jar/\
    --main-jar Microcraft.jar --main-class com.jdh.microcraft.Main --resource-dir res --icon res/icon.icns
fi