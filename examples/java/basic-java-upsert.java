/*
File: basic-java-upsert.java 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/kv-operations.html">Key Value Operations in .Java</a>.
*/

import com.couchbase.client.core.error.subdoc.PathNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.LookupInResult;
import static com.couchbase.client.java.kv.LookupInSpec.get;
import static com.couchbase.client.java.kv.MutateInSpec.upsert;
import java.util.Collections;

class basic-java-upsert {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");
    var collection = bucket.defaultCollection();

    JsonObject content = JsonObject.create()
      .put("country", "Iceland")
      .put("callsign", "ICEAIR")
      .put("iata", "FI")
      .put("icao", "ICE")
      .put("id", 123)
      .put("name", "Icelandair")
      .put("type", "airline");

    collection.upsert("airline_123", content);

    try {
      LookupInResult lookupResult = collection.lookupIn(
        "airline_123", Collections.singletonList(get("name"))
      );

      var str = lookupResult.contentAs(0, String.class);
      System.out.println("New Document name = " + str);

    } catch (PathNotFoundException ex) {
      System.out.println("Document not found!");
    }

  }
}
