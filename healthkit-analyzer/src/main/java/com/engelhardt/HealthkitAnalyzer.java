package com.engelhardt;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.engelhardt.analysis.Analyzer;
import com.engelhardt.data.Analysis;
import com.engelhardt.data.HealthKitData;
import com.engelhardt.es.ElasticsearchClient;
import com.fasterxml.jackson.core.JsonProcessingException;

public class HealthkitAnalyzer implements RequestHandler<S3Event, Analysis> {
	
	private static final Logger LOG = LogManager.getLogger();
	
	public Analysis handleRequest(S3Event event, Context context) {
		List<HealthKitData> healthData = new ArrayList<>();
		AmazonS3 s3Client = new AmazonS3Client();
		String bucketName = "";
		String srcKey = "";
		try {
			for (S3EventNotificationRecord record : event.getRecords()) {
				bucketName = record.getS3().getBucket().getName();

				srcKey = record.getS3().getObject().getKey().replace('+', ' ');
				srcKey = URLDecoder.decode(srcKey, "UTF-8");

				S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, srcKey));
				
				LOG.info("Analyzing file " + srcKey + " from bucket " + bucketName);
				
				healthData.add(parseHealthkitDump(s3Object.getObjectContent()));
			}
			
			return startAnalysis(healthData);
			
		} catch (Exception e) {
			LOG.error("Error during lambda execution", e);
			throw new RuntimeException(e);
		}
	}
	
	Analysis startAnalysis(List<HealthKitData> healthData) throws UnknownHostException, JsonProcessingException
	{
		Analysis analysis = new Analyzer().analyze(healthData);
		
		ElasticsearchClient esClient = new ElasticsearchClient();
		esClient.indexAnalysis(analysis);
		esClient.finalize();
		return analysis;
	}

	HealthKitData parseHealthkitDump(InputStream stream)
			throws XMLStreamException, FactoryConfigurationError, NumberFormatException, ParseException {
		HealthKitData data = new HealthKitData();

		XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(stream);

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();

			if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
				StartElement startElement = event.asStartElement();
				
				Attribute valueAttribute = startElement.getAttributeByName(new QName("value"));
				Attribute dateAttribute = startElement.getAttributeByName(new QName("creationDate"));
				Attribute typeAttribute = startElement.getAttributeByName(new QName("type"));
				
				if(valueAttribute == null || dateAttribute == null || typeAttribute == null)
				{
					continue;
				}
				
				String value = valueAttribute.getValue();
				String date = dateAttribute.getValue();

				String elementName = typeAttribute.getValue();

				switch (elementName) {
				case "HKQuantityTypeIdentifierBodyMass":
					data.addWeight(date, Float.valueOf(value));
					break;

				case "HKQuantityTypeIdentifierDietaryProtein":
					data.addConsumedProtein(date, Float.valueOf(value));
					break;

				case "HKQuantityTypeIdentifierDietaryFatTotal":
					data.addConsumedFat(date, Float.valueOf(value));
					break;

				case "HKQuantityTypeIdentifierDietaryCarbohydrates":
					data.addConsumedCarboHydrate(date, Float.valueOf(value));
				}
			}
			continue;
		}

		return data;
	}
}
