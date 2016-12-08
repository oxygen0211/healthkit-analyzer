package com.engelhardt.analysis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.engelhardt.data.Analysis;
import com.engelhardt.data.Analysis.AnalysisDay;
import com.engelhardt.data.HealthKitData;

public class AnalyzerTest {
	private Analyzer fixture;
	
	@Before
	public void setUp(){
		this.fixture = new Analyzer();
	}
	
	@Test
	public void testSingleDayAnalysis(){
		HealthKitData data = new HealthKitData();
		
		Date date = new Date();
		float weight = 60;
		float carbos = 20;
		float fat = 20;
		float protein = 20;
		
		data.getWeights().put(date.getTime(), weight);
		data.getConsumedCarbohydrate().put(date.getTime(), carbos);
		data.getConsumedFat().put(date.getTime(), fat);
		data.getConsumedProtein().put(date.getTime(), protein);
		
		Analysis analysis = fixture.analyze(Arrays.asList(data));
		
		assertEquals(1, analysis.getDays().size());
		
		AnalysisDay day = analysis.getDays().get(0);
		
		assertEquals(date, day.getDate());
		assertEquals(weight, day.getWeight(), 0f);
		assertEquals(carbos, day.getConsumedCarbohydrate(), 0f);
		assertEquals(fat, day.getConsumedFat(), 0f);
		assertEquals(protein, day.getConsumedProtein(), 0f);
	}
}
