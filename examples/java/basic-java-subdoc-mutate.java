/*
File: basic-java-subdoc-mutate.java 
Description: Sub-document Mutate

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Java</a>.
*/

import com.couchbase.client.core.error.subdoc.PathNotFoundException;
import com.couchbase.client.core.error.subdoc.PathExistsException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.LookupInResult;
import static com.couchbase.client.java.kv.LookupInSpec.get;
import static com.couchbase.client.java.kv.MutateInSpec.upsert;
import java.util.Collections;
import java.util.Arrays;

class basic-java-subdoc-mutate {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");
    var collection = bucket.defaultCollection();

    try {
      LookupInResult result = collection.lookupIn(
        "airline_10", Collections.singletonList(get("country"))
      );

      var str = result.contentAs(0, String.class);
      System.out.println("Sub-doc before: ");
      System.out.println(str);
      
    } catch (PathNotFoundException e) {
      System.out.println("Sub-doc path not found!");
    }

    try {
      collection.mutateIn("airline_10", Arrays.asList(
        upsert("country", "Canada")
      ));
    } catch (PathExistsException e) {
      System.out.println("Sub-doc path exists!");
    }

    try {
      LookupInResult result = collection.lookupIn(
        "airline_10", Collections.singletonList(get("country"))
      );

      var str = result.contentAs(0, String.class);
      System.out.println("Sub-doc after: ");
      System.out.println(str);
      
    } catch (PathNotFoundException e) {
      System.out.println("Sub-doc path not found!");
    }
    
  }
}
