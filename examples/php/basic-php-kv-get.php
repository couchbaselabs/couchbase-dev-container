/*
File: basic-php-kv-get.php 
Description: Key Value Get

Given a document's key, you can use the collection.get() method to retrieve a document from a collection.
<br/><br/>
See more at the SDK documentation on
<a target="_blank" href="https://docs.couchbase.com/php-sdk/current/howtos/kv-operations.html">PHP Key Value Operations</a>.
*/

<?php
  $connectionString = "couchbase://127.0.0.1";
  $options = new \Couchbase\ClusterOptions();
  $options->credentials("Administrator", "password");
  $cluster = new \Couchbase\Cluster($connectionString, $options);

  $bucket = $cluster->bucket("travel-sample");
  $collection = $bucket->defaultCollection();

  try {
    $result = $collection->get("airline_10");
    print_r($result->content());
  } catch (\Couchbase\DocumentNotFoundException $ex) {
      print("Document does not exist\n");
  }
?>
