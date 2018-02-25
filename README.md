# Web Analytics Example (under development)

[![Build Status](https://travis-ci.org/joumenharzli/web-analytics-example.svg?branch=master)](https://travis-ci.org/joumenharzli/web-analytics-example)

## Overview
This is an example of web analytics and recommendations using Apache Kafka, Apache Storm, Apache Cassandra, Spring Boot and Angular 5

## Architecture
<img src="https://image.ibb.co/jYDHVx/Image1.png" />

## Running the example

### Requirements
This example requires:
* jdk 1.8.0_162
* Apache Kafka 0.11.0.2
* Apache Cassandra 2.2.11
* Apache Storm 1.1.1
* Apache Spark 2.2.1

### Configuration (currently)
* Access logs in the example are broadcasted via the topic <b>product-access-topic</b>. So you need to create it in Kafka.
* We need also to prepare the Cassandra column families and keyspace. Please insert the following lines
```cql
create keyspace web_analytics WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use web_analytics;

create table products_access_log(id UUID PRIMARY KEY, userId text, productId text, timestamp timestamp);

create table products_views_total(productId text PRIMARY KEY, count counter);

create table products_views_by_timestamp (productId text, timestamp timestamp, count counter, primary key (productId,timestamp) ) WITH CLUSTERING ORDER BY (timestamp desc);

create table products_views_by_user (productId text, userId text, timestamp timestamp, count counter, primary key (userId, productId,timestamp) ) WITH CLUSTERING ORDER BY (productId desc);

create table product_recommendations(userId text, productId text, rating double , timestamp timestamp, primary key ((userId, productId),timestamp) ) WITH CLUSTERING ORDER BY (timestamp desc)

```

### Then what ? (currently)
* Connect to the swagger interface in the shopping microservice via ```http://localhost:8081/swagger-ui.html```
* Invoke the list of shops api then list of products in shops then get the product by it's id
* A message will be boardcasted to Kafka wich holds the access log of the product
* Apache Storm will parse the received data from Kafka then process them and saves the results in Cassandra
* Check Cassandra to see the processed data
* Now you can launch Spark, which should be preferably scheduled to run every a defined period of time, this will insert recommendations of products for users using the ALS algorithm. the higher the value of the rating is the more likely the use will love the product.
* Check Cassandra to see the processed data

### Results (currently)
* You can check that everything is working by viewing data in Cassandra
<img src="https://image.ibb.co/eKGZ87/cassandra_finished.png" />
<img src="https://image.ibb.co/jUqxVx/recommendation.png" />
