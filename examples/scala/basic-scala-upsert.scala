/*
File: basic-scala-upsert.scala 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/scala-sdk/current/howtos/kv-operations.html">Key Value Operations in Scala</a>.
*/


package com.couchbase

import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
import com.couchbase.client.core.error.subdoc.{PathExistsException,PathNotFoundException}
import com.couchbase.client.scala.Cluster
import com.couchbase.client.scala.json.{JsonObject, JsonObjectSafe}
import com.couchbase.client.scala.kv.LookupInSpec._
import com.couchbase.client.scala.kv.{LookupInResult, _}
import scala.util.{Failure, Success, Try}

object Program extends App {
  val cluster = Cluster.connect("127.0.0.1", "Administrator", "password").get
  var bucket = cluster.bucket("travel-sample");
  val collection = bucket.defaultCollection

  val content = JsonObject("country" -> "Iceland",
                "callsign"-> "ICEAIR",
                "iata" -> "FI",
                "icao" -> "ICE",
                "id" -> 123,
                "name" -> "Icelandair",
                "type" -> "airline")

  collection.upsert("airline_123", content) match {
    case Success(result)    =>
    case Failure(exception) => println("Error: " + exception)
  }

  val result = collection.lookupIn("airline_123", Array(get("name")))
  result match {
    case Success(r) =>
      val str: Try[String] = r.contentAs[String](0)
      str match {
        case Success(s)   => println(s"New document name: ${s}")
        case Failure(err) => println(s"Error: ${err}")
      }
    case Failure(err: DocumentNotFoundException) => println("Document not found")
    case Failure(err: PathNotFoundException) => println("Sub-doc path not found!")
    case Failure(err: CouchbaseException) => println("Couchbase error: " + err)
    case Failure(err) => println("Error getting document: " + err)
  }
}
