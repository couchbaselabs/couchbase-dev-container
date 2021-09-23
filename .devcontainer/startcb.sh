#!/bin/bash

CB_USER="${CB_USER:-Administrator}"
CB_PSWD="${CB_PSWD:-password}"
CB_HOST="${CB_HOST:-127.0.0.1}"
CB_PORT="${CB_PORT:-8091}"
CB_NAME="${CB_NAME:-Developer}"

CB_SERVICES="${CB_SERVICES:-data,query,index,fts}"

CB_BUCKET_RAMSIZE="${CB_BUCKET_RAMSIZE:-256}"
CB_KV_RAMSIZE="${CB_KV_RAMSIZE:-512}"
CB_INDEX_RAMSIZE="${CB_INDEX_RAMSIZE:-256}"
CB_FTS_RAMSIZE="${CB_FTS_RAMSIZE:-256}"
CB_EVENTING_RAMSIZE="${CB_EVENTING_RAMSIZE:-512}"
CB_ANALYTICS_RAMSIZE="${CB_ANALYTICS_RAMSIZE:-1024}"

set -euo pipefail

COUCHBASE_TOP=/opt/couchbase

echo "Start couchbase..."
couchbase-server --start

echo "Waiting for couchbase-server..."
until curl -s http://${CB_HOST}:${CB_PORT}/pools > /dev/null; do
    sleep 5
    echo "Waiting for couchbase-server..."
done

echo "Waiting for couchbase-server... ready"

if ! couchbase-cli server-list -c ${CB_HOST}:${CB_PORT} -u ${CB_USER} -p ${CB_PSWD} > /dev/null; then
  echo "couchbase cluster-init..."
  couchbase-cli cluster-init \
        --services ${CB_SERVICES} \
        --cluster-name ${CB_NAME} \
        --cluster-username ${CB_USER} \
        --cluster-password ${CB_PSWD} \
        --cluster-ramsize ${CB_KV_RAMSIZE} \
        --cluster-index-ramsize ${CB_INDEX_RAMSIZE} \
        --cluster-fts-ramsize ${CB_FTS_RAMSIZE} \
        --cluster-eventing-ramsize ${CB_EVENTING_RAMSIZE} \
        --cluster-analytics-ramsize ${CB_ANALYTICS_RAMSIZE}
fi

sleep 3

# Sometimes failing to load the sample without sleep
sleep 10

echo "cbimport beer-sample..."
/opt/couchbase/bin/cbimport json --format sample --verbose \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} \
 -b beer-sample \
 -d file:///opt/couchbase/samples/beer-sample.zip

echo "drop beer-sample indexes..."
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX beer_primary ON `beer-sample`'

echo "create beer-sample primary index..."
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=CREATE PRIMARY INDEX beer_primary ON `beer-sample`'

echo "couchbase-cli bucket-create travel-sample..."
/opt/couchbase/bin/couchbase-cli bucket-create \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} \
 --bucket travel-sample \
 --bucket-type couchbase \
 --bucket-ramsize ${CB_BUCKET_RAMSIZE} \
 --bucket-replica 0 \
 --bucket-priority low \
 --bucket-eviction-policy fullEviction \
 --enable-flush 1 \
 --enable-index-replica 0 \
 --wait

# Sometimes failing to load the sample without sleep
sleep 10

echo "couchbase-cli bucket-list..."
/opt/couchbase/bin/couchbase-cli bucket-list \
 -c localhost -u ${CB_USER} -p ${CB_PSWD}

echo "cbimport travel-sample..."
/opt/couchbase/bin/cbimport json --format sample --verbose \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} \
 -b travel-sample \
 -d file:///opt/couchbase/samples/travel-sample.zip

echo "drop travel-sample indexes..."
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_airportname ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_city ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_faa ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_icao ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_airline_primary ON `travel-sample`.`inventory`.`airline`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_airport_airportname ON `travel-sample`.`inventory`.`airport`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_airport_city ON `travel-sample`.`inventory`.`airport`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_airport_primary ON `travel-sample`.`inventory`.`airport`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_airport_faa ON `travel-sample`.`inventory`.`airport`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_hotel_city ON `travel-sample`.`inventory`.`hotel`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_hotel_primary ON `travel-sample`.`inventory`.`hotel`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_landmark_city ON `travel-sample`.`inventory`.`landmark`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_landmark_primary ON `travel-sample`.`inventory`.`landmark`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_route_primary ON `travel-sample`.`inventory`.`route`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_route_route_src_dst_day ON `travel-sample`.`inventory`.`route`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_route_schedule_utc ON `travel-sample`.`inventory`.`route`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_inventory_route_sourceairport ON `travel-sample`.`inventory`.`route`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_name_type ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_primary ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_route_src_dst_day ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_schedule_utc ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_sourceairport ON `travel-sample`'
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=DROP INDEX def_type ON `travel-sample`'
    
echo "sleep 10 to allow stabilization..."
sleep 10

echo "create travel-sample primary index..."
curl http://${CB_USER}:${CB_PSWD}@localhost:8093/query/service \
    -d 'statement=CREATE PRIMARY INDEX def_primary ON `travel-sample`'

echo "couchbase-cli bucket-list..."
/opt/couchbase/bin/couchbase-cli bucket-list \
 -c localhost -u ${CB_USER} -p ${CB_PSWD}

echo "couchbase-cli bucket-edit beer-sample..."
/opt/couchbase/bin/couchbase-cli bucket-edit \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} \
 --bucket beer-sample \
 --bucket-replica 0

echo "couchbase-cli bucket-edit travel-sample..."
/opt/couchbase/bin/couchbase-cli bucket-edit \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} \
 --bucket travel-sample \
 --bucket-replica 0

echo "couchbase-cli bucket-list..."
/opt/couchbase/bin/couchbase-cli bucket-list \
 -c localhost -u ${CB_USER} -p ${CB_PSWD}

echo "couchbase-cli rebalance..."
/opt/couchbase/bin/couchbase-cli rebalance \
 -c localhost -u ${CB_USER} -p ${CB_PSWD} --no-progress-bar

echo "sleep 40 to allow stabilization..."
sleep 40