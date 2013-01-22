#!/bin/bash

INSTALL_DIR=~/.jashi

mkdir -p $INSTALL_DIR/bin $INSTALL_DIR/jars

mvn package
mvn dependency:copy-dependencies

cp target/dependency/*.jar $INSTALL_DIR/jars

CLASSPATH=""
for JAR in $INSTALL_DIR/jars/*.jar
do
	CLASSPATH=$CLASSPATH:$JAR
done

JASHI_JAR=target/jashi-*.jar
cp $JASHI_JAR $INSTALL_DIR/jashi.jar

(
	echo "#!/bin/bash"
	echo "java -cp $INSTALL_DIR/jashi.jar$CLASSPATH jashi.Executor "'$*'
) > $INSTALL_DIR/jashi

chmod +x $INSTALL_DIR/jashi

echo "Please add this to your ~/.bashrc"
echo "alias jashi=$INSTALL_DIR/jashi"