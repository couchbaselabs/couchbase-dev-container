#!/usr/bin/python3
"""
File: basic-py-query-rows.py 
Description: Query Rows

Basic N1QL query,
with looping through each returned row.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/python-sdk/current/howtos/n1ql-queries-with-sdk.html#handling-results">Handling Query Results in Python</a>.

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
  WHERE h.type = 'hotel'
    AND h.city = 'Malibu' LIMIT 5
"""

try:
  result = cluster.query(query)

  for row in result:
    # each row is an instance of the query call
    name = row['name']
    age = row['city']
    print("hotel: ", name + age)

except:
  print("exception:", sys.exc_info()[0])
