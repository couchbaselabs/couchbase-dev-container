-begin
File: basic-rb-upsert.rb 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/ruby-sdk/current/howtos/kv-operations.html">Key Value Operations in Ruby</a>.
-end


require "couchbase"
include Couchbase

options = Cluster::ClusterOptions.new
options.authenticate("Administrator", "password")
cluster = Cluster.connect("couchbase://127.0.0.1", options)

bucket = cluster.bucket("travel-sample")
collection = bucket.default_collection

begin
  content = {"country" => "Iceland",
             "callsign" => "ICEAIR",
             "iata" => "FI",
             "icao" => "ICE",
             "id" => 123,
             "name" => "Icelandair",
             "type" => "airline"}
  collection.upsert("airline_123", content)
  result = collection.lookup_in("airline_123", [ LookupInSpec.get("name")])
  puts "New Document name: #{result.content(0)}"
rescue Couchbase::Error::PathNotFound => pnfe
  puts "Sub-doc path not found!"
rescue Couchbase::Error::DocumentNotFound => ex
  puts "Document not found!"
end
