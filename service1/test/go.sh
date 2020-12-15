#!/bin/bash

# JSON object to pass to Lambda Function
# json={"\"row\"":50,"\"col\"":10,"\"bucketname\"":\"test.bucket.562f20.rab\"","\"filename\"":\"test.csv\""}
json={}

echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H "Content-Type: application/json" -X POST -d $json {https://xbo96wsusg.execute-api.us-east-2.amazonaws.com/TransformJava_dev}`
echo ""
echo "FINISHED"

