using System;
using System.Threading.Tasks;
using Couchbase;

namespace CouchbaseDotNetExample
{
  class Program
  {
    static async Task Main(string[] args)
    {
      var cluster = await Cluster.ConnectAsync(
        "couchbase://127.0.0.1", "Administrator", "password"
      );

      var bucket = await cluster.BucketAsync("travel-sample");
      var scope = await bucket.ScopeAsync("_default");
      var collection = await scope.CollectionAsync("_default");

      var result = await collection.GetAsync("airline_10");
      var airline = result.ContentAs<dynamic>();

      Console.WriteLine(airline);

      await cluster.DisposeAsync();
    }
  }
}
