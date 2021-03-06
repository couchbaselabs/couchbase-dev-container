/*
File: basic-go-query-rows.go 
Description: Query Rows

Basic N1QL query,
with looping through each returned row.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/go-sdk/current/howtos/n1ql-queries-with-sdk.html#handling-results">Handling Query Results in Go</a>.
*/


package main

import (
  "fmt"
  "time"
  "log"
  "encoding/json"

  "github.com/couchbase/gocb/v2"
)

func main() {
  cluster, err := gocb.Connect("couchbase://127.0.0.1", gocb.ClusterOptions{
    Authenticator: gocb.PasswordAuthenticator{
      Username: "Administrator",
      Password: "password",
    },
  })
  if err != nil {
    log.Fatal(err)
  }

  bucket := cluster.Bucket("travel-sample")
  err = bucket.WaitUntilReady(5*time.Second, nil)
  if err != nil {
    log.Fatal(err)
  }
  
  results, err := cluster.Query(
    "SELECT x.* FROM `travel-sample` x " +
    "WHERE x.`type`=\"hotel\" AND x.name LIKE \"%hotel%\" LIMIT 10", nil)
  if err != nil {
    log.Fatal(err)
  }

  for results.Next() {
    var content interface{}
    err := results.Row(&content)
    if err != nil {
      log.Fatal(err)
    }

    prettyJSON, err := json.MarshalIndent(content, "", "    ")
    if err != nil {
        log.Fatal("Failed to generate json", err)
    }

    fmt.Printf("%s\n", string(prettyJSON))
  }

  err = results.Err()
  if err != nil {
    log.Fatal(err)
  }
}
