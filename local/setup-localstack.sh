aws --endpoint http://localhost:4566 sqs create-queue \
  --queue-name payment-request-queue \

aws --endpoint http://localhost:4566 sqs create-queue \
  --queue-name payment-response-queue \

