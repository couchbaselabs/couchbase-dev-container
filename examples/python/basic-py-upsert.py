#!/usr/bin/python3
"""
File: basic-py-upsert.py 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/python-sdk/current/howtos/kv-operations.html">Key Value Operations in .Python</a>.

"""


import sys
import couchbase.collection
import couchbase.subdocument as SD
from couchbase.cluster import Cluster, ClusterOptions
from couchbase_core.cluster import PasswordAuthenticator
from couchbase.durability import ServerDurability, Durability
from datetime import timedelta

pa = PasswordAuthenticator('Administrator', 'password')
cluster = Cluster('couchbase://127.0.0.1', ClusterOptions(pa))
bucket = cluster.bucket('travel-sample')
collection = bucket.default_collection()

try:
  document = dict(
    country="Iceland", callsign="ICEAIR", iata="FI", icao="ICE",
    id=123, name="Icelandair", type="airline"
  )
  result = collection.upsert(
    'airline_123',
    document,
    expiry=timedelta(minutes=1),
    durability=ServerDurability(Durability.MAJORITY)
  )
  print("UPSERT SUCCESS")
  print("cas result:", result.cas)
except:
  print("exception:", sys.exc_info())

try:
  result = collection.lookup_in('airline_123', [SD.get('name')])
  name = result.content_as[str](0) # "United Kingdom"
  print("name:", name)

except:
  print("exception:", sys.exc_info()[0])
