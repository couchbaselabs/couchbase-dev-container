const couchbase = require('couchbase')

const cluster = new couchbase.Cluster('couchbase://127.0.0.1', {
  username: 'Administrator', password: 'password'
})

const bucket = cluster.bucket('travel-sample')
const collection = bucket.defaultCollection()

const getDocument = async (key) => {
  try {
    const result = await collection.get(key)
    console.log(result)
  } catch (err) {
    console.error(err)
  }
}

getDocument('airline_10')
  .then(() => process.exit(0))
