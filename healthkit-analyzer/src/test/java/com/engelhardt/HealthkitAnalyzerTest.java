package com.engelhardt;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.engelhardt.data.HealthKitData;

public class HealthkitAnalyzerTest {
	private HealthkitAnalyzer fixture;

	@Before
	public void setUp() {
		fixture = new HealthkitAnalyzer();
	}

	@Test
	public void testParseHealthkitDump() throws Exception {
		InputStream stream = new FileInputStream(new File("src/test/resources/export-single-element.xml"));

		HealthKitData data = fixture.parseHealthkitDump(stream);
		assertEquals(1, data.getWeights().size());
		assertEquals(1, data.getConsumedCarbohydrate().size());
		assertEquals(1, data.getConsumedFat().size());
		assertEquals(1, data.getConsumedProtein().size());
	}
}
