<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zoho.Servletnew.bo.CustomerBo"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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
            width: 100%;
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

        .inline-field {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .inline-field select,
        .inline-field input {
            flex: 1;
            margin-right: 10px;
        }

        .inline-field input:last-child {
            margin-right: 0;
        }

        .item-group {
            margin-bottom: 20px;
            border: 1px solid #ccc;
            padding: 10px;
            border-radius: 5px;
        }

        .item-group .inline-field {
            margin-bottom: 10px;
        }

        .item-group label {
            width: 100%;
            margin-bottom: 5px;
        }

        .remove-item {
            color: red;
            cursor: pointer;
            font-weight: bold;
        }

        .add-item {
            background-color: #2c3e50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .add-item:hover {
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
            <h2>Customer Information Form</h2>
            <form action="bill" method="post">
                <h3>${message}</h3>
                <label for="customer">Select Customer:</label>
                <select id="customer" name="customer" required>
                    <option value="">Select Customer</option>
                    <%
                        List<CustomerBo> customers = (List<CustomerBo>) request.getAttribute("list");
                        if(customers!=null){
                        for (CustomerBo customer : customers) {
                    %>
                    <option value="<%= customer.getId() %>"><%= customer.getName() %></option>
                    <%
                        }

                        } else {
                            out.println("<option disabled>No customers available</option>"); // Optional: Display a message if no customers are found
                        }
                    %>
                </select>
                <div class="inline-field">
                    <div>
                        <label for="billDate">Bill Date:</label>
                        <input type="date" id="billDate" name="billDate" required>
                    </div>
                    <div>
                        <label for="dueDate">Due Date:</label>
                        <input type="date" id="dueDate" name="dueDate" required>
                    </div>
                </div>
                <div>
                    <h5>${error}</h5>
                    <label for="billId">BillId:</label>
                    <input type="number" class="billId" name="billId" min="1" required>
                </div>

                <!-- Container to dynamically add item groups -->
                <div id="itemList">
                    <%-- Initial Item Group --%>
                    <div class="item-group">
                        <div class="inline-field">
                            <div>
                                <label for="item">Select Item:</label>
                                <select class="item-select" name="item" onchange="updatePriceAndTotal(this)" required>
                                    <option value="">Select Item</option>
                                    <%
                                        try {
                                            Class.forName("com.mysql.jdbc.Driver");
                                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vendor", "root", "");
                                            Statement st = con.createStatement();
                                            String sql = "SELECT * FROM items;";
                                            ResultSet rs = st.executeQuery(sql);
                                            while (rs.next()) { %>
                                    <option value="<%= rs.getInt("id") %>" data-price="<%= rs.getDouble("Sellingprice") %>">
                                        <%= rs.getString("name") %>
                                    </option>
                                    <% }
                                        con.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } %>
                                </select>
                            </div>

                            <div>
                                <label for="quantity">Quantity:</label>
                                <input type="number" class="quantity" name="quantity[]" min="1" required>
                            </div>

                            <div>
                                <label for="pricePerUnit">Price per Unit:</label>
                                <input type="number" class="pricePerUnit" name="pricePerUnit[]" step="0.01" readonly required>
                            </div>

                            <div>
                                <label for="tax">Tax:</label>
                                <select class="tax-select" name="tax[]" onchange="updateTotal(this)">
                                    <option value="">Select Tax</option>
                                    <%
                                        try {
                                            Class.forName("com.mysql.jdbc.Driver");
                                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vendor", "root", "");
                                            Statement st = con.createStatement();
                                            String sql = "SELECT * FROM tax;";
                                            ResultSet rs = st.executeQuery(sql);
                                            while (rs.next()) { %>
                                    <option value="<%= rs.getInt("rate") %>"><%= rs.getInt("rate") %></option>
                                    <% }
                                        con.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } %>
                                </select>
                            </div>

                            <div>
                                <label for="discount">Discount (%):</label>
                                <input type="number" class="discount" name="discount[]" min="0" max="100" step="0.01">
                            </div>

                            <div>
                                <label for="total">Total:</label>
                                <input type="number" class="total" step="0.01" readonly>
                            </div>
                        </div>

                        <div class="remove-item" style="display: none;" onclick="removeItem(this)">Remove</div>
                    </div>
                </div>

                <div class="inline-field">
                    <button type="button" class="add-item" onclick="addItem()">+ Add Item</button>
                </div>

                <div class="inline-field">
                    <div>
                        <label for="grandTotal">Grand Total:</label>
                        <input type="number" id="grandTotal" name="grandTotal" step="0.01" readonly>
                    </div>
                </div>

                <input type="submit" value="Submit">
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Initialize existing item groups
        document.querySelectorAll('.item-group').forEach(initializeItemGroup);
    });

    function initializeItemGroup(itemGroup) {
        attachEventListeners(itemGroup);
        updateTotal(itemGroup);
    }

    function attachEventListeners(itemGroup) {
        itemGroup.querySelectorAll('.quantity, .pricePerUnit, .tax-select, .discount').forEach((input) => {
            input.addEventListener('input', function () {
                updateTotal(itemGroup);
            });
        });

        const selectElement = itemGroup.querySelector('.item-select');
        if (selectElement) {
            selectElement.addEventListener('change', function () {
                updatePriceAndTotal(selectElement);
            });
        }
    }

    function updatePriceAndTotal(selectElement) {
        const itemGroup = selectElement.closest('.item-group');
        const pricePerUnitInput = itemGroup.querySelector('.pricePerUnit');
        const selectedOption = selectElement.options[selectElement.selectedIndex];
        const price = parseFloat(selectedOption.getAttribute('data-price')) || 0;

        pricePerUnitInput.value = price.toFixed(2);
        updateTotal(itemGroup);
    }

    function updateTotal(itemGroup) {
        const quantity = parseFloat(itemGroup.querySelector('.quantity').value) || 0;
        const pricePerUnit = parseFloat(itemGroup.querySelector('.pricePerUnit').value) || 0;
        const taxRate = parseFloat(itemGroup.querySelector('.tax-select').value) || 0;
        const discountRate = parseFloat(itemGroup.querySelector('.discount').value) || 0;

        let subTotal = quantity * pricePerUnit;
        const discountAmount = (subTotal * discountRate) / 100;
        subTotal -= discountAmount;
        const taxAmount = (subTotal * taxRate) / 100;
        const totalAmount = subTotal + taxAmount;

        itemGroup.querySelector('.total').value = totalAmount.toFixed(2);
        updateGrandTotal();
    }

    function updateGrandTotal() {
        let grandTotal = 0;
        document.querySelectorAll('.total').forEach((totalInput) => {
            grandTotal += parseFloat(totalInput.value) || 0;
        });

        document.getElementById('grandTotal').value = grandTotal.toFixed(2);
    }

    function addItem() {
        const itemGroupTemplate = document.querySelector('.item-group').cloneNode(true);
        itemGroupTemplate.querySelectorAll('input').forEach(input => input.value = '');
        itemGroupTemplate.querySelector('.remove-item').style.display = 'block';
        document.getElementById('itemList').appendChild(itemGroupTemplate);
        initializeItemGroup(itemGroupTemplate);
    }

    function removeItem(removeButton) {
        const itemGroup = removeButton.closest('.item-group');
        itemGroup.remove();
        updateGrandTotal();
    }
</script>
</body>
</html>
