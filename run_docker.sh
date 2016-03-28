#/bin/bash

polku=`pwd`/docker
tietokannanPolku=`pwd`/db

# Käynnistetään Postgresql:n sisältämä docker-image
docker run -d -p 5432:5432 --name moveatis-db -v $tietokannanPolku:/var/lib/postgresql/data -e 'DB_USER=moveatis' -e 'DB_PASS=lotas' -e 'DB_NAME=moveatisdb' centos/postgresql

# Käynnistetään Wildfly:n sisältämä docker-image
docker run --privileged=true -it -p 8080:8080 -p 8787:8787 -p 9990:9990 --name moveatis-wildfly --link moveatis-db:lotasdb -v $polku:/opt/wildfly/standalone/deployments moveatis/moveatis-devel