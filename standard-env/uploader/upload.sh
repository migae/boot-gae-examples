#!/bin/bash

curl --form "file=@$1" localhost:8080/upload
