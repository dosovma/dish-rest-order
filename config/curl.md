curl --location --request POST 'http://localhost:8080/api/v1/users/1/votes' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic bWlrZUBtYWlsLnJ1Om1pa2VwYXNzd29yZA==' \
--data-raw '{
"date": "2021-01-15T20:32:00",
"user_id": 4,
"restaurant_id": 3
}'

curl --location --request GET 'http://localhost:8080/api/v1/restaurants?date=2021-01-13' \
--header 'Authorization: Basic YWRtaW5AbWFpbC5ydTphZG1pbnBhc3N3b3Jk'

curl --location --request GET 'http://localhost:8080/api/v1/votes' \
--header 'Authorization: Basic YWRtaW5AbWFpbC5ydTphZG1pbnBhc3N3b3Jk'

curl --location --request GET 'http://localhost:8080/api/v1/votes/2' \
--header 'Authorization: Basic YWRtaW5AbWFpbC5ydTphZG1pbnBhc3N3b3Jk'