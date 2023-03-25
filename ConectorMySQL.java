package Inicio;


/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Conexion_BD {
	
  String MySQLURL = "jdbc:mysql://localhost:3306/web?useSSL=false";
  String databseUserName = "root";
  String databasePassword = "C0nf1t7d0!";
  
  Connection con = null;
  
  try {
	  con = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
     if (con != null) {
        System.out.println("Database connection is successful !!!!");
     }
  } catch (Exception e) {
     e.printStackTrace();
  }
}*/


import java.sql.*;

/**
 * The SimpleConnection class is a command line application that accepts 
 * the following command line:
 * java SimpleConnection DRIVER URL UID PASSWORD 
 * If the URL fits the specified driver, it will then load the driver and
 * get a connection.
 */
public class ConectorMySQL {
	
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 
    Connection connection = null;

    String MySQLURL = "jdbc:mysql://localhost:3306/rgp1";
    String databaseUserName = "root";
    String databasePassword = "C0nf1t7d0!";
    
    public ConectorMySQL() {
    }

    public void conectar() {
         
        try {

        connection = DriverManager.getConnection(MySQLURL, databaseUserName, databasePassword);
        System.out.println("Existo");
      }
      catch( SQLException e ) {
        e.printStackTrace( );
      }
      finally {
        if( connection != null ) {
            try { connection.close( ); }
            catch( SQLException e ) {
              e.printStackTrace( ); 
            }
        }
      }
    }
    
    
	/**
	 * Devuelve un StringBuffer con los errores generados durante la ejecución de esta clase
	 * @return un StringBuffer con los errores generados durante la ejecución de esta clase
	 */
	public StringBuffer imprimirErrores() {

		return this.logErrores;
	}
	
	
	/**
	 * Escribe los errores correspondientes a la ejecución de esta clase en el log de errores
	 * @param error El error lógico que se ha producido al ejecutar esta clase 
	 */
	public void escribirError( String error) {
		this.logErrores.append(error + "\n");
	}

}