package mx.edu.utez.controller;

import mx.edu.utez.database.ConnectionMysql;
import mx.edu.utez.model.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {
    Connection con;
    PreparedStatement pstm;
    ResultSet rs;
    Statement statement;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> findAll(){
        List<Employee> listEmployee = new ArrayList<>();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employees.employeeNumber, employees.lastName, employees.firstName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle" +
                    " FROM employees;";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastName"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
                listEmployee.add(employee);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return listEmployee;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){ //CONSULTA POR ID
        Employee employee = new Employee();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle FROM employees WHERE employees.employeeNumber=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            if (rs.next()){
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setLastName(rs.getString("lastName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employee;
    }

    @POST
    @Path("/{id}/{lastName}/{firstName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean getEmployee(@PathParam("id") int employeeNumber, @PathParam("lastName") String lastName,@PathParam("firstName") String firstName,@PathParam("extension") String extension, @PathParam("email") String email,@PathParam("officeCode") int officeCode , @PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle ){
        boolean flag = false;
        try{
            con = ConnectionMysql.getConnection();
            pstm = con.prepareCall("INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?,?,?,?,?,?,?,?);");
            pstm.setInt(1, employeeNumber);
            pstm.setString(2, lastName);
            pstm.setString(3, firstName);
            pstm.setString(4, extension);
            pstm.setString(5, email);
            pstm.setInt(6, officeCode);
            pstm.setInt(7, reportsTo);
            pstm.setString(8, jobTitle);

            flag = pstm.executeUpdate() == 1;
        }catch(SQLException e){
            System.out.println("Error" + e.getMessage());
        }finally{
            closeConnection();
        }

        return flag;
    }

    @PUT
    @Path("/{id}/{lastName}/{firstName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEmployee(@PathParam("id") int employeeNumber, @PathParam("lastName") String lastName,@PathParam("firstName") String firstName,@PathParam("extension") String extension, @PathParam("email") String email,@PathParam("officeCode") int officeCode , @PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle){
        boolean flag = false;
        try{
            con = ConnectionMysql.getConnection();
            pstm = con.prepareCall("UPDATE employees SET employees.firstName = ?, employees.lastName = ?, employees.extension = ?, employees.email = ?, employees.officeCode = ?, employees.reportsTo = ?, employees.jobTitle = ? WHERE employees.employeeNumber = ?;");
            pstm.setString(1, lastName);
            pstm.setString(2, firstName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setInt(5, officeCode);
            pstm.setInt(6, reportsTo);
            pstm.setString(7, jobTitle);
            pstm.setInt(8, employeeNumber);

            flag = pstm.executeUpdate() == 1;
        }catch(SQLException e){
            System.out.println("Error" + e.getMessage());
        }finally{
            closeConnection();
        }

        return flag;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteEmployee(@PathParam("id") int id){ //ELIMINAR
        boolean flag = false;
        try{
            con = ConnectionMysql.getConnection();
            String queryP = "DELETE FROM employees WHERE employeeNumber = ?;";
            pstm = con.prepareStatement(queryP);
            pstm.setInt(1, id);
            flag = pstm.executeUpdate() == 1;
        }catch(SQLException e){
            System.out.println("Error" + e.getMessage());
        }finally{
            closeConnection();
        }
        return flag;
    }


    public void closeConnection() {
        try{
            if (con != null){
                con.close();
            }
            if (pstm != null){
                pstm.close();
            }
            if (rs != null){
                rs.close();
            }
            if (statement != null){
                statement.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}