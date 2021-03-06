/*
File: basic-php-upsert.php 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/php-sdk/current/howtos/kv-operations.html">Key Value Operations in PHP</a>.
*/

<?php
  $connectionString = "couchbase://127.0.0.1";
  $options = new \Couchbase\ClusterOptions();
  $options->credentials("Administrator", "password");
  $cluster = new \Couchbase\Cluster($connectionString, $options);

  $bucket = $cluster->bucket("travel-sample");
  $collection = $bucket->defaultCollection();

  $content = ["country" => "Iceland",
              "callsign" => "ICEAIR",
              "iata" => "FI",
              "icao" => "ICE",
              "id" => 123,
              "name" => "Icelandair",
              "type" => "airline"];

  $collection->upsert("airline_123", $content);
  try {
    $result = $collection->lookupIn("airline_123", [new \Couchbase\LookupGetSpec("name")]);
    $name = $result->content(0);
    print("New Document name = $name");
  } catch (\Couchbase\PathNotFoundException $pnfe) {
    print("Sub-doc path not found!");
  } catch (\Couchbase\BaseException $ex) {
    print("Exception $ex\n");
  }
?>
