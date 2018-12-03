package com.example.part.web;

import com.example.part.model.Part;
import com.example.part.repository.PartRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainServlet extends HttpServlet {
    private SimpleDateFormat SDF = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    private PartRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        repository = new PartRepository();
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        List<Part> parts = null;

        if ("filter".equals(action)) {
            try {
                String number = req.getParameter("number") == null ? "" : req.getParameter("number");
                String name = req.getParameter("name") == null ? "" : req.getParameter("name");
                String vendor = req.getParameter("vendor") == null ? "" : req.getParameter("vendor");
                int qty = req.getParameter("qty").equals("") || Integer.parseInt(req.getParameter("qty")) < 0 ?
                        0 : Integer.parseInt(req.getParameter("qty"));

                Date shippedAfter = req.getParameter("shippedAfter").equals("") ||
                        SDF.parse(req.getParameter("shippedAfter")) == null ? new Date(0L) : SDF.parse(req.getParameter("shippedAfter"));
                Date shippedBefore = req.getParameter("shippedBefore").equals("") ||
                        SDF.parse(req.getParameter("shippedBefore")) == null ? new Date() : SDF.parse(req.getParameter("shippedBefore"));
                Date receivedAfter = req.getParameter("receivedAfter").equals("") ||
                        SDF.parse(req.getParameter("receivedAfter")) == null ? new Date(0L) : SDF.parse(req.getParameter("receivedAfter"));
                Date receivedBefore = req.getParameter("receivedBefore").equals("") ||
                        SDF.parse(req.getParameter("receivedBefore")) == null ? new Date() : SDF.parse(req.getParameter("receivedBefore"));

                parts = repository.getFiltered(number, name, vendor, qty, shippedAfter, shippedBefore, receivedAfter, receivedBefore);
            } catch (ParseException | SQLException e) {
                e.printStackTrace();
            }

            req.setAttribute("parts", parts);
            req.getRequestDispatcher("/resources/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        List<Part> parts = null;
        try {
            parts = repository.getPart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("parts", parts);
        req.getRequestDispatcher("/resources/index.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
