<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
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
        }

        .form-container label {
            display: block;
            margin-bottom: 5px;
            font-size: 16px;
        }

        .form-container input,
        .form-container select,
        .form-container textarea {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 15px; /* Adjusted margin for better spacing */
        }

        .form-container input[type="submit"] {
            background-color: #2c3e50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .header {
            background-color: #2c3e50;
            color: white;
            text-align: center;
            padding: 20px;
            font-size: 24px;
        }

        .form-container input[type="submit"]:hover {
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
        <div class="form-container">
            <h3>${message}</h3>
            <h2>Customer Information Form</h2>
            <form action="customer" method="post">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" required>

                <label for="customerType">Customer Type:</label>
                <select id="customerType" name="customerType" required>
                    <option value="Individual">Individual</option>
                    <option value="Business">Corporate</option>
                </select>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>

                <label for="phoneNo">Phone Number:</label>
                <input type="tel" id="phoneNo" name="phoneNo" pattern="[0-9]{10}" required>

                <label for="state">State:</label>
                <input type="text" id="state" name="state" required>

                <label for="city">City:</label>
                <input type="text" id="city" name="city" required>

                <label for="pincode">Pincode:</label>
                <input type="number" id="pincode" name="pincode" required>


                <input type="submit" value="Submit">
            </form>
        </div>
    </div>
</div>
</body>
</html>
