#!/usr/bin/env bash
docker stop redis
docker rm redis
docker run --name some-redis -p 0.0.0.0:6379:6379 -d redis
./gradlew run