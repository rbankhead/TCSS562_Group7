#!/bin/bash
#function=`cat config.json | jq '.functionName' | tr -d '"'`
function="servThree"
json=`cat config.json | jq -c '.payload'`
ibmjson=`cat config.json | jq '.payload' | tr -d '"' | tr -d '{' | tr -d '}' | tr -d ':'`

echo
echo ----- Testing $function on each platform with CLI. ------
echo

echo
echo Invoking $function on AWS Lambda 50 times...
echo
aws lambda invoke --invocation-type RequestResponse --function-name $function --region us-east-2 --payload $json /dev/stdout

