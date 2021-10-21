#!/usr/bin/python3
"""
File: basic-py-subdoc-lookup.py 
Description: Sub-document Lookup

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/python-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Python</a>.

"""



import sys
import couchbase.collection
import couchbase.subdocument as SD

from couchbase.durability import Durability
from couchbase.cluster import Cluster, ClusterOptions
from couchbase_core.cluster import PasswordAuthenticator

pa = PasswordAuthenticator('Administrator', 'password')
cluster = Cluster('couchbase://127.0.0.1', ClusterOptions(pa))
bucket = cluster.bucket('travel-sample')
collection = bucket.default_collection()

try:
  result = collection.lookup_in('airline_10', [SD.get('country')])
  country = result.content_as[str](0) # "United Kingdom"
  print("country:", country)

except:
  print("exception:", sys.exc_info()[0])
