#/bin/bash

polku=`pwd`/docker
tietokannanPolku=`pwd`/db

echo "Yritetään käynnistää db-image"
docker start moveatis-db 2>&1
# Luetaan virheilmoitus, jos db-imagea ei vielä löydy
if [ $? -ne 0 ]; then
    # Jos arvo ei ollut 0, pitää moveatis db ensin luoda
    docker create -p 5432:5432 --name moveatis-db -v $tietokannanPolku:/var/lib/postgresql/data -e 'DB_USER=moveatis' -e 'DB_PASS=lotas' -e 'DB_NAME=moveatisdb' centos/postgresql 2>&1
    # Ja sitten käynnistää
    docker start moveatis-db 2>&1
fi
echo "db-image käynnissä"

echo "Yritetään käynnissää wildfly-image"
# Seuraavaksi sama Wildfly-imagelle
docker start -a -i moveatis-wildfly 2>/dev/null # Virheilmoitukset bittitaivaiseen
# Luetaan virheilmoitus, jos wildlfy-imagea ei vielä löydy
if [ $? -ne 0 ]; then
    docker create --privileged=true -it -p 8080:8080 -p 8787:8787 -p 9990:9990 --name moveatis-wildfly --link moveatis-db:lotasdb -v $polku:/opt/wildfly/standalone/deployments moveatis/moveatis-devel 2>/dev/null
    # Ja käynnistetään
    docker start -a -i moveatis-wildfy 2>/dev/null
fi

