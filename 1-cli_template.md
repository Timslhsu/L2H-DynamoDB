# AWS DynamoDB CLI Template

## Create table and GSI

```bash
aws dynamodb create-table --cli-input-json '
{
    "TableName": "events",
    "BillingMode": "PROVISIONED",
    "ProvisionedThroughput": {
        "ReadCapacityUnits": 1,
        "WriteCapacityUnits": 1
    },
    "KeySchema": [
        {
            "AttributeName": "deviceid",
            "KeyType": "HASH"
        },
        {
            "AttributeName": "ts",
            "KeyType": "RANGE"
        }
    ],
    "AttributeDefinitions": [
        {
            "AttributeName": "deviceid",
            "AttributeType": "S"
        },
        {
            "AttributeName": "ts",
            "AttributeType": "N"
        }

    ],
    "GlobalSecondaryIndexes": [
        {
            "IndexName": "ts-device-index",
            "KeySchema": [
                {
                    "AttributeName": "ts",
                    "KeyType": "HASH"
                },
                {
                    "AttributeName": "deviceid",
                    "KeyType": "RANGE"
                }
            ],
            "Projection": {
                "ProjectionType": "ALL"
            },
            "ProvisionedThroughput": {
                "ReadCapacityUnits": 1,
                "WriteCapacityUnits": 1
            }
        }
    ]
}
'
```

## Delete table

```bash
aws dynamodb delete-table --table-name events
```

## Update table - create GSI

```bash
aws dynamodb update-table --cli-input-json '
{
    "TableName": "events",
    "AttributeDefinitions": [
        {
            "AttributeName": "deviceid",
            "AttributeType": "S"
        },
        {
            "AttributeName": "ts",
            "AttributeType": "N"
        }

    ],
    "GlobalSecondaryIndexUpdates": [
        {
            "Create": {
                "IndexName": "ts-device-index",
                "KeySchema": [
                    {
                        "AttributeName": "ts",
                        "KeyType": "HASH"
                    },
                    {
                        "AttributeName": "deviceid",
                        "KeyType": "RANGE"
                    }
                ],
                "Projection": {
                    "ProjectionType": "ALL"
                },
                "ProvisionedThroughput": {
                    "ReadCapacityUnits": 1,
                    "WriteCapacityUnits": 1
                }
            }
        }
    ]
}
'
```

## Put item

```bash
aws dynamodb put-item --cli-input-json '
{
    "TableName": "events",
    "Item": {
        "deviceid": { "S": "deviceid-1" },
        "ts": { "N": "20191101"},
        "temperature": { "N": "25.5"},
        "humidity": { "N": "64"},
        "air": { "S": "Yellow" }
    },
    "ReturnConsumedCapacity": "INDEXES",
    "ReturnItemCollectionMetrics": "SIZE"
}
'
```

## Get item

```bash
aws dynamodb get-item --cli-input-json '
{
    "TableName": "events",
    "Key": {
        "deviceid": { "S": "deviceid-1" },
        "ts": { "N": "20191101"}
    },
    "ConsistentRead": true,
    "ReturnConsumedCapacity": "INDEXES",
    "ProjectionExpression": "temperature, humidity"
}
'
```

## Update item

```bash
aws dynamodb update-item --cli-input-json '
{
    "TableName": "events",
    "Key": {
        "deviceid": { "S": "deviceid-1" },
        "ts": { "N": "20191101"}
    },
    "UpdateExpression": "SET newattr = :v, temperature = :t",
    "ConditionExpression": "air = :a",
    "ExpressionAttributeValues": {
        ":v": { "S": "the new attribute"},
        ":t": { "N": "33"},
        ":a": { "S": "Yellow"}
    },
    "ReturnConsumedCapacity": "INDEXES"
}
'
```

## Delete item

```bash
aws dynamodb delete-item --cli-input-json '
{
    "TableName": "events",
    "Key": {
        "deviceid": { "S": "deviceid-1" },
        "ts": { "N": "20191101"}
    },
    "ReturnConsumedCapacity": "INDEXES"
}
'
```

## Query

```bash
aws dynamodb query --cli-input-json '
{
    "TableName": "events",
    "KeyConditionExpression": "deviceid = :devid",
    "FilterExpression": "humidity > :h",
    "ExpressionAttributeValues": {
        ":devid": { "S": "deviceid-1"},
        ":h": { "N": "70" }
    },
    "ProjectionExpression": "humidity",
    "ScanIndexForward": true,
    "ConsistentRead": true,
    "ReturnConsumedCapacity": "INDEXES"
}
'
```

## Scan

```bash
aws dynamodb scan --cli-input-json '
{
    "TableName": "events",
    "FilterExpression": " air = :a",
    "ExpressionAttributeValues": {
        ":a": { "S": "Red" }
    },
    "ProjectionExpression": "deviceid, ts",
    "ConsistentRead": true,
    "ReturnConsumedCapacity": "INDEXES"
}
'
```
