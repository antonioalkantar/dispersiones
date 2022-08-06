package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;

/** 
  * Clase que implementa un Datasource de conexiones a la BD para el uso de la aplicación.
  * El datasource es generado mediante el patrón de diseño de Singleton
  * Para compilar y ejecutar se necesitan en el classpath:
  * commons-dbcp-2.9.jar (antes 2.1)
  * 
  * commons-pool-2.3.jar (Por verificar si se requiere)
  * commons-logging-1.2.jar (Solo para ejecutar) (Por verificar si se requiere)
  * 
  * @author raul.soto 
  */
public final class PostgresDatasource {
	
	private static final Logger LOGGER = LogManager.getLogger(PostgresDatasource.class);
	
	private static PostgresDatasource postgresDatasource = null;
	private static DataSource dataSource = null;
	
	private final static String JDBC_DRIVER = "org.postgresql.Driver";
	
	private PostgresDatasource() {
		setupDataSource();
	}
	
	public static PostgresDatasource getInstance(){
		if(postgresDatasource == null){
			postgresDatasource = new PostgresDatasource();
		}
		return postgresDatasource;
	}
	
	public Connection getConnection() throws SQLException {
		if(dataSource == null) {
			setupDataSource();
		}
		Connection conn = dataSource.getConnection();
		int contador = 1;
		while(conn.isClosed() || !conn.isValid(10)) {
			LOGGER.warn("Se detectó una conexión a la BD inválida o cerrada en el pool de conexiones. Intentando obtener otra conexión.");
			((BasicDataSource)dataSource).invalidateConnection(conn);
			conn = dataSource.getConnection();
			contador++;
			if(contador >= 20) {
				try {
					Thread.sleep(1000 * 60 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return conn; //dataSource.getConnection();
	}

    private static void setupDataSource() {
    	BasicDataSource ds = new BasicDataSource();
    	ds.setDriverClassName(JDBC_DRIVER);
    	ds.setUrl(Environment.getDbUrl());
    	ds.setUsername(Environment.getDbUser());
    	ds.setPassword(Environment.getDbPassword());
    	ds.setInitialSize(10);
    	ds.setMaxTotal(100);
    	ds.setMaxIdle(3);
    	ds.setMaxWaitMillis(3000);
    	ds.setValidationQuery("select 1"); //por ejemplo para Oracle sería Select 1 from dual, para postgres es solo select 1
    	ds.setPoolPreparedStatements(true);
    	ds.setMaxOpenPreparedStatements(50);
    	ds.setMinEvictableIdleTimeMillis(1000*60*10);
    	dataSource = ds;
    	LOGGER.info("Se ha generado el Datasource: " + Environment.getDbUrl());
    }

    public void printDataSourceStats() {
    	if(dataSource != null){
    		BasicDataSource bds = (BasicDataSource) dataSource;
    		LOGGER.info("*************** Statistics Datasource ***************");
            LOGGER.info("***   NumActive: " + bds.getNumActive() +"             ***");
            LOGGER.info("***     NumIdle: " + bds.getNumIdle() +"             ***");
//            LOGGER.info("*** InitialSize: " + bds.getInitialSize() +"             ***");
//            LOGGER.info("***    MaxTotal: " + bds.getMaxTotal() +"             ***");
            LOGGER.info("*****************************************************");
    	}
    }

    public void shutdownDataSource() {
    	if(dataSource != null){
    		BasicDataSource bds = (BasicDataSource) dataSource;
            try {
				bds.close();
			} catch (SQLException e) {
				LOGGER.warn("No se pudo cerrar el Datasource de Postgres:", e);
			}
            LOGGER.info("Se ha cerrado el Datasource: " + Environment.getDbUrl());
    	}
    	
    	if(postgresDatasource != null) {
    		postgresDatasource = null;
    	}
    }
    
    public void close(ResultSet rs, Statement stmt, Connection conn){
    	if(rs != null){
    		try { rs.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar un resultset"); } 
    	}
    	if(stmt != null){
    		try { stmt.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar un statement"); } 
    	}
    	if(conn != null){
    		try { conn.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar una conexión"); }
    	}
    }
}
