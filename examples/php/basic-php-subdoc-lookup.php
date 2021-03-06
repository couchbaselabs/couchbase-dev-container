/*
File: basic-php-subdoc-lookup.php 
Description: Sub-document Lookup

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/php-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in PHP</a>.
*/

<?php
  $connectionString = "couchbase://127.0.0.1";
  $options = new \Couchbase\ClusterOptions();
  $options->credentials("Administrator", "password");
  $cluster = new \Couchbase\Cluster($connectionString, $options);

  $bucket = $cluster->bucket("travel-sample");
  $collection = $bucket->defaultCollection();

  try {
    $result = $collection->lookupIn("airport_1254", [new \Couchbase\LookupGetSpec("geo.alt")]);
    $alt = $result->content(0);
    print("Altitude = $alt");
  } catch (\Couchbase\PathNotFoundException $pnfe) {
    print("Sub-doc path not found!");
  } catch (\Couchbase\BaseException $ex) {
    print("Exception $ex\n");
  }
?>
