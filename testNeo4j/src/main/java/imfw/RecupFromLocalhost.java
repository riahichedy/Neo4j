package imfw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.neo4j.jdbc.Driver;
import org.neo4j.jdbc.Neo4jConnection;

public class RecupFromLocalhost {

	public static void main(String[] args) throws SQLException {
		Neo4jConnection connect = new Driver().connect("jdbc:neo4j://localhost:7474", new Properties());
		 
		ResultSet resultSet = connect.createStatement().
		  executeQuery("MATCH (domaine)-[:A_POUR_PAGE]->(page) RETURN domaine");
		 
		if(resultSet.next()) {
		    Map<String, Object> e = (Map<String, Object>) resultSet.getObject("domaine");
		    System.out.println(e.toString());
		}
	}
}
