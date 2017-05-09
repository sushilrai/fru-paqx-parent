#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#
#  USAGE: (from .. dir)
#    docker build -f fru-paqx-distribution/Dockerfile --tag=fru-paqx fru-paqx
#

# Start from base image with Java 8
FROM java:8-jre-alpine

RUN mkdir -p /opt/dell/cpsd/fru-paqx/bin
RUN mkdir -p /opt/dell/cpsd/fru-paqx/logs
RUN mkdir -p /opt/dell/cpsd/fru-paqx/lib
RUN mkdir -p /opt/dell/cpsd/fru-paqx/install

COPY target/dependency/lib/fru-paqx-*.jar /opt/dell/cpsd/fru-paqx/lib/fru-paqx.jar
COPY docker/image/bin/run.sh /opt/dell/cpsd/fru-paqx/bin/

RUN chmod 755 /opt/dell/cpsd/fru-paqx/bin/run.sh

EXPOSE 8085

CMD ["/opt/dell/cpsd/fru-paqx/bin/run.sh"]
