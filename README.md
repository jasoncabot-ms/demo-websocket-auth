# WebSocket Authentication

## Scenario

Be able to open a secure WebSocket connection with user authentication and authorization with minimal application changes

## Solution

From the view of the client

1. Client sends a POST request to `/connections` to get a unique, single-use URL for opening the WebSocket connection
2. Client opens a WebSocket to the returned endpoint

## Interesting Parts

* ... 
