package com.Bankmanagement2;

import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Time;
//import java.time.LocalDate;
//import java.time.LocalTime;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

@ManagedBean(name = "loan", eager = true)
public class Loans{
    private String password;
    private String tenure;
    private String amount;
    private String mobile;
    private String income;
    private boolean is_approved;
    private boolean is_rejected;
    
    
    @ManagedProperty(value = "#{login.u}")
    private User user;
    
    
    
    @Resource(lookup = "java:/BankDS")
    private DataSource ds;
    
    
    
    
    public boolean isIs_rejected() {
		return is_rejected;
	}

	public void setIs_rejected(boolean is_rejected) {
		this.is_rejected = is_rejected;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    

	public boolean isIs_approved() {
		return is_approved;
	}

	public void setIs_approved(boolean is_approved) {
		this.is_approved = is_approved;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String applyLoan() {
		try (Connection con = ds.getConnection()) {
            // Verify password
            PreparedStatement ps = con.prepareStatement("SELECT password FROM userdata WHERE mobile=?");
            ps.setString(1, user.getMobile());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("password");
                if (pass.equals(password)) {
                    // Update balance
                	PreparedStatement ps2 = con.prepareStatement("INSERT INTO loans (mobile, tenure, amount, income, is_approved, is_rejected) VALUES (?, ?, ?, ?, ?, ?)");
                    ps2.setString(1, user.getMobile());
                    ps2.setString(2, tenure);
                    ps2.setDouble(3, Double.parseDouble(amount));
                    ps2.setDouble(4, Double.parseDouble(income));
                    ps2.setBoolean(5, false);
                    ps2.setBoolean(6,false);
                    int rowsAffected = ps2.executeUpdate();
                    if (rowsAffected > 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Loan Applied Successfully", null));
                        user.l.clear();
                        try {
	                    	PreparedStatement ps4 = con.prepareStatement("SELECT * FROM loans WHERE mobile = ?");
	                        ps4.setString(1, user.getMobile());
	                        ResultSet rs4 = ps4.executeQuery();
	
	                        while (rs4.next()) {
	                            Loans loan = new Loans();
	                            loan.setMobile(rs4.getString("mobile"));
	                            loan.setIncome(Double.toString(rs4.getDouble("income")));
	                            loan.setAmount(Double.toString(rs4.getDouble("amount")));
	                            loan.setTenure(Integer.toString(rs4.getInt("tenure")));
	                            loan.setIs_approved(rs4.getBoolean("is_approved"));
	                            loan.setIs_rejected(rs4.getBoolean("is_rejected"));
	                            user.l.add(loan);
	                        }
	                    }catch (SQLException e) {
	                        e.printStackTrace();
	                    }
                        return null;
                        
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in Applying Loan", null));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in Applying Loan", null));
            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database error: " + e.getMessage(), null));
            e.printStackTrace();
        }
		return null;
        
    }
}
