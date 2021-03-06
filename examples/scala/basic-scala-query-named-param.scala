/*
File: basic-scala-query-named-param.scala 
Description: Query w/ Named Param

This example shows how to use named parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/scala-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Scala</a>.
*/


package com.couchbase

import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
import com.couchbase.client.scala.Cluster
import com.couchbase.client.scala.query.{QueryResult, QueryOptions, QueryParameters}
import scala.util.{Failure, Success, Try}
import com.couchbase.client.scala.json.{JsonObject, JsonObjectSafe}

object Program extends App {
  val cluster = Cluster.connect("127.0.0.1", "Administrator", "password").get
  var bucket = cluster.bucket("travel-sample");
  val collection = bucket.defaultCollection

  val options = QueryOptions().parameters(
                    QueryParameters.Named(Map("type" -> "hotel"))
                  )
  val result: Try[QueryResult] = cluster.query("""SELECT x.* FROM `travel-sample` x WHERE x.`type`=$type LIMIT 10;""", options)
  result match {
    case Success(result: QueryResult) =>
      result.rowsAs[JsonObject] match {
        case Success(rows) =>
          rows.foreach(row => println(row))
        case Failure(err) => println("Error decoding result: " + err)
      }
    case Failure(err: CouchbaseException) => println("Couchbase error: " + err)
    case Failure(err) => println("Error: " + err)
  }
}
