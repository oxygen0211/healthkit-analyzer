# Analyzing health data using apple Healtkit and Elasticsearch

This project has been created for a small project I wrote a Blog post about. The idea is to use data exported from Apple's Health app to get source nutrition and weight data and index it into an Elastic Stack to visualize it and find patterns.

## Usage
* In Apple's Health App, select your Profile -> Export data
* Copy the resulting zip on your computer, unzip it and rename the XML file to "export.xml"
* Set environment Variable "ES_HOST" to your Elasticsearch cluster hostname
* Start analysis by calling com.engelhardt.Main this asumes that the transport API is running on 9300

__ALL OF THIS CODE IS EXPERIMENTAL. IT IS NOT ADVISED TO USE IT FOR ANY CRITICAL APPLICATION__

## Deployment as AWS Lambda function
com.engelhardt.HealthkitAnalyzer implements the LambdaHandler interface and is intended to be deployed as a AWS Lambda function that analyses Health export files uploaded to an S3 bucket. However, this is not thoroughly tested and probably needs further work.
