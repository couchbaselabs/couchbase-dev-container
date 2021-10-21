/*
File: basic-php-query-positional-param.php 
Description: Query w/ Positional Param

This example shows how to use positional parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/php-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with PHP</a>.
*/

<?php
  $connectionString = "couchbase://127.0.0.1";
  $options = new \Couchbase\ClusterOptions();
  $options->credentials("Administrator", "password");
  $cluster = new \Couchbase\Cluster($connectionString, $options);

  $bucket = $cluster->bucket("travel-sample");
  
  $options = new \Couchbase\QueryOptions();
  $options->positionalParameters(["hotel"]);
  try {
    $result = $cluster->query('SELECT x.* FROM `travel-sample` x 
                              WHERE x.`type`=$1 LIMIT 10;', $options);

    foreach($result->rows() as $row) {
        print_r($row);
    }
  } catch (\Couchbase\BaseException $ex) {
      print("Exception $ex\n");
  }
?>
