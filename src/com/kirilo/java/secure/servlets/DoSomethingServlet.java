package com.kirilo.java.secure.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DoSomethingServlet extends BaseServlet {

    private HttpSession session;

    @Override
    protected boolean createDynamicPageBody(PrintWriter out, Statement statement, HttpServletRequest req) throws SQLException {
        List<String> roles;
        String userName;
        synchronized (session.getId()) {
            userName = (String) session.getAttribute("username");
            roles = (List<String>) session.getAttribute("roles");
        }

        out.println("<table>");
        out.println("<tr>");
        out.println("<td>Username:</td>");
        out.println("<td>" + userName + "</td></tr>");
        out.println("<tr>");
        out.println("<td>Roles:</td>");
        out.println("<td>");
        for (String role : roles) {
            out.println(role + " ");
        }
        out.println("</td></tr>");
        out.println("<tr>");
        out.println("</table>");

        out.println("<p><a href='logout'>Logout</a></p>");
        return true;
    }

    @Override
    protected String createHeader() {
        return "Do Somethings...";
    }

    @Override
    protected String createTitle() {
        return "Do Somethings";
    }

    @Override
    protected boolean validation(HttpServletRequest req, PrintWriter out) {
        session = req.getSession(false);
        if (session == null) {
            out.println("<h3>You have not login!</h3>");
            return false;
        }
        return true;
    }
}
