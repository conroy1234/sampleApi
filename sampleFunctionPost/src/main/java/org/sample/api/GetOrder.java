package org.sample.api;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;


public class GetOrder {

    private static String TABLE_NAME = System.getenv("ORDER_TABLE");
    public APIGatewayProxyResponseEvent getOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBAsyncClientBuilder.defaultClient();
        ScanResult scanResult = amazonDynamoDB.scan(new ScanRequest().withTableName(TABLE_NAME));
       List<Order> orsers = scanResult.getItems().stream().map(item-> new Order( Integer.parseInt(item.get("id").getN()),item.get("name").getS(),Integer.parseInt(item.get("price").getN()))).collect(Collectors.toList());

       String result = mapper.writeValueAsString(orsers);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(result);
    }
}
