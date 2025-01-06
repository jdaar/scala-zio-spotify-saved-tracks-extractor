Little scala app made to extract the saved tracks of an spotify account 

# How to use

> Main dependencies: ZIO, zio-schema, zio-json, zio-http

> Requirements: Sbt and Scala 3

```bash
sbt compile
sbt run > output.txt
```

# Structure

- /model: Contains the spotify model that is going to be saved
- /service/httpclient: Http client used to interact with the spotify api
- /service/filesystem: Filesystem related stuff
- /usecase: Logic
- /util: Codecs and such
