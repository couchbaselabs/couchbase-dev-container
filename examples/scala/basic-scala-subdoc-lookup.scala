/*
File: basic-scala-subdoc-lookup.scala 
Description: Sub-document Lookup

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/scala-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Scala</a>.
*/


package com.couchbase

import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
import com.couchbase.client.core.error.subdoc.{PathExistsException,PathNotFoundException}
import com.couchbase.client.scala.Cluster
import com.couchbase.client.scala.json.{JsonObject, JsonObjectSafe}
import com.couchbase.client.scala.kv.LookupInSpec._
import com.couchbase.client.scala.kv.MutateInSpec._
import com.couchbase.client.scala.kv.{LookupInResult, _}
import scala.util.{Failure, Success, Try}

object Program extends App {
  val cluster = Cluster.connect("127.0.0.1", "Administrator", "password").get
  var bucket = cluster.bucket("travel-sample");
  val collection = bucket.defaultCollection

  val result = collection.lookupIn("airport_1254", Array(get("geo.alt")))
  result match {
    case Success(r) =>
      val str: Try[String] = r.contentAs[String](0)
      str match {
        case Success(s)   => println(s"Altitude = ${s}")
        case Failure(err) => println(s"Error: ${err}")
      }
    case Failure(err: PathNotFoundException) => println("Sub-doc path not found!")
    case Failure(err: DocumentNotFoundException) => println("Document not found!")
    case Failure(err: CouchbaseException) => println("Couchbase error: " + err)
    case Failure(err) => println("Error: " + err)
  }
}
