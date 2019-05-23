package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	//dato un autore trovo i coautori
	public List<Author> listaCoautori(int id){
		
		List<Author> result = new ArrayList<Author>();
		final String sql = "SELECT distinct C2.authorid, a.firstname, a.lastname " + 
				"FROM creator C1, creator C2, author a " + 
				"WHERE C1.eprintid = C2.eprintid " + 
				"AND C1.authorid <> C2.authorid " + 
				"AND a.id = C2.authorid " + 
				"AND C1.authorid = ?";
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author author = new Author(rs.getInt("authorid"), rs.getString("a.firstname"), rs.getString("a.lastname"));
				result.add(author);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
	
	public List<Author> listAutori(Map<Integer, Author> idMap){
		
		List<Author> result = new ArrayList<Author>();
		final String sql = "SELECT * FROM author a";
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(idMap.get(rs.getInt("id"))==null) {
					Author author = new Author(rs.getInt("id"), rs.getString("a.firstname"), rs.getString("a.lastname"));	
					idMap.put(author.getId(), author);
					result.add(author);
				}
				else {
					result.add(idMap.get(rs.getInt("id")));
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper trovaArticoliTraDueAutori(int id1, int id2) { 
		String sql = "SELECT DISTINCT c1.eprintid " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.eprintid = c2.eprintid " + 
				"AND c1.authorid = ? " + 
				"AND c2.authorid = ? " + 
				"LIMIT 1";
		
			try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id1);
			st.setInt(2, id2);
			
			ResultSet rs = st.executeQuery();
			Paper paper = null;
			if(rs.next())
				 paper = this.getArticolo(rs.getInt("c1.eprintid"));

			conn.close();
			return paper;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<String> ritornaAutori(){
		
		List<String> result = new ArrayList<String>();
		final String sql = "SELECT * FROM author";
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery(); 
			
			while(rs.next()) {
				result.add(rs.getInt(1)+"-"+rs.getString(2)+" "+rs.getString(3));
			}
			
			conn.close();
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
	
}