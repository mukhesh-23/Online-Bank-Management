package com.Bankmanagement2;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "admin", eager = true)
@ViewScoped
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mobile;
    private String name;
    private String type;
    private double balance;
    private boolean is_admin;
    private List<Admin> a = new ArrayList<>();
    private List<Loans> l = new ArrayList<Loans>();

    @Resource(lookup = "java:/BankDS")
    private DataSource ds;

    public List<Loans> getL() {
        if (l.isEmpty()) {
            loadUserLoans();
        }
        return l;
    }

    public void setL(List<Loans> l) {
        this.l = l;
    }

    public List<Admin> getA() {
        if (a.isEmpty()) {
            loadUserList();
        }
        return a;
    }

    public void setA(List<Admin> a) {
        this.a = a;
    }

    public String getType() {
        return type;
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

    private void loadUserList() {
        a.clear();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM userdata WHERE is_admin = false");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setName(rs.getString("name"));
                admin.setMobile(rs.getString("mobile"));
                admin.setType(rs.getString("type"));
                admin.setBalance(rs.getDouble("balance"));
                admin.setIs_admin(rs.getBoolean("is_admin"));
                a.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String mobile) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM userdata WHERE mobile = ?")) {
            ps.setString(1, mobile);
            ps.executeUpdate();
            // Refresh the list after deletion
            loadUserList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserLoans() {
        l.clear();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM loans where is_approved=false AND is_rejected=false");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Loans loan = new Loans();
                loan.setMobile(rs.getString("mobile"));
                loan.setIncome(Double.toString(rs.getDouble("income")));
                loan.setAmount(Double.toString(rs.getDouble("amount")));
                loan.setTenure(Integer.toString(rs.getInt("tenure")));

                l.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveLoan(String mobile, String amount, String tenure) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE loans SET is_approved=true WHERE mobile = ? AND amount = ? AND tenure = ?")) {
            ps.setString(1, mobile);
            ps.setString(2, amount);
            ps.setString(3, tenure);
            ps.executeUpdate();

            PreparedStatement psUpdate = con.prepareStatement("UPDATE userdata SET balance = balance + ? WHERE mobile = ?");
            psUpdate.setDouble(1, Double.parseDouble(amount));
            psUpdate.setString(2, mobile);
            psUpdate.executeUpdate();
            // Refresh the list after deletion
            loadUserLoans();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rejectLoan(String mobile, String amount, String tenure) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE loans SET is_rejected=true WHERE mobile = ? AND amount = ? AND tenure = ?")) {
            ps.setString(1, mobile);
            ps.setString(2, amount);
            ps.setString(3, tenure);
            ps.executeUpdate();
            // Refresh the list after deletion
            loadUserLoans();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
