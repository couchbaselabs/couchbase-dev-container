#!/usr/bin/python3
"""
File: basic-py-query-named-param.py 
Description: Query w/ Named Param

This example shows how to use named parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/python-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Python</a>.

"""



import sys

from couchbase.cluster import Cluster, ClusterOptions
from couchbase_core.cluster import PasswordAuthenticator

pa = PasswordAuthenticator('Administrator', 'password')

cluster = Cluster('couchbase://127.0.0.1', ClusterOptions(pa))
bucket = cluster.bucket('travel-sample')

query = """
  SELECT h.name, h.city, h.state 
  FROM `travel-sample` h 
  WHERE h.type = $type 
    AND h.city = $city LIMIT 5
"""

try:
  result = cluster.query(query, type='hotel', city='Malibu')

  for row in result:
    name = row['name']
    city = row['city']
    print("Hotel: " + name + ", " + city)

except:
  print("exception:", sys.exc_info()[0])
