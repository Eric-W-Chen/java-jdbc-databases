package com.pluralsight.order.dao;

import com.pluralsight.order.dto.OrderDto;
import com.pluralsight.order.dto.ParamsDto;
import com.pluralsight.order.util.Database;
import com.pluralsight.order.util.ExceptionHandler;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO to get an order
 */
public class GetOrderDao {
    private String query = "SELECT * FROM orders o WHERE o.order_id = ?";
    private Database database;

    /**
     * Constructor
     * @param database Database object
     */
    public GetOrderDao(Database database) {
        this.database = database;
    }

    /**
     * Gets an order by its ID
     * @param paramsDto Object with the parameters for the operation
     * @return Object with the main information of an order
     */
    public OrderDto getOrderById(ParamsDto paramsDto) {
        OrderDto orderDto = new OrderDto();

        try (Connection con = database.getConnection();
             PreparedStatement ps = createPreparedStatement(con, paramsDto.getOrderId());
             ResultSet rs = createResultSet(ps)
        ) {
            OrderDto order_dto = orderDto;
            while (rs.next()) {
                long orderID = rs.getLong("order_id");
                order_dto.setOrderId(orderID);
                long customerID = rs.getLong("customer_id");
                order_dto.setCustomerId(customerID);
                Date orderDate = rs.getDate("order_date");
                order_dto.setDate(orderDate);
                String status = rs.getString("order_status");
                order_dto.setStatus(status);
            }
            

        } catch (SQLException ex) {
            ExceptionHandler.handleException(ex);
        }

        return orderDto;
    }

    /**
     * Creates a PreparedStatement object to get an order
     * @param con Connnection object
     * @param orderId Order ID to set on the PreparedStatement
     * @return A PreparedStatement object
     * @throws SQLException In case of an error
     */
    private PreparedStatement createPreparedStatement(Connection con, long orderId) throws SQLException {
        PreparedStatement prepared_statement = con.prepareStatement(query);
        prepared_statement.setLong(1, orderId);
        return prepared_statement;
    }

    /**
     * Creates a ResultSet object to get the results of the query
     * @param ps PreparedStatement object to create the query
     * @return A ResultSet object
     * @throws SQLException In case of an error
     */
    private ResultSet createResultSet(PreparedStatement ps) throws SQLException {
        ResultSet result_set = ps.getResultSet();
        return result_set;
    }
}
