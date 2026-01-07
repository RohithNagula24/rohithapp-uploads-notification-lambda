package com.rohith.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class UploadsNotificationHandler
        implements RequestHandler<SQSEvent, Void> {

    private static final SnsClient snsClient = SnsClient.create();
    private static final String TOPIC_ARN =
            System.getenv("SNS_TOPIC_ARN");

    @Override
    public Void handleRequest(SQSEvent event, Context context) {


        event.getRecords().forEach(record -> {
            snsClient.publish(PublishRequest.builder()
                    .topicArn(TOPIC_ARN)
                    .message(record.getBody())
                    .build());
        });
        context.getLogger().log("Deployed via CI/CD pipeline");

        return null;
    }
}
