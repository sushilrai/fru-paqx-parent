#!/bin/sh
CONTAINERID=$(basename "$(cat /proc/1/cpuset)" | cut -c 1-12)
java -jar -Xms64m -Xmx192m -Dcontainer.id=$CONTAINERID /opt/dell/cpsd/fru-paqx/lib/fru-paqx.jar
