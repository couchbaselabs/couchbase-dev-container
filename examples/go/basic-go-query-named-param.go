/*
File: basic-go-query-named-param.go 
Description: Query w/ Named Param

This example shows how to use named parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/go-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with Go</a>.
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

  params := make(map[string]interface{}, 1)
  params["type"] = "hotel"
  results, err := cluster.Query(
    "SELECT x.* FROM `travel-sample` x WHERE x.`type`=$type LIMIT 10;",
    &gocb.QueryOptions{NamedParameters: params})
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
