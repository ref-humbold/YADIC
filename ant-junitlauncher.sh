#! /bin/sh

set -ev

ANT=$(ant -version | sed -r 's/.*version ([0-9.]+).*/\1/g')
LINK="https://repo1.maven.org/maven2/org/apache/ant/ant-junitlauncher/${ANT}/ant-junitlauncher-${ANT}.jar"

echo "ant version: $ANT"
echo "link: $LINK"
wget $LINK -P ~/.ant/lib
