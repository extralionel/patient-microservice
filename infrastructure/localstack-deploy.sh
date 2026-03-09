#!/bin/bash
set -e # Stops the script if any command fails

export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

export AWS_ENDPOINT_URL="http://localhost:4566"

# 3. Ya no hace falta poner --endpoint-url en cada línea
aws cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"

aws elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text