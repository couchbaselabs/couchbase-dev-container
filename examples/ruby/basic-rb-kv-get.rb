-begin
File: basic-rb-kv-get.rb 
Description: Key Value Get

Given a document's key, you can use the collection.get() method to retrieve a document from a collection.
<br/><br/>
See more at the SDK documentation on
<a target="_blank" href="https://docs.couchbase.com/ruby-sdk/current/howtos/kv-operations.html">Ruby Key Value Operations</a>.
-end


require "couchbase"
include Couchbase

options = Cluster::ClusterOptions.new
options.authenticate("Administrator", "password")
cluster = Cluster.connect("couchbase://127.0.0.1", options)

bucket = cluster.bucket("travel-sample")
collection = bucket.default_collection

begin
  get_result = collection.get("airline_10")
  puts get_result.content
rescue Couchbase::Error::DocumentNotFound => ex
  puts "Document not found!"
end
