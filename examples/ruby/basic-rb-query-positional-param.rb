-begin
File: basic-rb-query-positional-param.rb 
Description: Query w/ Positional Param

This example shows how to use positional parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/ruby-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Ruby</a>.
-end


require "couchbase"
include Couchbase

options = Cluster::ClusterOptions.new
options.authenticate("Administrator", "password")
cluster = Cluster.connect("couchbase://127.0.0.1", options)

bucket = cluster.bucket("travel-sample")

begin
  options = Cluster::QueryOptions.new
  options.positional_parameters(["hotel"])
  result = cluster.query('SELECT x.* FROM `travel-sample` x WHERE x.`type`=$1 LIMIT 10;', options)
  result.rows.each do |row|
    puts row
  end
rescue Couchbase::Error::CouchbaseError => ex
  puts "Couchbase Error: #{ex}"
end
