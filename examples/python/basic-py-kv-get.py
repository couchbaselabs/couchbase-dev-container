#!/usr/bin/python3
"""
File: basic-py-kv-get.py 
Description: Key Value Get

Given a document's key, you can use the collection.get() method to retrieve a document from a collection.
<br/><br/>
See more at the SDK documentation on
<a target="_blank" href="https://docs.couchbase.com/python-sdk/current/howtos/kv-operations.html">Python Key Value Operations</a>.

"""



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
