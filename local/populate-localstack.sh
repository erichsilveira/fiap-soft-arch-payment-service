
aws --endpoint http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/payment-request-queue \
--message-body '{
             "type": "payment.requested",
             "data": {
               "id": "1",
               "description": test,
               "status": "created"
             },
             "specversion": "1.0",
             "source": "payment-service",
             "datacontenttype": "application/json",
             "id": "432fe667-c561-4d2a-ac54-074d0d691054",
             "time": "2023-07-25T21:43:23+00:00"
           }'