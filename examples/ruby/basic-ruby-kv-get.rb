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
