#!/bin/sh
eval $(docker-machine env mysql)
docker-compose stop
docker-compose rm -f
