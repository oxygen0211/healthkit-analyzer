package com.engelhardt.es;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import com.engelhardt.data.Analysis;
import com.engelhardt.data.Analysis.AnalysisDay;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticsearchClient {

	private static final String ENV_ES_HOST = "ES_HOST";

	private TransportClient client;
	private ObjectMapper mapper = new ObjectMapper();

	public ElasticsearchClient() throws UnknownHostException {
		String esHost = System.getenv(ENV_ES_HOST);
		client = new PreBuiltXPackTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), 9300));
	}

	public void indexAnalysis(Analysis analysis) throws JsonProcessingException {
		for (AnalysisDay day : analysis.getDays()) {
			String dayJson = mapper.writeValueAsString(day);
			byte[] analysisBytes = dayJson.getBytes();

			IndexRequestBuilder reqBuilder = client.prepareIndex("health", "healthkit-analysis");
			reqBuilder.setSource(analysisBytes);

			reqBuilder.get();
		}
	}

	public void finalize() {
		client.close();
	}
}