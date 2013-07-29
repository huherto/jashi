#!/bin/bash

set -e # End if any command fails.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

INSTALL_DIR=~/.jashi

mkdir -p $INSTALL_DIR

cd $DIR
mvn install # install target/jashi-*.jar in ~/.m2 repository

cd $DIR/jashi-exec
mvn clean
mvn package

JASHI_JAR=$DIR/jashi-exec/target/jashi-exec-*.jar
cp $JASHI_JAR $INSTALL_DIR/jashi-exec.jar

(
	echo "#!/bin/bash"
	echo "java -jar $INSTALL_DIR/jashi-exec.jar "'$*'
) > $INSTALL_DIR/jashi

chmod +x $INSTALL_DIR/jashi

echo "Please add this to your ~/.bashrc"
echo "alias jashi=$INSTALL_DIR/jashi"
