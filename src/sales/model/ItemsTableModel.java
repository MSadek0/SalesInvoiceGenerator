
package sales.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class ItemsTableModel extends AbstractTableModel{
    
    
    private ArrayList<Item> items;
    private String[] columns = {"No.", "Item Name", "Item Price", "Quantity", "Item Total"};

    public ItemsTableModel(ArrayList<Item> items) {
        this.items = items;
    }
public ArrayList<Item> getItems(){
    return items;
    
}
    
    @Override
    public int getRowCount() {
        return items.size();
        
    }

    @Override
    public int getColumnCount() {
        return columns.length;
        
    }

     @Override
    public String getColumnName(int column){
     return columns[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
     
        Item item = items.get(rowIndex);
        switch(columnIndex){
            case 0: return item.getInvoice().getNum();
            case 1: return item.getItem();
            case 2: return item.getPrice();
            case 3: return item.getCount();
            case 4: return item.getItemTotal();
            default : return "";
            
            
        }
    }
    
}
