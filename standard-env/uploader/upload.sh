#!/bin/bash

curl --form "file=@$1" localhost:8089/upload
