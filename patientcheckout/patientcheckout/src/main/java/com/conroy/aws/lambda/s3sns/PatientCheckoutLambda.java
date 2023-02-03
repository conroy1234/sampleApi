package com.conroy.aws.lambda.s3sns;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class PatientCheckoutLambda {

    AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();
    ObjectMapper objectMapper = new ObjectMapper();
    private AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final String TOPIC_NAME = System.getenv("PATIENT_CHECKOUT_TOPIC");

    public void handler(S3Event s3Event) {
        s3Event.getRecords().forEach(record -> {
            S3ObjectInputStream s3ObjectInputStream = amazonS3.getObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey())
                    .getObjectContent();

            try {
               List<PatientCheckoutEvent>  patientCheckoutEvents = List.of(objectMapper.readValue(s3ObjectInputStream, PatientCheckoutEvent[].class));
               patientCheckoutEvents.forEach(patient ->{
                   try {
                      String patientData = objectMapper.writeValueAsString(patient);

                       sns.publish(TOPIC_NAME,patientData);
                   } catch (JsonProcessingException e) {
                       throw new RuntimeException(e);
                   }
               });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
