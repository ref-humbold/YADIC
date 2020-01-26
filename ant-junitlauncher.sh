#! /bin/sh

ANT_VERSION=$(ant -version | sed -r 's/.*version ([0-9.]+).*/\1/g')
LINK="https://repo1.maven.org/maven2/org/apache/ant/ant-junitlauncher/${ANTV}/ant-junitlauncher-${ANTV}.jar"

wget $LINK -P ~/.ant/lib
