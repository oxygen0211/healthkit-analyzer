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
	
	private static final String ENV_ES_CLUSTERNAME = "ES_CLUSTERNAME";
	private static final String ENV_ES_USER = "ES_USER";
	private static final String ENV_ES_PASSWORD = "ES_PASSWORD";
	private static final String ENV_ES_CLUSTERID = "ES_CLUSTERID";
	private static final String ENV_ES_REGION = "ES_REGION";

	private TransportClient client;
	private ObjectMapper mapper = new ObjectMapper();
	
	public ElasticsearchClient() throws UnknownHostException
	{
//		String esUser = System.getenv(ENV_ES_USER);
//		String esPass = System.getenv(ENV_ES_PASSWORD);
//		String clusterId = System.getenv(ENV_ES_CLUSTERID);
//		String region = System.getenv(ENV_ES_REGION);
//		String clustername = System.getenv(ENV_ES_CLUSTERNAME);
//		
//		Settings settings = Settings.builder()
//	            .put("client.transport.nodes_sampler_interval", "5s")
//	            .put("client.transport.sniff", true)
//	            .put("client.transport.ignore_cluster_name", false)
//	            .put("transport.tcp.compress", true)
//	            .put("xpack.security.transport.ssl.enabled", true)
//	            .put("xpack.security.user", esUser + ":" + esPass)
//	            .put("client.transport.ignore_cluster_name", true)
//	            .put("request.headers.X-Found-Cluster", clusterId)
//	            .put("cluster.name", clustername)
//	            .build();
//
//			String hostname = clusterId + "." + region + ".aws.found.io";
//			// Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
//			// Only port 9343 (SSL-encrypted) is currently supported.
//			client = new PreBuiltXPackTransportClient(settings)
//					 .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), 9200));
			client = new PreBuiltXPackTransportClient(Settings.EMPTY)
					 .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.2.160"), 9300));
	}	
	public void indexAnalysis(Analysis analysis) throws JsonProcessingException
	{
		for(AnalysisDay day : analysis.getDays())
		{
			String dayJson = mapper.writeValueAsString(day);
			byte[] analysisBytes = dayJson.getBytes();
			
			IndexRequestBuilder reqBuilder = client.prepareIndex("health", "healthkit-analysis");
			reqBuilder.setSource(analysisBytes);
			
			reqBuilder.get();
		}
	}
	
	public void finalize()
	{
		client.close();
	}
}