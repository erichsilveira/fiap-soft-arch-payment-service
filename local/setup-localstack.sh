aws --endpoint http://localhost:4566 sqs create-queue \
  --queue-name payment-request-queue \

aws --endpoint http://localhost:4566 sqs create-queue \
  --queue-name order-payment-queue \

aws --endpoint http://localhost:4566 sqs create-queue \
  --queue-name order-fulfillment-queue \
