-begin
File: basic-rb-subdoc-mutate.rb 
Description: Sub-document Mutate

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/ruby-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Ruby</a>.
-end

require "couchbase"
include Couchbase

options = Cluster::ClusterOptions.new
options.authenticate("Administrator", "password")
cluster = Cluster.connect("couchbase://127.0.0.1", options)

bucket = cluster.bucket("travel-sample")
collection = bucket.default_collection

begin
  result = collection.lookup_in("airline_10", [ LookupInSpec.get("country")])
  puts "Sub-doc before: #{result.content(0)}"
rescue Couchbase::Error::PathNotFound => pnfe
  puts "Sub-doc path not found!"
rescue Couchbase::Error::CouchbaseError => ex
  puts "Couchbase Error: #{ex}"
end

begin
  result = collection.mutate_in("airline_10", [ MutateInSpec.upsert("country", "Canada")])
rescue Couchbase::Error::PathExists => pnfe
  puts "Sub-doc path exists!"
rescue Couchbase::Error::CouchbaseError => ex
  puts "Couchbase Error: #{ex}"
end

begin
  result = collection.lookup_in("airline_10", [ LookupInSpec.get("country")])
  puts "Sub-doc after: #{result.content(0)}"
rescue Couchbase::Error::PathNotFound => pnfe
  puts "Sub-doc path not found!"
rescue Couchbase::Error::CouchbaseError => ex
  puts "Couchbase Error: #{ex}"
end
