package com.kirilo.java.secure.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

public class LogoutServlet extends BaseServlet{

    @Override
    protected boolean createDynamicPageBody(PrintWriter out, Statement statement, HttpServletRequest req) throws SQLException {
        final HttpSession session = req.getSession(false);
        if (session == null) {
            out.println("<h3>You have not login!</h3>");
            return false;
        } else {
            session.invalidate();
/*            session.removeAttribute("username");
            session.removeAttribute("role");*/
//            session.setMaxInactiveInterval(1);
            out.println("<p>Bye!</p>");
            out.println("<p><a href='index.html'>Login</a></p>");
        }
        return true;
    }

    @Override
    protected String createHeader() {
        return "Logout";
    }

    @Override
    protected String createTitle() {
        return "Logout";
    }

    @Override
    protected boolean validation(HttpServletRequest req, PrintWriter out) {
        return true;
    }
}
