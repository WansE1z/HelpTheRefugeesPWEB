#!/bin/bash

code = curl -s -w '\n%{http_code}\n' 'localhost/' | tail -n 1
expect_code = "200"

if [ "$code" == "$expect_code" ]
then {
   echo "ok"
   exit 0
}
else {
   echo "not ok"
   exit -1
}
fi
