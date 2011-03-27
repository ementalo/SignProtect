package com.ementalo.signprotect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.block.Block;

public class SignProtectData {

	static String SQLdriver = SignProtect.SQLdriver;
	static String SQLusername = SignProtect.SQLusername;
	static String SQLpassword = SignProtect.SQLpassword;
	static String SQLdb = SignProtect.SQLdb;
	static Logger log = Logger.getLogger("Minecraft");
	static String SignProtect_TABLE = "CREATE TABLE IF NOT EXISTS `SignProtect` "
			+ "(`id` int(11) NOT NULL AUTO_INCREMENT,`playerName` varchar(150) NOT NULL,"
			+ "`x` int(11) NOT NULL," + "`y` int(11) NOT NULL,"
			+ "`z` int(11) NOT NULL," + "PRIMARY KEY (`id`)"
			+ ") ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=latin1";
	static String SignProtect_SQLITE = "CREATE TABLE IF NOT EXISTS SignProtect (id INTEGER PRIMARY KEY, playerName TEXT, x NUMERIC, y NUMERIC, z NUMERIC)";

	public String formatCoords(int x, int y, int z) {
		return x + "," + y + "," + z;
	}

	public void insertSignProtectIntoDb(String playerName, int x, int y, int z) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			if (SignProtect.dataType.contentEquals("mysql")) {
				Class.forName(SQLdriver);
				conn = DriverManager.getConnection(SQLdb, SQLusername,
						SQLpassword);
			} else {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(SignProtect.sqlIte);
			}

			ps = conn
					.prepareStatement("INSERT INTO SignProtect (playerName, x, y, z)"
							+ "VALUES (?,?,?,?)");
			ps.setString(1, playerName);
			ps.setInt(2, x);
			ps.setInt(3, y);
			ps.setInt(4, z);
			ps.executeUpdate();

		} catch (SQLException ex) {
			log.log(Level.SEVERE,
					"[SignProtect] Unable to add protection into SQL", ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "[SignProtect] Class not found", e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				log.log(Level.SEVERE,
						"[SignProtect] Could not close connection to SQL", ex);
			}
		}
	}

	public boolean canDestroySign(String playerName, Block block) {

		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();

		int rowCount = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (SignProtect.dataType.contentEquals("mysql")) {
				Class.forName(SQLdriver);
				conn = DriverManager.getConnection(SQLdb, SQLusername,
						SQLpassword);
			} else {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(SignProtect.sqlIte);
			}
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("SELECT COUNT(*) from SignProtect where x = ? and y = ? and z = ? limit 10"); // ps.setString(1,
																													// playerName);
			ps.setInt(1, x);
			ps.setInt(2, y);
			ps.setInt(3, z);
			rs = ps.executeQuery();
			rs.next();
			rowCount = rs.getInt(1);
			rs.close();
			ps.close();

			if (rowCount == 0) {

				return true;
			} else {

				ps = conn
						.prepareStatement("SELECT count(*) from SignProtect where playerName =? and  x = ? and y = ? and z = ? limit 10");
				ps.setString(1, playerName);
				ps.setInt(2, x);
				ps.setInt(3, y);
				ps.setInt(4, z);
				rs = ps.executeQuery();
				rs.next();
				rowCount = rs.getInt(1);

				if (rowCount == 0) {
					return false;
				}

			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE,
					"[SignProtect] Unable to query SignProtection", ex);
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"[SignProtect] Unable to query SignProtection", e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				log.log(Level.SEVERE,
						"[SignProtection] Could not close connection to SQL",
						ex);
			}

		}

		return true;
	}

	public void CreateSqlTable() {
		Connection conn = null;
		Statement st = null;

		try {

			if (SignProtect.dataType.contentEquals("mysql")) {
				Class.forName(SQLdriver);
				conn = DriverManager.getConnection(SQLdb, SQLusername,
						SQLpassword);
			} else {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(SignProtect.sqlIte);
			}

			st = conn.createStatement();

			if (SignProtect.dataType.contentEquals("mysql")) {

				st.executeUpdate(SignProtect_TABLE);
			} else {
				st.executeUpdate(SignProtect_SQLITE);
			}
		} catch (SQLException s) {
			log.log(Level.SEVERE, "[SignProtect] Could not create table for : "
					+ SignProtect.dataType);

		} catch (ClassNotFoundException ex) {
			log.log(Level.SEVERE, "[SignProtect] Could not find driver for :"
					+ SignProtect.dataType);

		} catch (Exception e) {
			log.log(Level.SEVERE,
					"[SignProtect} Unexpected error occured whilst creating table",
					e);
		}

		finally {
			try {
				if (conn != null && !conn.isClosed()) {
					try {
						conn.close();
					} catch (SQLException e) {
						log.log(Level.SEVERE,
								"[SignProtect] Unexpected error occured whilst closing the connection",
								e);
					}

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getBlockOwner(String playerName, Block block) {

		String returnPlayerName = null;
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (SignProtect.dataType.contentEquals("mysql")) {
				Class.forName(SQLdriver);
				conn = DriverManager.getConnection(SQLdb, SQLusername,
						SQLpassword);
			} else {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(SignProtect.sqlIte);
			}
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("SELECT playerName from SignProtect where x = ? and y = ? and z = ? limit 10"); // ps.setString(1,
																														// playerName);
			ps.setInt(1, x);
			ps.setInt(2, y);
			ps.setInt(3, z);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnPlayerName = rs.getString("playerName");
			}
			rs.close();
			ps.close();

		} catch (SQLException ex) {
			log.log(Level.SEVERE,
					"[SignProtect] Unable to query SignProtection", ex);
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"[SignProtect] Unable to query SignProtection", e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				log.log(Level.SEVERE,
						"[SignProtection] Could not close connection to SQL",
						ex);
			}

		}
		return returnPlayerName;
	}

	// later release
	public void RemoveProtectionFromDB(Block block) {
		try {

			Connection conn = null;
			if (SignProtect.dataType.contentEquals("mysql")) {
				Class.forName(SQLdriver);
				conn = DriverManager.getConnection(SQLdb, SQLusername,
						SQLpassword);
			} else {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(SignProtect.sqlIte);
			}

			PreparedStatement ps = null;

			try {
				ps = conn
						.prepareStatement("DELETE FROM SignProtect WHERE x=? and y=? and z=?");
				ps.setInt(1, block.getX());
				ps.setInt(2, block.getY());
				ps.setInt(3, block.getZ());
				ps.executeUpdate();

			} catch (SQLException ex) {
				log.log(Level.WARNING,
						"[SignProtect] Could not delete block data from database",
						ex);
			} finally {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (ps != null) {
					ps.close();
				}
			}

		} catch (Exception e) {
			log.log(Level.SEVERE,
					" [SignProtect] Exception occured whilst trying to delete data from sql",
					e);
		}

	}

}
