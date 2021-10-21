/*
File: basic-java-query-named-param.java 
Description: Query w/ Named Param

This example shows how to use named parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Java</a>.
*/

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryOptions;
import static com.couchbase.client.java.query.QueryOptions.queryOptions;

class basic-java-query-named-param {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");

    try {
      var query = 
        "SELECT h.name, h.city, h.state " +
        "FROM `travel-sample` h " +
        "WHERE h.type = $type " +
          "AND h.city = $city LIMIT 5;";

      QueryResult result = cluster.query(query,
        queryOptions().parameters(
          JsonObject.create()
            .put("type", "hotel")
            .put("city", "Malibu")
        ));
      result.rowsAsObject().stream().forEach(
        e-> System.out.println(
          "Hotel: "+e.getString("name")+", "+e.getString("city"))
      );

    } catch (CouchbaseException ex) {
      System.out.println("Exception: " + ex.toString());
    }
  }
}
