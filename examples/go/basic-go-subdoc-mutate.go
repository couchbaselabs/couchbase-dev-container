/*
File: basic-go-subdoc-mutate.go 
Description: Sub-document Mutate

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
    gocb.GetSpec("country", &gocb.GetSpecOptions{}),
  }
  results, err := collection.LookupIn("airline_10", options, &gocb.LookupInOptions{})
  if err != nil {
    log.Fatal(err)
  }

  var country string
  err = results.ContentAt(0, &country)
  if err != nil {
    log.Fatal(err)
  }

  fmt.Printf("Sub-doc before: %s\n", country)

  mops := []gocb.MutateInSpec{
    gocb.UpsertSpec("country", "Canada", &gocb.UpsertSpecOptions{}),
  }
  _, merr := collection.MutateIn("airline_10", mops,
    &gocb.MutateInOptions{Timeout: 10050 * time.Millisecond})
  if merr != nil {
    log.Fatal(merr)
  }

  options = []gocb.LookupInSpec{
    gocb.GetSpec("country", &gocb.GetSpecOptions{}),
  }
  results, err = collection.LookupIn("airline_10", options, &gocb.LookupInOptions{})
  if err != nil {
    log.Fatal(err)
  }

  err = results.ContentAt(0, &country)
  if err != nil {
    log.Fatal(err)
  }

  fmt.Printf("Sub-doc after: %s\n", country)
}
