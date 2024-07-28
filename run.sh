#!/bin/sh

if [ "${SW_AGENT_NAME}" != "" ] ; then
	java  -javaagent:/work/agent/skywalking-agent.jar $APOLLO_META $K8S_VIP $SERVER_PORT -Dfile.encoding=utf-8 -jar $JAVA_OPT boot001.jar
else
    java $APOLLO_META $K8S_VIP $SERVER_PORT -Dfile.encoding=utf-8 -jar $JAVA_OPT boot001.jar
fi
