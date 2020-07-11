package com.kirilo.java.secure.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginServlet extends BaseServlet {

    @Override
    protected boolean createDynamicPageBody(PrintWriter out, Statement statement, HttpServletRequest req) throws SQLException {
        // Verify the username/password and retrieve the role(s)
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        StringBuilder sqlStr = new StringBuilder()
                .append("SELECT role FROM users, user_roles WHERE ")
                .append("STRCMP(users.username, '")
                .append(userName).append("') = 0 ")
//                .append("AND STRCMP(users.password, PASSWORD('")
                .append("AND STRCMP(users.password, CONCAT('*', UPPER(SHA1(UNHEX(SHA1('")
                .append(password).append("')))))) = 0 ")
                .append("AND users.username = user_roles.username");
        System.out.println(sqlStr);  // for debugging

        final ResultSet resultSet = statement.executeQuery(sqlStr.toString());
        // Check if username/password are correct
        if (!resultSet.next()) {  // empty ResultSet
            out.println("<h3>Wrong username/password!</h3>");
            out.println("<p><a href='index.html'>Back to Login Menu</a></p>");
            return false;
        } else {
            // Retrieve the roles
            List<String> roles = new ArrayList<>();
            do {
                roles.add(resultSet.getString("role"));
            } while (resultSet.next());

            // Create a new HTTPSession and save the username and roles
            // First, invalidate the session. if any
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            session = req.getSession(true);
            synchronized (session.getId()) {
                session.setAttribute("username", userName);
                session.setAttribute("roles", roles);
            }

            out.println("<p>Hello, " + userName + "!</p>");
            out.println("<p><a href='dosomething'>Do Somethings</a></p>");
            return true;
        }
    }

    @Override
    protected String createHeader() {
        return "Login";
    }

    @Override
    protected String createTitle() {
        return "Login";
    }

    @Override
    protected boolean validation(HttpServletRequest req, PrintWriter out) {
        // Retrieve and process request parameters: username and password
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        boolean hasUserName = userName != null && ((userName = userName.trim()).length() > 0);
        boolean hasPassword = password != null && ((password = password.trim()).length() > 0);
        req.setAttribute("username", userName);
        req.setAttribute("password", password);
        // Validate input request parameters
        if (!hasUserName) {
            out.println("<h3>Please Enter Your username!</h3>");
            out.println("<p><a href='index.html'>Login</a></p>");
            return false;
        } else if (!hasPassword) {
            out.println("<h3>Please Enter Your password!</h3>");
            out.println("<p><a href='index.html'>Login</a></p>");
            return false;
        }
        return true;
    }
}
