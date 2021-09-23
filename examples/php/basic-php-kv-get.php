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
