package com.Bankmanagement2;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

@ManagedBean(name = "createuser", eager = true)
public class CreateUser{
    private String name;
    private String password;
    private String type;
    private String balance;
    private String confirmPassword;
    private String mobile;
    
    
    
    @Resource(lookup = "java:/BankDS")
    private DataSource ds;
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String createUser() {
        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
            return null;
        }

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO userdata (name, mobile, password, type, balance, is_admin) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, password);
            ps.setString(4, type);
            ps.setDouble(5, Double.parseDouble(balance));
            ps.setBoolean(6, false);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User created successfully", null));
                return "login.xhtml?faces-redirect=true"; // Redirect to login page
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating user", null));
                return null;
            }
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database error: " + e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }
}
