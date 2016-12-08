package com.engelhardt.data;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Analysis {
	
	@XmlElement
	private List<AnalysisDay> days = new ArrayList<>();
	
	public List<AnalysisDay> getDays() {
		return days;
	}
	
	public void setDays(List<AnalysisDay> days) {
		this.days = days;
	}

	public void addDay(AnalysisDay day) {
		this.days.add(day);
	}
	
	public void addDays(List<AnalysisDay> days){
		this.days.addAll(days);
	}
	
	public void serialize(OutputStream stream) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(this.getClass());
		Marshaller marshaller = context.createMarshaller();
		
		marshaller.marshal(this, stream);
	}

	@XmlRootElement
	public static class AnalysisDay
	{
		@XmlElement
		private Date date = new Date();
		
		@XmlElement
		private float consumedProtein = 0;
		
		@XmlElement
		private float consumedFat = 0;
		
		@XmlElement
		private float consumedCarbohydrate = 0;
		
		@XmlElement
		private float weight = 0;
		
		@XmlElement
		private float weightChange = 0;
		
		public AnalysisDay()
		{
			
		}
		
		public AnalysisDay(Date date, float consumedProtein, float consumedFat, float consumedCarbohydrate, float weight)
		{
			this.date = date;
			this.consumedProtein = consumedProtein;
			this.consumedFat = consumedFat;
			this.consumedCarbohydrate = consumedCarbohydrate;
			this.weight = weight;
		}
		
		public Date getDate() {
			return date;
		}
		public float getConsumedProtein() {
			return consumedProtein;
		}
		public float getConsumedFat() {
			return consumedFat;
		}
		public float getConsumedCarbohydrate() {
			return consumedCarbohydrate;
		}
		public float getWeight(){
			return weight;
		}

		public float getWeightChange() {
			return weightChange;
		}

		public void setWeightChange(float weightChange) {
			this.weightChange = weightChange;
		}
		
	}
}
