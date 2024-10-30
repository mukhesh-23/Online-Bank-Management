package com.Bankmanagement2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import javax.annotation.Resource;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.sql.DataSource;
//import java.sql.*;

//@ManagedBean(name = "user", eager =true)
//@SessionScoped

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String mobile;
    public String name; 
    public String type;
    public double balance;
    public boolean is_admin;
    public List<Transactions> t = new ArrayList<Transactions>();
    public List<Loans> l = new ArrayList<Loans>();
    
//    @Resource(lookup = "java:/MySqlDS")
//    private DataSource ds;
    
    
    
    public String getType() {
		return type;
	}

	public List<Loans> getL() {
		return l;
	}

	public void setL(List<Loans> l) {
		this.l = l;
	}

	public boolean getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<Transactions> getT() {
		return t;
	}

	public void setT(List<Transactions> l) {
		this.t = l;
	}

	public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
//    public String transactionList() {
//        try (Connection con = ds.getConnection()) {
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE mobile = ?");
//            ps.setString(1, getMobile());
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                Transactions transaction = new Transactions();
//                transaction.setMobile(rs.getString("mobile"));
//                transaction.setDt(rs.getDate("date"));
//                transaction.setTime(rs.getTime("time"));
//                transaction.setType(rs.getString("type"));
//                t.add(transaction);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//		return null;
//    }
//    
    
}
