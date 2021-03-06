/*
File: basic-go-upsert.go 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/go-sdk/current/howtos/kv-operations.html">Key Value Operations in Go</a>.
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

  type Doc struct {
    Id int `json:"id"`
    Type string `json:"type"`
    Name string `json:"name"`
    Iata string `json:"iata"`
    Icao string `json:"icao"`
    Callsign string `json:"callsign"`
    Country string `json:"country"`
  }

  document := Doc {
    Country: "Iceland",
    Callsign: "ICEAIR",
    Iata: "FI",
    Icao: "ICE",
    Id: 123,
    Name: "Icelandair",
    Type: "airline",
  }

  _, err = collection.Upsert("airline_123", &document, nil)
  if err != nil {
    log.Fatal(err)
  }

  options := []gocb.LookupInSpec{
    gocb.GetSpec("name", &gocb.GetSpecOptions{}),
  }
  results, err := collection.LookupIn("airline_123", options, &gocb.LookupInOptions{})
  if err != nil {
    log.Fatal(err)
  }

  var country string
  err = results.ContentAt(0, &country)
  if err != nil {
    log.Fatal(err)
  }

  fmt.Printf("New document name: %s\n", country)
}
