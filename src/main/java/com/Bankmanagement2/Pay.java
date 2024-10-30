package com.Bankmanagement2;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@ManagedBean(name = "pay", eager = true)
@SessionScoped
public class Pay implements Serializable {
    private static final long serialVersionUID = 1L;
    public String mobile;
	public String amount;
    public String password;

    @Resource(lookup = "java:/BankDS")
    private DataSource ds;
    

    @ManagedProperty(value = "#{login.u}")
    private User user;

    // Getters and Setters
    public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String payAmount() {
        try (Connection con = ds.getConnection()) {
            // Verify password
            PreparedStatement ps = con.prepareStatement("SELECT password FROM userdata WHERE mobile=?");
            ps.setString(1, user.getMobile());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("password");
                if (pass.equals(password)) {
                    // Update balance
                	PreparedStatement ps2 = con.prepareStatement("SELECT * FROM userdata WHERE mobile=?");
                	ps2.setString(1, mobile);
                	ResultSet rs2=ps2.executeQuery();
                	if(rs2.next()) {
	                    PreparedStatement psUpdate = con.prepareStatement("UPDATE userdata SET balance = balance + ? WHERE mobile = ?");
	                    psUpdate.setDouble(1, Double.parseDouble(amount));
	                    psUpdate.setString(2, mobile);
                         // Update the balance in the session
	                    PreparedStatement ps3 = con.prepareStatement("UPDATE userdata SET balance = balance - ? WHERE mobile = ?");
	                    ps3.setDouble(1, Double.parseDouble(amount));
	                    ps3.setString(2, user.getMobile());
	                    ps3.executeUpdate();
	                    user.setBalance(user.getBalance() - Double.parseDouble(amount));
	                    int rowsAffected = psUpdate.executeUpdate();
	                	
	                    if (rowsAffected > 0) {
	                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Amount deposited successfully", null));
	                        PreparedStatement insertTransaction = con.prepareStatement("INSERT INTO transactions (mobile, date, time, type, amount, to_mob) VALUES (?, ?, ?, ?, ?, ?)");
	                        insertTransaction.setString(1, user.getMobile());
	                        insertTransaction.setDate(2, Date.valueOf(LocalDate.now()));
	                        insertTransaction.setTime(3, Time.valueOf(LocalTime.now()));
	                        insertTransaction.setString(4, "Deposit");
	                        insertTransaction.setDouble(5, Double.parseDouble(amount));
	                        insertTransaction.setString(6, mobile);
	                        insertTransaction.executeUpdate();
	                        
	                        insertTransaction.setString(1, mobile);
	                        insertTransaction.setDate(2, Date.valueOf(LocalDate.now()));
	                        insertTransaction.setTime(3, Time.valueOf(LocalTime.now()));
	                        insertTransaction.setString(4, "Credit");
	                        insertTransaction.setDouble(5, Double.parseDouble(amount));
	                        insertTransaction.setString(6, user.mobile);
	                        insertTransaction.executeUpdate();
	                    }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mobile Number not found", null));
                    }try  {
                    	user.t.clear();
                        PreparedStatement ps3 = con.prepareStatement("SELECT * FROM transactions WHERE mobile = ?");
                        ps3.setString(1, user.getMobile());
                        ResultSet rs3 = ps3.executeQuery();

                        while (rs3.next()) {
                            Transactions transaction = new Transactions();
                            transaction.setMobile(rs3.getString("mobile"));
                            transaction.setDt(rs3.getDate("date"));
                            transaction.setTime(rs3.getTime("time"));
                            transaction.setType(rs3.getString("type"));
                            transaction.setTo_mob(rs3.getString("to_mob"));
                            transaction.setAmount(rs3.getDouble("amount"));
                            user.t.add(transaction);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in depositing amount", null));
            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database error: " + e.getMessage(), null));
            e.printStackTrace();
        }
        return null;
    }
}
