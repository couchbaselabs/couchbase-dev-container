-begin
File: basic-rb-query-rows.rb 
Description: Query Rows

Basic N1QL query,
with looping through each returned row.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/ruby-sdk/current/howtos/n1ql-queries-with-sdk.html#handling-results">Handling Query Results in Ruby</a>.
-end


require "couchbase"
include Couchbase

options = Cluster::ClusterOptions.new
options.authenticate("Administrator", "password")
cluster = Cluster.connect("couchbase://127.0.0.1", options)

bucket = cluster.bucket("travel-sample")
collection = bucket.default_collection

begin
  result = cluster.query('SELECT x.* FROM `travel-sample` x '\
                        'WHERE x.`type`="hotel" AND x.name LIKE "%hotel%" LIMIT 10')
  result.rows.each do |row|
    puts row
  end
rescue Couchbase::Error::CouchbaseError => ex
  puts "Couchbase Error: #{ex}"
end
