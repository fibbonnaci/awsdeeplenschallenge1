from __future__ import print_function

import boto3
from decimal import Decimal
import json
import urllib
import datetime

print('Loading function')

rekognition = boto3.client('rekognition')


# --------------- Helper Functions to call Rekognition APIs ------------------


def detect_text(bucket, key):
    response = rekognition.detect_text(Image={"S3Object": {"Bucket": bucket, "Name": key}})
    texts = [ text_detection['DetectedText'] for text_detection in response['TextDetections']]
    return texts
    
def save_response(texts):
    table = boto3.resource('dynamodb').Table('text-detection-deeplens')
    #time_now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    time_now = int(datetime.datetime.now().strftime("%s")) * 1000 
    table.put_item(Item={'userId':'test','timeStampRecord': time_now, 'texts': texts})



# --------------- Main handler ------------------


def lambda_handler(event, context):
    '''Demonstrates S3 trigger that uses
    Rekognition APIs to detect faces, labels and index faces in S3 Object.
    '''

    # Get the object from the event
    bucket = event['Records'][0]['s3']['bucket']['name']
    key = urllib.unquote_plus(event['Records'][0]['s3']['object']['key'].encode('utf8'))
    try:
        # Calls rekognition DetectFaces API to detect faces in S3 object
        response = detect_text(bucket, key)
        save_response(response)

        # Print response to console.
        print(response)

        return response
    except Exception as e:
        print(e)
        print("Error processing object {} from bucket {}. ".format(key, bucket) +
              "Make sure your object and bucket exist and your bucket is in the same region as this function.")
        raise e
