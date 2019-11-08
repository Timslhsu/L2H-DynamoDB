#!/bin/bash
aws dynamodb create-table --cli-input-json '
{
    "TableName": "WeatherData",
    "BillingMode": "PROVISIONED",
    "ProvisionedThroughput": {
        "ReadCapacityUnits": 5,
        "WriteCapacityUnits": 5
    },
    "KeySchema": [
        {
            "AttributeName": "StationId",
            "KeyType": "HASH"
        },
        {
            "AttributeName": "ObsTime",
            "KeyType": "RANGE"
        }
    ],
    "AttributeDefinitions": [
        {
            "AttributeName": "StationId",
            "AttributeType": "S"
        },
        {
            "AttributeName": "ObsTime",
            "AttributeType": "N"
        }

    ]
}
'

sleep 30

for i in {1..13};
do
    aws dynamodb batch-write-item --request-items file://WeatherData$i.json
done
