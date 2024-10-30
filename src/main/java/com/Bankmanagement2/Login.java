package com.Bankmanagement2;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.*;

import java.io.Serializable;
import java.sql.*;
import javax.faces.bean.SessionScoped;


@ManagedBean(name = "login", eager =true)
@SessionScoped

public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
	public String number;
	public String password;
//	public String type;
//	public String name;
//	public int balance;
	
    public User getU() {
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}


	 @Resource(lookup = "java:/BankDS")
	    private DataSource ds;
	    
    
     
    public User u = new User();
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public int getBalance() {
//		return balance;
//	}
//
//	public void setBalance(int balance) {
//		this.balance = balance;
//	}
	
	
	public String loginUser() {
		try {
			Connection con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT password FROM userdata WHERE mobile=?");
			ps.setString(1, number);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String pass = rs.getString("password");
				if(pass.equals(password)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful", null));
                    PreparedStatement ps2 = con.prepareStatement("SELECT * FROM userdata WHERE mobile=?");
                    ps2.setString(1, number);
                    ResultSet rs2 = ps2.executeQuery();
                    
	                    while(rs2.next()) {
	                    	u.setBalance(rs2.getInt("balance"));
	                    	u.setName(rs2.getString("name"));
	                    	u.setMobile(number);
	                    	u.setType(rs2.getString("type"));
	                    	u.setIs_admin(rs2.getBoolean("is_admin"));
	                    }
	                    u.t.clear();
	                    try  {
	                        PreparedStatement ps3 = con.prepareStatement("SELECT * FROM transactions WHERE mobile = ?");
	                        ps3.setString(1, number);
	                        ResultSet rs3 = ps3.executeQuery();
	
	                        while (rs3.next()) {
	                            Transactions transaction = new Transactions();
	                            transaction.setMobile(rs3.getString("mobile"));
	                            transaction.setDt(rs3.getDate("date"));
	                            transaction.setTime(rs3.getTime("time"));
	                            transaction.setType(rs3.getString("type"));
	                            transaction.setTo_mob(rs3.getString("to_mob"));
	                            transaction.setAmount(rs3.getDouble("amount"));
	                            u.t.add(transaction);
	                        }
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                    
	                    u.l.clear();
	                    try {
	                    	PreparedStatement ps4 = con.prepareStatement("SELECT * FROM loans WHERE mobile = ?");
	                        ps4.setString(1, number);
	                        ResultSet rs4 = ps4.executeQuery();
	
	                        while (rs4.next()) {
	                            Loans loan = new Loans();
	                            loan.setMobile(rs4.getString("mobile"));
	                            loan.setIncome(Double.toString(rs4.getDouble("income")));
	                            loan.setAmount(Double.toString(rs4.getDouble("amount")));
	                            loan.setTenure(Integer.toString(rs4.getInt("tenure")));
	                            loan.setIs_approved(rs4.getBoolean("is_approved"));
	                            loan.setIs_rejected(rs4.getBoolean("is_rejected"));
	                            u.l.add(loan);
	                        }
	                    }catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                    
	                    if (u.getIs_admin()) {
	                        return "admin.xhtml?faces-redirect=true";
	                    }else {
	                    	return "home.xhtml?faces-redirect=true";
	                    }
                     
				}else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Incorrect Password", null));

				}
			}else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mobile number not found", null));

			}
		}catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database error: " + e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
		return null;
	}
	
	
}
