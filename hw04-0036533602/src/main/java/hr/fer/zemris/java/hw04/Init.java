package hr.fer.zemris.java.hw04;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@WebListener
public class Init implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();

		String filePath = servletContext.getRealPath("/WEB-INF/dbsettings.properties");

		if(filePath == null)
			throw new IllegalStateException("There is no dbsettings.properties file");

		Properties properties = new Properties();
		try {
			properties.load(Files.newBufferedReader(Path.of(filePath)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		boolean validFile = properties.containsKey("host") && properties.containsKey("port") && properties.containsKey("name") && properties.containsKey("user") && properties.containsKey("password");

		if(!validFile)
			throw new RuntimeException("Invalid file. File has to contain host, port, name, user, password");

		String host = (String) properties.get("host");
		String  port = (String) properties.get("port");
		String name = (String) properties.get("name");
		String user = (String) properties.get("user");
		String password = (String) properties.get("password");


		String connectionURL = String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s",host,port,name,user,password);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		try(Connection conn = cpds.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet pollsTable = metaData.getTables(null,null,"POLLS",new String[]{"TABLE"});
			ResultSet pollOptionsTable = metaData.getTables(null,null,"POLLOPTIONS",new String[]{"TABLE"});

			if(!pollsTable.next()){
				createPollsTable(conn);
			}

			if(!pollOptionsTable.next()){
				List<String> data = getData(servletContext,"pollsOptionsDefaultValues.txt");
				List<String> data2 = getData(servletContext,"football.txt");

				List<List<String>> allData = new ArrayList<>();
				allData.add(data);
				allData.add(data2);

				createPollOptionsTable(conn,allData);
			}

			pollOptionsTable.close();
			pollsTable.close();

		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}

		sce.getServletContext().setAttribute("databasePool", cpds);
	}

	private  List<String> getData(ServletContext servletContext,String textFile) throws IOException {
		String dataPath = servletContext.getRealPath("/WEB-INF/"+textFile);
		if(dataPath == null)
			throw new RuntimeException();

		Path path = Path.of(dataPath);

		return Files.readAllLines(path);
	}

	private void createPollOptionsTable(Connection connection, List<List<String>> data) {
		System.out.println("Creating table POLL_OPTIONS");
		final String query = "CREATE TABLE PollOptions\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" optionTitle VARCHAR(100) NOT NULL,\n" +
				" optionLink VARCHAR(150) NOT NULL,\n" +
				" pollID BIGINT,\n" +
				" votesCount BIGINT,\n" +
				" FOREIGN KEY (pollID) REFERENCES Polls(id)\n" +
				")";

		final String insertQuery = "INSERT INTO POLLOPTIONS (optionTitle, optionLink,pollID,votesCount) values (?,?,?,0)";

		try(PreparedStatement preparedStatement = connection.prepareStatement(query)
		){
			int numberOfRowsAffected = preparedStatement.executeUpdate();
			System.out.println("Number of rows affected: "+ numberOfRowsAffected);
		}catch (SQLException e){
			throw new RuntimeException();
		}

		try(PreparedStatement insertPreparedStatement = connection.prepareStatement(insertQuery)){
			for (List<String> dataSet : data) {
				for (String line : dataSet) {
					String[] split = line.split(",");

					insertPreparedStatement.setString(1, split[0]);
					insertPreparedStatement.setString(2, split[1]);
					insertPreparedStatement.setInt(3, Integer.parseInt(split[2].trim()));

					insertPreparedStatement.executeUpdate();
					System.out.printf("Added data:\n\t%s\n\t%s\n\t%s\n", split[0], split[1], split[2]);
				}
			}
		}catch (SQLException e){
			throw new RuntimeException();
		}
	}

	private void createPollsTable(Connection connection) {
		System.out.println("Creating table POLLS");
		final String query = "CREATE TABLE Polls\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" title VARCHAR(150) NOT NULL,\n" +
				" message CLOB(2048) NOT NULL)";

		final String insertQuery = "INSERT INTO POLLS (title, message) values (?,?)";

		try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
			int numberOfRowsAffected = preparedStatement.executeUpdate();
			System.out.println("Number of rows affected: "+ numberOfRowsAffected);
		}catch (SQLException e){
			throw new RuntimeException();
		}

		try(PreparedStatement insertPreparedStatement = connection.prepareStatement(insertQuery)){
			insertPreparedStatement.setString(1,"Glasanje za omiljeni bend");
			insertPreparedStatement.setString(2,"Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
			insertPreparedStatement.addBatch();

			insertPreparedStatement.setString(1,"Najbolji nogometni igrač na svijeu");
			insertPreparedStatement.setString(2,"Tko je od sljedećih igrača, najbolji na svijetu");
			insertPreparedStatement.addBatch();

			insertPreparedStatement.executeBatch();
		}catch (SQLException e){
			throw new RuntimeException();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
