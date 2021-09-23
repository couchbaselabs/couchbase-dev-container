#!/usr/bin/python3

import sys

from couchbase.cluster import Cluster, ClusterOptions
from couchbase_core.cluster import PasswordAuthenticator

pa = PasswordAuthenticator('Administrator', 'password')
cluster = Cluster('couchbase://127.0.0.1', ClusterOptions(pa))

bucket = cluster.bucket('travel-sample')
collection = bucket.default_collection()

try:
  result = collection.get('airline_10')
  print(result.content)

except:
  print("exception:", sys.exc_info()[0])
