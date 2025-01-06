Little scala app made to extract the saved tracks of an spotify account 

# How to use

> Main dependencies: ZIO, zio-schema, zio-json, zio-http

> Requirements: Sbt and Scala 3

```bash
sbt compile
sbt run > output.txt
```
It is also required to update the application.conf inside of the resources directory

```hocon
spotify {
    token = "{PUT YOUR TOKEN HERE, obtain it from spotify OAuth flow}"
    getTokenEndpoint = "https://accounts.spotify.com/api/token"
    getUserSavedTracksEndpoint = "https://api.spotify.com/v1/me/tracks"
}
separator = "\n"
```

# Structure

- /model: Contains the spotify model that is going to be saved
- /service/httpclient: Http client used to interact with the spotify api
- /service/filesystem: Filesystem related stuff
- /usecase: Logic
- /util: Codecs and such
