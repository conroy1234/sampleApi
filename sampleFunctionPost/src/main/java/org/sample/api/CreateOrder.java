package org.sample.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrder {

    private final ObjectMapper mapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBAsyncClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent CreateOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {

        Order order = mapper.readValue(requestEvent.getBody(), Order.class);

        Table table = dynamoDB.getTable(System.getenv("ORDER_TABLE"));
      // order.setId("1");
        Item item = new Item().withPrimaryKey("id", order.getId())
                .withString("name", order.getName())
                .withInt("price", order.getPrice());

        table.putItem(item);
        return new APIGatewayProxyResponseEvent().withBody(" data: " + order).withStatusCode(200);
    }
}
