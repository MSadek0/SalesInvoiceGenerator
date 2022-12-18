
package sales.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Invoice {
    private int num;
    private String date;
    private String customer;
    private ArrayList<Item> items;
    private DateFormat dateFt = new SimpleDateFormat("dd-MM-yyyy");
      
    

    public Invoice() {
    }

    public Invoice(int num, String date, String customer) {
        this.num = num;
        this.date = date;
        this.customer = customer;
        this.items = new ArrayList();
    }
    
    public double getInvoiceTotal(){
        double total = 0.0;
        for (Item item : getItems ()) {
            total += item.getItemTotal();
        }
        return total;
    }
    
    public ArrayList<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    @Override
    public String toString(){
        return "Invoice" + "num=" + num + ", date=" + date + ", customer=" + customer + ')';
    }
    
    public String getAsCSV(){
        
        return num + "," + date + "," + customer;
        
    }        
    public Object getLines() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

  
    
    
    
}
