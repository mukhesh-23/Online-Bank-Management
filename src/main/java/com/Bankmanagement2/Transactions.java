package com.Bankmanagement2;

import java.sql.Time;
import java.sql.Date;

public class Transactions {
	String mobile;
	Date dt;
	Time time;
	String type;
	String to_mob;
	double amount;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getDt() {
		return dt;
	}
	public void setDt(Date dt) {
		this.dt = dt;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTo_mob() {
		return to_mob;
	}
	public void setTo_mob(String to_mob) {
		this.to_mob = to_mob;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
	
}
