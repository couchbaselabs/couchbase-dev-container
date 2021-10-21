#!/bin/sh
#File: basic-n1ql.sh 
#Description: N1QL Query Using curl

#The above example shows a primary key lookup using N1QL through the query service's REST API via curl.
#<br/><br/>
#Visit our docs to learn more about <a target="_blank" href="https://docs.couchbase.com/server/current/n1ql/n1ql-rest-api/index.html">N1QL REST API</a>.

curl http://Administrator:password@127.0.0.1:8093/query/service \
  -d 'statement=
    SELECT a.name, a.callsign, a.country
    FROM `travel-sample` a
    WHERE meta().id = "airline_10"'
