/*
File: basic-scala-kv-get.scala 
Description: Key Value Get

Given a document's key, you can use the collection.get() method to retrieve a document from a collection.
<br/><br/>
See more at the SDK documentation on
<a target="_blank" href="https://docs.couchbase.com/scala-sdk/current/howtos/kv-operations.html">Scala Key Value Operations</a>.
*/


package com.couchbase

import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
import com.couchbase.client.scala.Cluster
import scala.util.{Failure, Success, Try}
import com.couchbase.client.scala.json.{JsonObject, JsonObjectSafe}

object Program extends App {
  val cluster = Cluster.connect("127.0.0.1", "Administrator", "password").get
  var bucket = cluster.bucket("travel-sample");
  val collection = bucket.defaultCollection

  collection.get("airline_10") match {
    case Success(result) =>
      result.contentAs[JsonObjectSafe] match {
          case Success(json) => println(json)
          case Failure(err) => println("Error decoding result: " + err)
      }
    case Failure(err: DocumentNotFoundException) => println("Document not found")
    case Failure(err: CouchbaseException) => println("Couchbase error: " + err)
    case Failure(err) => println("Error getting document: " + err)
  }
}
