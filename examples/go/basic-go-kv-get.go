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

  collection := bucket.DefaultCollection()

  result, err := collection.Get("airline_10", nil)
  if err != nil {
    log.Fatal(err)
  }

  type Doc struct {
    Id int `json:"id"`
    Type string `json:"type"`
    Name string `json:"name"`
    Iata string `json:"iata"`
    Icao string `json:"icao"`
    Callsign string `json:"callsign"`
    Country string `json:"country"`
  }

  var document Doc
  err = result.Content(&document)
  if err != nil {
    log.Fatal(err)
  }

  prettyJSON, err := json.MarshalIndent(document, "", "    ")
  if err != nil {
      log.Fatal("Failed to generate json", err)
  }

  fmt.Printf("%s\n", string(prettyJSON))
}
