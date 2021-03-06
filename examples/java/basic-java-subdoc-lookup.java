/*
File: basic-java-subdoc-lookup.java 
Description: Sub-document Lookup

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Java</a>.
*/

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.LookupInResult;
import static com.couchbase.client.java.kv.LookupInSpec.get;
import java.util.Collections;

class basic-java-subdoc-lookup {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");
    var collection = bucket.defaultCollection();

    try {
      LookupInResult result = collection.lookupIn(
        "airport_1254",
        Collections.singletonList(get("geo.alt"))
      );

      var str = result.contentAs(0, String.class);
      System.out.println("Altitude = " + str);
      
    } catch (DocumentNotFoundException ex) {
      System.out.println("Document not found!");
    }
  }
}
