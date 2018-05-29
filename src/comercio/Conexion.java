package comercio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class Conexion {
    
    private final Connection conexion;
    private final Statement prep;
    static ResultSet rs;
    static ResultSet rs2;
    static ResultSet rs3;

    // Constructor que crea la conexión con la BBDD
    
    public Conexion(String ruta) throws SQLException {
        
        conexion = DriverManager.getConnection("jdbc:sqlite:" + ruta);
        prep = conexion.createStatement();
    }

    // Metodo que carga los datos de la base y en este caso los muestra por una Tabla del Neatbeans
    
    public void btnCargar(DefaultTableModel modelo, String nombre) {

        // Limpia la tabla
        modelo.setColumnCount(0);
        modelo.setRowCount(0);

        try {

            switch (nombre) {
                case "Producto":
                    ResultSet rs = prep.executeQuery("select * from ReferenciaProducto");

                    modelo.addColumn("Ref Producto");
                    modelo.addColumn("Nombre Producto");
                    modelo.addColumn("Ref Precio");
                    // Recorremos el Array y vamos introduciendo los datos en la tabla
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
                    } break;
                case "Ventas":
                    rs = prep.executeQuery("select * from Ventas");

                    modelo.addColumn("Ref Producto");
                    modelo.addColumn("Nota");
                    modelo.addColumn("Referencia");
                    // Recorremos el Array y vamos introduciendo los datos en la tabla
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getString(1), rs.getInt(2), rs.getString(3)});
                    } break;
                case "Precio":
                    rs = prep.executeQuery("select * from Precio");

                    modelo.addColumn("Ref Precio");
                    modelo.addColumn("Precio");

                    // Recorremos el Array y vamos introduciendo los datos en la tabla
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getString(1), rs.getInt(2)});
                    } break;
            }

        } catch (SQLException ex) {}
    }

    public void factura(DefaultTableModel modelo, String pedido) {
                
        // Limpia la tabla
        
        modelo.setColumnCount(0);
        modelo.setRowCount(0);
        
        try {
            rs = prep.executeQuery("select * from Ventas where nVenta= " + pedido + "");
            rs2 = prep.executeQuery("select * from ReferenciaProducto where refProducto in(select refProducto from Ventas where nVenta= " + pedido + ")");
            rs3 = prep.executeQuery("select * from Precio where refPrecio in(select refPrecio from ReferenciaProducto where refProducto in(select refProducto from Ventas where nVenta= " + pedido + "))");
            
            int Cantidad = rs.getInt(3);
            int Pt = rs3.getInt(2);
            int PrecioTotal = Cantidad * Pt;
            
            modelo.addColumn("NumVenta");
            modelo.addColumn("Nombre Producto");
            modelo.addColumn("Precio");
            
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt(2), rs2.getString(2), PrecioTotal});
            }
            
        } catch (SQLException ex) {}
    }

}
