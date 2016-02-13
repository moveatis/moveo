#/bin/bash

polku=`pwd`/docker

docker run --privileged=true -it -p 8080:8080 -v $polku:/opt/wildfly/standalone/deployments moveatis/moveatis-devel
