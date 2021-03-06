/*
File: basic-php-query-rows.php 
Description: Query Rows

Basic N1QL query,
with looping through each returned row.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/php-sdk/current/howtos/n1ql-queries-with-sdk.html#handling-results">Handling Query Results in PHP</a>.
*/

<?php
  $connectionString = "couchbase://127.0.0.1";
  $options = new \Couchbase\ClusterOptions();
  $options->credentials("Administrator", "password");
  $cluster = new \Couchbase\Cluster($connectionString, $options);

  $bucket = $cluster->bucket("travel-sample");
  try {
    $result = $cluster->query('SELECT x.* FROM `travel-sample` x 
                              WHERE x.`type`="hotel" AND x.name LIKE "%hotel%" LIMIT 10');
    
    foreach($result->rows() as $row) {
        print_r($row);
    }
  } catch (\Couchbase\BaseException $ex) {
      print("Exception $ex\n");
  }
?>
