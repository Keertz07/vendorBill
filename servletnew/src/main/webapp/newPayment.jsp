<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zoho.Servletnew.bo.CustomerBo"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>New Payment</title>
  <style>
    body {
      margin: 0;
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
    }

    .container {
      display: flex;
      height: 100vh;
    }

    .sidebar {
      width: 250px;
      background-color: #2c3e50;
      color: white;
      display: flex;
      flex-direction: column;
      padding-top: 20px;
      box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    }

    .sidebar a {
      padding: 15px;
      text-decoration: none;
      color: white;
      display: block;
      font-size: 18px;
      transition: background-color 0.3s;
    }

    .sidebar a:hover {
      background-color: #34495e;
    }

    .main-content {
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 20px;
    }

    .form-container {
      background-color: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      width: 500px;
    }

    .form-container h2 {
      margin-bottom: 20px;
      font-size: 24px;
      color: #2c3e50;
      text-align: center;
    }

    .form-container label {
      display: block;
      margin-bottom: 5px;
      font-size: 16px;
    }

    .form-container input,
    .form-container select {
      width: 100%;
      padding: 10px;
      font-size: 16px;
      border: 1px solid #ccc;
      border-radius: 5px;
      margin-bottom: 15px;
    }

    .form-container input[type="submit"] {
      background-color: #2c3e50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .form-container input[type="submit"]:hover {
      background-color: #34495e;
    }

    .header {
      background-color: #2c3e50;
      color: white;
      text-align: center;
      padding: 20px;
      font-size: 24px;
    }
  </style>
  <script>
    function filterBills(customerId) {
      const billDropdown = document.getElementById("bill");
      const allBills = document.querySelectorAll(".bill-option");

      // Clear existing options
      billDropdown.innerHTML = '<option value="">Select Bill</option>';

      // Loop through all bills and only display those matching the selected customer
      allBills.forEach(function (option) {
        if (option.getAttribute("data-customer-id") === customerId || customerId === "") {
          billDropdown.appendChild(option);
        }
      });
      document.getElementById("balance").value = "";
    }

  </script>
</head>
<body>
<div class="header">
  Vendor Bill Management
</div>
<div class="container">
  <div class="sidebar">
    <a href="customer">Customer</a>
    <a href="item">Items</a>
    <a href="bill">Bills</a>
    <a href="tax">Tax</a>
    <a href="payment">Payments</a>
  </div>
  <div class="main-content">
    <div class="form-container">
      <h3>${message}</h3>
      <h2>Create New Payment</h2>
      <form action="newPayment" method="post">
        <label for="customer">Customer Name:</label>
        <select id="customer" name="customerId" required onchange="filterBills(this.value);">
          <option value="">Select Customer</option>
          <%
            List<CustomerBo> customerList = (List<CustomerBo>) request.getAttribute("list");
            if (customerList != null) {
              for (CustomerBo customer : customerList) {
          %>
          <option value="<%= customer.getId() %>"><%= customer.getName() %></option>
          <%
              }
            }
          %>
        </select>

        <label for="bill">Bill:</label>
        <select id="bill" name="billId" required onchange="updateBalance();">

        <option value="">Select Bill</option>
          <%
            try {
              Class.forName("com.mysql.cj.jdbc.Driver");
              Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vendor", "root", "");
              String sql = "SELECT * FROM bill where balance >0";
              PreparedStatement ps = con.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();

              while (rs.next()) {
                int billId = rs.getInt("id");
                int customerId = rs.getInt("customerId");
                double total = rs.getDouble("amount");
                double balance = rs.getDouble("balance");
          %>
          <option class="bill-option" data-customer-id="<%= customerId %>" value="<%= billId %>">
            Bill #<%= billId %> - Amount: <%= total %> - Balance: <%= balance %>
          </option>
          <%
              }
              con.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          %>
        </select>

        <label for="amount">Amount:</label>
        <input type="number" id="amount" name="amount" step="0.01" min="0" required>

        <label for="paymentDate">Payment Date:</label>
        <input type="date" id="paymentDate" name="paymentDate" required>

        <input type="hidden" id="balance" name="balance" value="">

        <input type="submit" value="Submit">
      </form>
    </div>
  </div>
</div>
</body>
<script>
  function updateBalance() {
    const selectedBill = document.getElementById("bill");
    const selectedOption = selectedBill.options[selectedBill.selectedIndex];

    if (selectedOption) {
      const balance = selectedOption.getAttribute("data-balance");
      console.log("Selected balance:", balance); // Debugging line
      document.getElementById("balance").value = balance;
    }
  }

</script>
</html>
