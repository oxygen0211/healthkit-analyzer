package com.engelhardt.analysis;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.engelhardt.data.Analysis;
import com.engelhardt.data.Analysis.AnalysisDay;
import com.engelhardt.data.HealthKitData;

public class Analyzer {
	public Analysis analyze(List<HealthKitData> healthKitData)
	{
		Analysis analysis = new Analysis();
		
		for (HealthKitData data : healthKitData)
		{
			List<Analysis.AnalysisDay> days = data.getWeights()
	 				  .keySet()
	 				  .parallelStream()
	 				  .map(date -> createAnalysis(date, data))
	 				  .collect(Collectors.toList());
										 				  
			analysis.addDays(days);
		}
		
		calculateRatios(analysis);
		
		return analysis;
	}
	
	private AnalysisDay createAnalysis(long date, HealthKitData data)
	{
		float protein = data.getConsumedProtein(date);
		float fat = data.getConsumedFat(date);
		float carbs = data.getConsumedCarbohydrate(date);
		float weight = data.getWeights().get(date);
		return new AnalysisDay(new Date(date), protein, fat, carbs, weight);
	}
	
	private Analysis calculateRatios(Analysis analysis)
	{
		List<AnalysisDay> days = analysis.getDays();
		
		for (int i=0 ; i<days.size() ; i++)
		{
			if(i<1)
			{
				continue;
			}
			
			float previousWeight = days.get(i-1).getWeight();
			
			AnalysisDay currentDay = days.get(i);
			currentDay.setWeightChange(currentDay.getWeight()-previousWeight);
			
			days.set(i, currentDay);
		}
		
		analysis.setDays(days);
		
		return analysis;
	}
}
