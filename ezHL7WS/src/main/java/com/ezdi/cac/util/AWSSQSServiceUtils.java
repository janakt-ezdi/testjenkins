package com.ezdi.cac.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class AWSSQSServiceUtils
{
	private static AWSSQSServiceUtils awssqsServiceUtils = null;

	private AmazonSQS amazonSqs;
	private String hl7wsProducerQueueURL;

	public static AWSSQSServiceUtils getInstance()
	{
		if (awssqsServiceUtils == null)
		{
			awssqsServiceUtils = new AWSSQSServiceUtils();
		}
		return awssqsServiceUtils;
	}

	private AWSSQSServiceUtils()
	{
		try
		{
			BasicAWSCredentials credentials = new BasicAWSCredentials(HL7PropertiesUtils.getAWSAccessKey(), HL7PropertiesUtils.getAWSSecretKey());
			this.amazonSqs = new AmazonSQSClient(credentials);
			this.amazonSqs.setEndpoint(HL7PropertiesUtils.getAWSSQSEndPoint());

			GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(HL7PropertiesUtils.getAWSSQSHL7WSProducerQueueName());
			hl7wsProducerQueueURL = this.amazonSqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();

		} catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void sendMessageToHL7WSProducerQueue(String message)
	{
		SendMessageResult messageResult = this.amazonSqs.sendMessage(new SendMessageRequest(this.hl7wsProducerQueueURL, message));
		System.out.println(messageResult.toString());
	}

}