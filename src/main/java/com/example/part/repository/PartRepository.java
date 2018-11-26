package com.example.part.repository;

import com.example.part.model.Part;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class PartRepository {
    private Connection connection;

    public PartRepository() {

    }

    protected void connect() throws SQLException {
        Properties props = readProperties();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connection = DriverManager.getConnection(
                    url, user, password);
        }
    }

    protected void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public List<Part> getPart() throws SQLException {
        connect();
        String querry = "SELECT * FROM part";

        PreparedStatement pstm = connection.prepareStatement(querry);

        ResultSet rs = pstm.executeQuery();
        List<Part> parts = getPartsFromResultSet(rs);

        if (rs != null) {
            rs.close();
        }
        pstm.close();
        disconnect();

        return parts;
    }

    public List<Part> getFiltered(String number,
                                  String name,
                                  String vendor,
                                  int qty,
                                  Date shippedAfter, Date shippedBefore,
                                  Date receivedAfter, Date receivedBefore) throws SQLException {
        connect();

        String querry = "SELECT * FROM part AS p WHERE p.shipped IS NULL OR p.shipped IS NOT NULL AND p.shipped BETWEEN ? AND ? INTERSECT " +
                "SELECT * FROM part AS p1 WHERE p1.receive IS NULL OR p1.receive IS NOT NULL AND p1.receive BETWEEN ? AND ? INTERSECT " +
                "SELECT * FROM part AS p2 WHERE p2.qty >= ? INTERSECT " +
                "SELECT * FROM part AS p3 WHERE lower(p3.part_number::text) LIKE ? OR p3.part_number = '' INTERSECT " +
                "SELECT * FROM part AS p4 WHERE lower(p4.part_name::text) LIKE ? OR p4.part_name = '' INTERSECT " +
                "SELECT * FROM part AS p5 WHERE  lower(p5.vendor::text) LIKE ? OR p5.vendor = '' ORDER BY part_number";

        PreparedStatement ps = connection.prepareStatement(querry);

        ps.setTimestamp(1, new Timestamp(shippedAfter.getTime()));
        ps.setTimestamp(2, new Timestamp(shippedBefore.getTime()));
        ps.setTimestamp(3, new Timestamp(receivedAfter.getTime()));
        ps.setTimestamp(4, new Timestamp(receivedBefore.getTime()));
        ps.setInt(5, qty);
        ps.setString(6, "%" + number + "%");
        ps.setString(7, "%" + name + "%");
        ps.setString(8, "%" + vendor + "%");

        ResultSet rs = ps.executeQuery();
        List<Part> parts = getPartsFromResultSet(rs);

        if (rs != null) {
            rs.close();
        }
        ps.close();
        disconnect();

        return parts;

    }

    private List<Part> getPartsFromResultSet(ResultSet rs) throws SQLException {
        List<Part> parts = new ArrayList<>();
        while (rs != null && rs.next()) {
            Part part = new Part();
            part.setNumber(rs.getString("part_number"));
            part.setName(rs.getString("part_name"));
            part.setVendor(rs.getString("vendor"));
            part.setQty(rs.getInt("qty"));
            part.setShipped(rs.getDate("shipped"));
            part.setReceive(rs.getDate("receive"));
            parts.add(part);
        }
        return parts;
    }

    //reads credentials for out Database
    private Properties readProperties() {

        Properties props = new Properties();

        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String dbConfigPath = rootPath + "db.properties";
        try {
            props.load(new FileInputStream(dbConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    /////////////////////////////////////////////////////////////////////////////
    // this option also filters, but fetching all data from db and then filters//
    /////////////////////////////////////////////////////////////////////////////
    public List<Part> filter(String number,
                             String name,
                             String vendor,
                             int qty,
                             Date shippedAfter, Date shippedBefore,
                             Date receivedAfter, Date receivedBefore)
            throws SQLException {
        List<Part> parts = getPart();

        List<Part> shippedPartsFiltered = getDatesPartsFiltered(shippedAfter, shippedBefore, parts);
        List<Part> receivedPartsFiltered = getDatesPartsFiltered(receivedAfter, receivedBefore, shippedPartsFiltered);
        List<Part> namedPartsFiltered1 = getNamedPartsFiltered(name, receivedPartsFiltered);
        List<Part> numberedPartsFiltered1 = getNumberedPartsFiltered(number, namedPartsFiltered1);
        List<Part> vendoredPartsFiltered1 = getVendoredPartsFiltered(vendor, numberedPartsFiltered1);
        List<Part> filtered = getQtyPartsFiltered(qty, vendoredPartsFiltered1);

        return filtered;
    }

    private List<Part> getVendoredPartsFiltered(String vendor, List<Part> parts) {
        if (vendor == null || vendor.equals("")) {
            return parts;
        }
        List<Part> vendored = new ArrayList<>();
        if (!parts.isEmpty()) {
            for (Part part : parts) {
                if (part.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                    vendored.add(part);
                }
            }
        }
        return vendored;
    }

    private List<Part> getQtyPartsFiltered(int qty, List<Part> parts) {
        if (qty == 0) {
            return parts;
        }
        List<Part> qtyed = new ArrayList<>();
        if (!parts.isEmpty()) {
            for (Part part : parts) {
                if (part.getQty() >= qty) {
                    qtyed.add(part);
                }
            }
        }
        return qtyed;
    }

    private List<Part> getNamedPartsFiltered(String name, List<Part> parts) {
        if (name == null || name.equals("")) {
            return parts;
        }
        List<Part> named = new ArrayList<>();
        if (!parts.isEmpty()) {
            for (Part part : parts) {
                if (part.getName().toLowerCase().contains(name.toLowerCase())) {
                    named.add(part);
                }
            }
        }
        return named;
    }

    private List<Part> getNumberedPartsFiltered(String number, List<Part> parts) {
        if (number == null || number.equals("")) {
            return parts;
        }
        List<Part> numbered = new ArrayList<>();
        if (!parts.isEmpty()) {
            for (Part part : parts) {
                String number1 = part.getNumber() == null ? "" : part.getNumber();
                if (number1.toLowerCase().contains(number.toLowerCase())) {
                    numbered.add(part);
                }
            }
        }
        return numbered;
    }

    private List<Part> getDatesPartsFiltered(Date after, Date before, List<Part> parts) {
        if (after.equals(new Date(0L)) && before.equals(new Date())) {
            return parts;
        }
        List<Part> between = new ArrayList<>();
        if (!parts.isEmpty()) {
            for (Part part : parts) {
                Date date = part.getReceive();
                if (date == null) {
                    between.add(part);
                }
                if ((date != null && date.after(after)) && date.before(before)) {
                    between.add(part);
                }
            }
        }
        return between;
    }
}
