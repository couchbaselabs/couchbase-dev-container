/*
File: basic-java-query-positional-param.java 
Description: Query w/ Positional Param

This example shows how to use positional parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Java</a>.
*/

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryOptions;
import static com.couchbase.client.java.query.QueryOptions.queryOptions;

class basic-java-query-positional-param {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");

    try {
      var query = 
        "SELECT h.name, h.city, h.state " +
        "FROM `travel-sample` h " +
        "WHERE h.type = $1 " +
          "AND h.city = $2 LIMIT 5;";
          
      QueryResult result = cluster.query(query,
        queryOptions().parameters(JsonArray.from("hotel", "Malibu"))
      );
      result.rowsAsObject().stream().forEach(
        e-> System.out.println(
          "Hotel: " + e.getString("name") + ", " + e.getString("city"))
      );
      
    } catch (CouchbaseException ex) {
      System.out.println("Exception: " + ex.toString());
    }
  }
}
