package com.engelhardt.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HealthKitData {
	//e.g. 2016-10-22 15:48:07 +0200
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

	private Map<Long, Float> weights = new HashMap<>();
	private Map<Long, Float> consumedProtein = new HashMap<>();
	private Map<Long, Float> consumedFat = new HashMap<>();
	private Map<Long, Float> consumedCarbohydrate = new HashMap<>();
	private Map<Long, Float> consumedEnergy = new HashMap<>();
	
	public void addWeight(String date, float weight) throws ParseException{
		weights.put(parseDate(date), weight);
	}
	
	public void addConsumedProtein(String date, float protein) throws ParseException{
		long parsedDate = parseDate(date);
		Float overallProtein = consumedProtein.get(parsedDate);
		if(overallProtein == null){
			overallProtein = 0.0f;
		}
		
		overallProtein += protein;
		consumedProtein.put(parsedDate, overallProtein);
	}
	
	public void addConsumedFat(String date, float fat) throws ParseException{
		long parsedDate = parseDate(date);
		Float overallFat = consumedFat.get(parsedDate);
		if (overallFat == null){
			overallFat = 0.0f;
		}
		
		overallFat += fat;
		
		consumedFat.put(parsedDate, overallFat);
	}
	
	public void addConsumedCarboHydrate(String date, float carbo) throws ParseException{
		long parsedDate = parseDate(date);
		Float overallCarbs = consumedCarbohydrate.get(parsedDate);
		if (overallCarbs == null){
			overallCarbs = 0.0f;
		}
		overallCarbs += carbo;
		
		consumedCarbohydrate.put(parsedDate, overallCarbs);
	}
	
	public void addConsumedEnergy(String date, float cals) throws ParseException{
		long parsedDate = parseDate(date);
		Float overallCals = consumedEnergy.get(parsedDate);
		if(overallCals == null){
			overallCals = 0.0f;
		}
		overallCals += cals;
		
		consumedEnergy.put(parsedDate, overallCals);
	}
	
	public Map<Long, Float> getWeights() {
		return weights;
	}

	public Map<Long, Float> getConsumedProtein() {
		return consumedProtein;
	}

	public Map<Long, Float> getConsumedFat() {
		return consumedFat;
	}
	
	public Map<Long, Float> getConsumedCarbohydrate() {
		return consumedCarbohydrate;
	}
	
	public float getConsumedCarbohydrate(long date) {
		return consumedCarbohydrate.get(date) == null ? 0.0f : consumedCarbohydrate.get(date);
	}
	
	public float getConsumedProtein(long date) {
		return consumedProtein.get(date) == null ? 0.0f : consumedProtein.get(date);
	}

	public float getConsumedFat(long date) {
		return consumedFat.get(date) == null ? 0.0f : consumedFat.get(date);
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}
	
	private long parseDate(String dateString) throws ParseException{
		Date date = sdf.parse(dateString);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTimeInMillis();
	}

	public Map<Long, Float> getConsumedEnergy() {
		return consumedEnergy;
	}

	public void setConsumedEnergy(Map<Long, Float> consumedEnergy) {
		this.consumedEnergy = consumedEnergy;
	}
}
