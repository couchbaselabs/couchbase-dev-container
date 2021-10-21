/*
File: basic-java-query-rows.java 
Description: Query Rows

Basic N1QL query,
with looping through each returned row.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html#the-query-result">Handling Query Results in Java</a>.
*/

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;

class basic-java-query-rows {
  public static void main(String[] args) {
    var cluster = Cluster.connect(
      "couchbase://127.0.0.1", "Administrator", "password"
    );
    var bucket = cluster.bucket("travel-sample");

    try {
      var query = 
        "SELECT h.name, h.city, h.state " +
        "FROM `travel-sample` h " +
        "WHERE h.type = 'hotel' " +
          "AND h.city = 'Malibu' LIMIT 5;";

      QueryResult result = cluster.query(query);
      for (JsonObject row : result.rowsAsObject()) {
        System.out.println("Hotel: " + row);
      }
      
    } catch (DocumentNotFoundException ex) {
      System.out.println("Document not found!");
    }
  }
}
