/*
File: basic-java-kv-get.java 
Description: Key Value Get

Given a document's key, you can use the collection.get() method to retrieve a document from a collection.
<br/><br/>
See more at the SDK documentation on
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/kv-operations.html">Java Key Value Operations</a>.
*/

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;

class basic-java-kv-get {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );

    var bucket = cluster.bucket("travel-sample");
    var collection = bucket.defaultCollection();

    try {
      var result = collection.get("airline_10");
      System.out.println(result.toString());

    } catch (DocumentNotFoundException ex) {
      System.out.println("Document not found!");
    }
  }
}
