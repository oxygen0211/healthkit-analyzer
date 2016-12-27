package com.engelhardt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import com.engelhardt.data.HealthKitData;

public class Main {

	public static void main(String[] args) throws Exception {
		InputStream stream = new FileInputStream(new File("export.xml"));

		HealthkitAnalyzer analyizer = new HealthkitAnalyzer();

		HealthKitData data = analyizer.parseHealthkitDump(stream);
		analyizer.startAnalysis(Arrays.asList(data));
	}

}
