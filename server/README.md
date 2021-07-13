# Build and Run Locally in Docker
```
mvn package
docker build . -t wssrv
docker run -it -p 8080:8080 --name wssrv --rm wssrv
```