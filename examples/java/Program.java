import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;

public class Program {
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
