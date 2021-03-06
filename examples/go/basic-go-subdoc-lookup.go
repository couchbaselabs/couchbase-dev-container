/*
File: basic-go-subdoc-lookup.go 
Description: Sub-document Lookup

The Sub-document API allows for retrieving or mutating a portion of
a larger document, without having to first retrieve the entire document,
which can provide for higher performance.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/go-sdk/current/howtos/subdocument-operations.html">Sub-document Operations in Go</a>.
*/


package main

import (
  "fmt"
  "log"
  "time"

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

  collection := bucket.DefaultCollection()

  options := []gocb.LookupInSpec{
    gocb.GetSpec("geo.alt", &gocb.GetSpecOptions{}),
  }
  results, err := collection.LookupIn("airport_1254", options, &gocb.LookupInOptions{})
  if err != nil {
    log.Fatal(err)
  }

  var alt float64
  err = results.ContentAt(0, &alt)
  if err != nil {
    log.Fatal(err)
  }

  fmt.Printf("Altitude = %g", alt)
}
