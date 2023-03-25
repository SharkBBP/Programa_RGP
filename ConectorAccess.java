package Inicio;

//import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Clase que permite conectar a la base de datos access que se encuentra en la ruta de trabajo.
 * @author AFB
 *
 */
public class ConectorAccess {
	

	private String ruta_entorno_trabajo;			//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	
	public ConectorAccess(String ruta_entorno_trabajo) {
		this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		//this.archivo = archivo;
		//this.tabla = tabla;
	}
	
	
	/**
	 * Obtiene un resultset con toda la información de la tabla que se pasa como parámetro, que está alojada en la base de datos indicada en el parámetro
	 * @param base_datos	Es el nombre de la base de datos sobre la cual queremos realizar la consulta
	 * @param tabla		Es el nombre de la tabla que contiene la información de la cual queremos obtener el ResultSet
	 * @return		Un ResultSet con toda la información de la tabla indicada como parámetro
	 */
	public ResultSet obtener_resultset(String base_datos, String tabla, ArrayList<String> campos, String campo_orden) {
		
		 ResultSet resultSet = null;
		 Iterator<String> it = campos.iterator();
		 String campos_busqueda = "";
		 int i = 0;
		 
		   try {
			   	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
	            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://" + ruta_entorno_trabajo + base_datos );//Establishing Connection
	 			   System.out.println("Connected With the database successfully");
	           while( it.hasNext()) {
	        	   if ( i == 0)
	        		  campos_busqueda.concat(it.next());
	        	   else
	        		   campos_busqueda.concat(it.next() + ", ");
	           }

	           //Using SQL SELECT Query
	           PreparedStatement preparedStatement=connection.prepareStatement("select distinct "+ campos_busqueda + " from " + tabla + " order by "+ tabla + "." + campo_orden);

	           //Creating Java ResultSet object
	           resultSet = preparedStatement.executeQuery();
	           
		   } catch (SQLException | ClassNotFoundException e) {
			   System.out.println("Error while connecting to the database");
		   }
		   
		return resultSet;
	}
	
	
	/**
	 * Obtiene un resultset de la base de datos y de la tabla que se pasa como parámetros, focalizándose en los cambos de búsqueda indicados
	 * @param base_datos	La base de datos que se va a consultar
	 * @param tabla		La tabla sobre la que se va a hacer la consulta
	 * @param campos_busqueda	Los campos que se incluyen en la consulta
	 * @param campo_orden	Sirve para indicar si el orden de los resultados se da ordenado en sentido ascendente o descendente. No está implementado
	 * @return
	 */
	public ResultSet obtener_resultset(String base_datos, String tabla, String campos_busqueda, String campo_orden) {
		
		 ResultSet resultSet = null;
		 
		   try {
			   	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
	            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://" + ruta_entorno_trabajo + base_datos );//Establishing Connection
	 			//   System.out.println("Connected With the database successfully");
	           
	           String s = "select distinct "+ campos_busqueda + " from " + tabla ; //+ " order by "+ campo_orden;
	           // String s = "select * from CausaConsecuenciaMCR";
	           //Using SQL SELECT Query
	           PreparedStatement preparedStatement=connection.prepareStatement(s );

	           //Creating Java ResultSet object
	           resultSet = preparedStatement.executeQuery();
	           
		   } catch (SQLException | ClassNotFoundException e) {
			   System.out.println("Error while connecting to the database");
		   }
		   
		return resultSet;
	}

	
	/**
	 * Devuelve un resultset introduciendo la base de datos, la tabla sobre la que se quiere hacer la consulta y la consulta en lenguaje SQL-ACCESS
	 * @param base_datos	El nombre de la base de datos sobre la que se quiere realizar la consulta
	 * @param tabla	La tabla de la base de datos sobre la cual se quiere realizar la consulta
	 * @param consulta	Consulta en lenguaje SQL-ACCESS que se quiere realizar sobre la tabla de la base de datos indicada en los parámetros anteriores.
	 * @return
	 */
	public ResultSet obtener_resultset(String base_datos, String tabla, String consulta) {
	
		 ResultSet resultSet = null;
		 
		   try {
			   	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
	            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://" + ruta_entorno_trabajo + base_datos );//Establishing Connection
	 			//   System.out.println("Connected With the database successfully");
	           
	           //Using SQL SELECT Query
	           PreparedStatement preparedStatement=connection.prepareStatement( consulta );

	           //Creating Java ResultSet object
	           resultSet = preparedStatement.executeQuery();
	           
		   } catch (SQLException | ClassNotFoundException e) {
			   System.out.println("Error while connecting to the database");
		   }
		   
		return resultSet;
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

	/*public void imprimir_result_set (ResultSet rs) {
	
	ArrayList<Array> a = new ArrayList<Array>();
	try {
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		while(rs.next()){
			for( int i = 1; i < rsmd.getColumnCount(); i++) {
				a.add(rs.getArray(i));
			}
			System.out.println( a.toString());
		  }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}*/


}
