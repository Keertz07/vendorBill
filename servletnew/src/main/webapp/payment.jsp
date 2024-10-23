<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zoho.Servletnew.bo.CustomerBo"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement"%>

<%@ page isELIgnored = "false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zoho.Servletnew.bo.PaymentBo" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
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

        .table-container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 100%;
        }

        .table-container h2 {
            margin-bottom: 20px;
            font-size: 24px;
            color: #2c3e50;
            text-align: center;
        }

        .button-container {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 15px;
        }

        .new-payment-btn {
            background-color: #2c3e50;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .new-payment-btn:hover {
            background-color: #34495e;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 18px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        table thead {
            background-color: #2c3e50;
            color: white;
        }

        table th, table td {
            padding: 12px 15px;
            text-align: left;
            border: 1px solid #ddd;
        }

        table tbody tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tbody tr:hover {
            background-color: #f1f1f1;
        }

        table th {
            background-color: #34495e;
            color: white;
            text-transform: uppercase;
        }

        .status-paid {
            color: green;
            font-weight: bold;
        }

        .status-pending {
            color: red;
            font-weight: bold;
        }

        .header {
            background-color: #2c3e50;
            color: white;
            text-align: center;
            padding: 20px;
            font-size: 24px;
        }

        /* CSS for search forms */
        .search-form {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            gap: 20px; /* Increase spacing between forms */
            flex-wrap: wrap; /* Allow wrapping for smaller screens */
        }

        .search-form label {
            font-weight: bold;
            color: #2c3e50;
        }

        .search-form input[type="text"],
        .search-form input[type="date"],
        .search-form select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            width: 200px; /* Set a fixed width for inputs */
        }

        .search-form input[type="submit"] {
            background-color: #2c3e50;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .search-form input[type="submit"]:hover {
            background-color: #34495e;
        }
    </style>
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
        <div class="table-container">
            <h3>${message}</h3>
            <h2>Customer Balance Overview</h2>
            <div class="button-container">
                <a href="newPayment" class="new-payment-btn" >New Payment</a>
            </div>
            <div class="search-form">
                <form action="searchCustomerName" method="post">
                    <label for="customer">Customer Name:</label>

                    <select id="customer" name="customerId" required onchange="this.form.submit()">
                        <option value="">Select Customer</option>
                        <%
                            List<CustomerBo> customerList = (List<CustomerBo>) request.getAttribute("customerList");
                            if (customerList != null) {
                                for (CustomerBo customer : customerList) {
                        %>
                        <option value="<%= customer.getId() %>"><%= customer.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </form>

                <form action="searchPaymentStatus" method="post">
                    <label for="paymentStatus">Payment Status:</label>
                    <select id="paymentStatus" name="paymentStatus">
                        <option value="">Select Status</option>
                        <option value="paid">Paid</option>
                        <option value="unpaid">Unpaid</option>
                        <option value="partially paid">Partially Paid</option>
                    </select>
                    <input type="submit" value="Search">
                </form>

                <form action="searchDueDate" method="post">
                    <label for="dueDate">Due Date:</label>
                    <input type="date" id="dueDate" name="dueDate">
                    <input type="submit" value="Search">
                </form>
            </div>

            <!-- Existing Table for Customer Balance Overview -->
            <table>
                <thead>
                <tr>
                    <th>S.No</th>
                    <th>Customer Name</th>
                    <th>Total</th>
                    <th>Balance</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int count = 1;
                    List<PaymentBo> payments = (List<PaymentBo>) request.getAttribute("list");
                    if (payments != null) {
                        for (PaymentBo payment : payments) {
                %>
                <tr>
                    <td><%= count++ %></td>
                    <td><%= payment.getName() %></td>
                    <td><%= payment.getTotal() %></td>
                    <td><%= payment.getBalance() %></td>
                    <td><%= payment.getStatus() %></td>
                </tr>
                <%
                        }
                    } else {
                        out.println("No details available");
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
