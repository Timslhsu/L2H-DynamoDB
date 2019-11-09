import boto3
from botocore.config import Config
import uuid
import random
import string

config = Config(
    retries=dict(max_attempts=0)
)
client = boto3.client('dynamodb', config=config)
content = ''.join(random.choices(string.ascii_uppercase, k=980))

req = 0
while req < 300:
    data = {
        'Throttle-Test': [
            {
                'PutRequest': {
                    'Item': {
                        'pk': {'S': str(uuid.uuid4())},
                        'text': {'S': content}
            }}},
            {
                'PutRequest': {
                    'Item': {
                        'pk': {'S': str(uuid.uuid4())},
                        'text': {'S': content}
            }}},
            {
                'PutRequest': {
                    'Item': {
                        'pk': {'S': str(uuid.uuid4())},
                        'text': {'S': content}
            }}},
            {
                'PutRequest': {
                    'Item': {
                        'pk': {'S': str(uuid.uuid4())},
                        'text': {'S': content}
            }}},
            {
                'PutRequest': {
                    'Item': {
                        'pk': {'S': str(uuid.uuid4())},
                        'text': {'S': content}
            }}}
        ]
    }

    response = client.batch_write_item(
        RequestItems=data,
        ReturnConsumedCapacity='TOTAL'
    )
    req += 1

    print('Req {}: Consumed {}, HTTP {}, Retry {} {}'.format(
        req,
        response['ConsumedCapacity'][0]['CapacityUnits'],
        response['ResponseMetadata']['HTTPStatusCode'],
        response['ResponseMetadata']['RetryAttempts'],
        '*****' if int(response['ResponseMetadata']['RetryAttempts']) > 0 else ''
    ))

