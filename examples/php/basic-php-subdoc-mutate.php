/*
File: basic-php-subdoc-mutate.php 
Description: Sub-document Mutate

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
    $result = $collection->lookupIn("airline_10", [new \Couchbase\LookupGetSpec("country")]);
    $country = $result->content(0);
    print("Sub-doc before: $country");
  } catch (\Couchbase\PathNotFoundException $pnfe) {
    print("Sub-doc path not found!");
  } catch (\Couchbase\BaseException $ex) {
    print("Exception $ex\n");
  }

  try {
    $result = $collection->mutateIn("airline_10", [new \Couchbase\MutateUpsertSpec("country", "Canada")]);
  } catch (\Couchbase\PathExistsException $pex) {
      print("Sub-doc path exists!");
  } catch (\Couchbase\BaseException $ex) {
    print("Exception $ex\n");
  }

  try {
    $result = $collection->lookupIn("airline_10", [new \Couchbase\LookupGetSpec("country")]);
    $country = $result->content(0);
    print("\nSub-doc after: $country");
  } catch (\Couchbase\PathNotFoundException $pnfe) {
    print("Sub-doc path not found!");
  } catch (\Couchbase\BaseException $ex) {
    print("Exception $ex\n");
  }
?>
