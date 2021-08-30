package banking.repository;

import banking.model.Account;

import java.sql.*;
import java.util.ArrayList;

public class AccountRepo {
    UtilDB db;

    public AccountRepo() {
        this.db = new UtilDB();
    }

    public AccountRepo(String args) {
        this.db = new UtilDB(args);
    }

    public boolean createTable() {
        try (Connection conn = db.getConn()) {
            try (Statement statement = conn.createStatement()) {
                int result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "pin TEXT NOT NULL," +
                        "number TEXT NOT NULL," +
                        "balance INTEGER DEFAULT (0))");

                return true;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean put(Account ac) {
        try (Connection conn = db.getConn()) {
            try (Statement statement = conn.createStatement()) {
                String stmt = String.format("INSERT INTO CARD (pin, number) VALUES ('%s', '%s')", ac.getId(), ac.getCard());
                int result = statement.executeUpdate(stmt);
                return true;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void query() {
        ResultSet resultset = null;


        try (Connection conn = db.getConn()) {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM card")) {


                resultset = statement.executeQuery();
                while (resultset.next()) {
                    System.out.println(resultset.getString(2));
                    System.out.println(resultset.getString(4));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Account auth(String pin, String number) {
        ResultSet resultset = null;

        try (Connection conn = db.getConn()) {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM card WHERE pin = ? AND number = ?")) {

                statement.setString(1, pin);
                statement.setString(2, number);
                resultset = statement.executeQuery();
                if (resultset.next()) {
                    String pinAux = resultset.getString(2);
                    String numberAux = resultset.getString(3);
                    int balance = resultset.getInt(4);
                    Account ac = new Account(pin, number, balance);
                    return ac;
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Account queryByNumber(String cardNumber) {

        try (Connection conn = db.getConn()) {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM CARD WHERE number = ?")) {
                statement.setString(1, cardNumber);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String pin = rs.getString(2);
                    String number = rs.getString(3);
                    int balance = rs.getInt(4);
                    Account ac = new Account(pin, number, balance);
                    return ac;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean updateBalance(Account account, double valor) {
        try (Connection conn = db.getConn()) {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE CARD SET balance = ? WHERE number = ?")) {

                statement.setDouble(1, valor);
                statement.setString(2, account.getCard());
                int resul = statement.executeUpdate();
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean transfer(Account firstAc, Account secondAc) {
        try (Connection conn = db.getConn()) {
            conn.setAutoCommit(false);

            try (PreparedStatement updateA1 = conn.prepareStatement("UPDATE CARD SET balance = ? WHERE number = ?");
                 PreparedStatement updateA2 = conn.prepareStatement("UPDATE CARD SET balance = ? WHERE number = ?")) {

                updateA1.setInt(1, firstAc.getBalance());
                updateA1.setString(2, firstAc.getCard());
                updateA1.executeUpdate();

                updateA2.setInt(1, secondAc.getBalance());
                updateA2.setString(2, secondAc.getCard());
                updateA2.executeUpdate();

                conn.commit();

                return true;
            } catch (SQLException throwables) {
                if (conn != null) {
                    try {
                        System.err.print("Transaction is being rolled back");
                        conn.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccount(Account account) {
        try (Connection conn = db.getConn()) {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM CARD WHERE number = ?")) {

                statement.setString(1, account.getCard());

                int resul = statement.executeUpdate();
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void exit() {
        try (Connection conn = db.getConn()) {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}

