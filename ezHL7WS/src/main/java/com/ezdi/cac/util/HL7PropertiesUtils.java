package com.ezdi.cac.util;

import java.io.File;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.ezdi.cac.constant.CommonConstant;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

public class HL7PropertiesUtils
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(HL7PropertiesUtils.class);
	public static Properties applicationProperties;
	static
	{
		try
		{
			LOGGER.info("Loading application properties.....");
			final ResourceLoader resourceLoader = new DefaultResourceLoader();

			String hl7wsFilePath = System.getenv(CommonConstant.HL7_WS_FILE_PATH_ENV_VARIABLE);
			LOGGER.info("HL7 webservice file path. hl7wsFilePath : " + hl7wsFilePath);

			String applicationPropertiesFileName = CommonConstant.APPLICATION_PROPERTIES_FILE_NAME;
			LOGGER.info("HL7 webservice application properties file name. applicationPropertiesFileName : " + applicationPropertiesFileName);

			String hl7wsApplicationPropertiFilePath = hl7wsFilePath + File.separator + applicationPropertiesFileName;
			LOGGER.info("HL7 webservice apllication properties file path. hl7wsApplicationPropertiFilePath : " + hl7wsApplicationPropertiFilePath);

			Resource applicationPropertiesResource = resourceLoader.getResource(hl7wsApplicationPropertiFilePath);
			applicationProperties = PropertiesLoaderUtils.loadProperties(applicationPropertiesResource);

			LOGGER.info("Application properties loaded successfully.");

			LOGGER.info("Application properties. applicationProperties : " + applicationProperties);
		} catch (Exception e)
		{
			LOGGER.error(e);
		}
	}

	public static String getAWSAccessKey()
	{
		return applicationProperties.getProperty("aws.access.key");
	}

	public static String getAWSSecretKey()
	{
		return applicationProperties.getProperty("aws.secret.key");
	}

	public static String getAWSRegion()
	{
		return applicationProperties.getProperty("aws.region");
	}

	public static String getAWSS3RootBucketName()
	{
		return applicationProperties.getProperty("aws.s3.root.bucket.name");
	}

	public static String getAWSS3OriginalTextBucketName()
	{
		return applicationProperties.getProperty("aws.s3.original.text.bucket.name");
	}
	
	public static String getAWSS3ScannedDocumentBucketName()
	{
		return applicationProperties.getProperty("aws.s3.scanned.document.bucket.name");
	}

	public static String getAWSS3OriginalTextBucketPath(String tenantCode)
	{
		return getAWSS3RootBucketName() + File.separator + tenantCode + File.separator + getAWSS3OriginalTextBucketName();
	}
	
	public static String getAWSS3ScannedDocumentBucketPath(String tenantCode)
	{
		return getAWSS3RootBucketName() + File.separator + tenantCode + File.separator + getAWSS3ScannedDocumentBucketName();
	}

	public static String getAWSSQSEndPoint()
	{
		return applicationProperties.getProperty("aws.sqs.end.point");
	}

	public static String getAWSSQSHL7WSProducerQueueName()
	{
		return applicationProperties.getProperty("aws.sqs.hl7ws.producer.queue.name");
	}

}
