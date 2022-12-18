
package sales.controller;

import static java.awt.SystemColor.text;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sales.model.Invoice;
import sales.model.InvoicesTableModel;
import sales.model.Item;
import sales.model.ItemsTableModel;
import sales.view.InvFrame;
import sales.view.InvoiceDialog;
import sales.view.ItemDialog;


public class Controller implements ActionListener, ListSelectionListener {
    
    private InvFrame frame;
    private InvoiceDialog invoiceDialog;
    private ItemDialog itemDialog;
       
    public Controller(InvFrame frame){
        this.frame = frame;
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        System.out.println("Action");
        String actionCommand = e.getActionCommand();
        switch(actionCommand){
            case "Load File" -> {
            try {
                loadFile();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            case "Save File" -> {
                saveFile();
            }
            case "Create New Invoice" -> {
                createNewInvoice();  
            }
            case "Delete Invoice" -> {
                deleteInvoice();
            }
            case "Create New Item" -> {
                createNewItem();
            }
            case "Cancel Item" -> {
                cancelItem();
            }
            case "createInvoiceCancel" -> {
                createInvoiceCancel(); 
            }
       
            case "createInvoiceOK" -> {
                createInvoiceOK();
            }
                
            case "createItemCancel" -> {
                createItemCancel(); 
            }
       
            case "createItemOK" -> {
                createItemOK();
            }    
         
        }
        
        
     }

    
        @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1){
        Invoice currentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumberLabel().setText(" "+currentInvoice.getNum());
        frame.getInvoiceDateLabel().setText(" "+currentInvoice.getDate());
        frame.getCustomerNameLabel().setText(" "+currentInvoice.getCustomer());
        frame.getInvoiceTotalAmountLabel().setText(" "+currentInvoice.getInvoiceTotal());
        ItemsTableModel itemsTableModel = new ItemsTableModel(currentInvoice.getItems());
        frame.getItemTable().setModel(itemsTableModel);
        itemsTableModel.fireTableDataChanged();
        }
    }
    
    private void loadFile() throws IOException {
        JFileChooser fc = new JFileChooser();
        try{
            
        int result = fc.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
            
            ArrayList<Invoice> invoicesArray = new ArrayList<>();
            
            for(String headerLine : headerLines){
                try{
                String[] headerFields = headerLine.split(",");
                int invoiceNumber = Integer.parseInt(headerFields[0]);
                String invoiceDate = headerFields[1];
                String customerName = headerFields[2];
                
                Invoice invoice = new Invoice(invoiceNumber, invoiceDate, customerName);
                invoicesArray.add(invoice);
                }catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error in Format","Error", JOptionPane.ERROR_MESSAGE);
 
                }
            }
System.out.println("Check Point1");
         result = fc.showOpenDialog(frame);
         if(result == JFileChooser.APPROVE_OPTION){
             File itemFile = fc.getSelectedFile();
                Path itemPath = Paths.get(itemFile.getAbsolutePath());
             List<String> itemLines;
                itemLines = Files.readAllLines(itemPath);
System.out.println("Check Point2");
             for (String itemLine : itemLines){
                 try {
                 String itemParts[] = itemLine.split(",");
                 int invoiceNumber = Integer.parseInt(itemParts[0]);
                 String itemName = itemParts[1];
                 double itemPrice = Double.parseDouble(itemParts[2]);
                 int count = Integer.parseInt(itemParts[3]);
                 Invoice inv = null;
                 for (Invoice invoice : invoicesArray){
                     if(invoice.getNum() == invoiceNumber){
                         inv = invoice;
                        break;  
                 
                     }
                         
                }
                Item item = new Item(itemName, itemPrice, count, inv);
                inv.getItems().add(item);
                 } catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error in Format","Error", JOptionPane.ERROR_MESSAGE);
             }
             }
             System.out.println("Check Point3");             
        }
         
         frame.setInvoices(invoicesArray);
         
         InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
         frame.setInvoicesTableModel(invoicesTableModel);
         frame.getInvoiceTable().setModel(invoicesTableModel);
         frame.getInvoicesTableModel().fireTableDataChanged();
         
         
        }
        }catch (IOException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Cannot Read File","Error", JOptionPane.ERROR_MESSAGE);
                
                
                }
            
                               
    }

        private void saveFile() {
            ArrayList<Invoice> invoices = frame.getInvoices();
            String headers = "";
            String items = "";
            for (Invoice invoice : invoices){
                String invCSV = invoice.getAsCSV();
                headers += invCSV;
                headers += "\n";
                
                for (Item item : invoice.getItems()){
                    String itemCSV = item.getAsCSV();
                    items += itemCSV;
                    items += "\n";
                    
                }
                
            }
    try {      
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            FileWriter hfw = new FileWriter (headerFile);
            hfw.write(headers);
            hfw.flush();
            hfw.close();
            
            result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File itemFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter (itemFile);
                lfw.write(items);
                lfw.flush();
                lfw.close();
            }
        }
        
    } catch (Exception ex){
        
    }       
    }
        
    private void createNewInvoice() {
        invoiceDialog = new InvoiceDialog(frame);
        invoiceDialog.setVisible(true);
        
       }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
         if (selectedRow != -1) {
             frame.getInvoices().remove(selectedRow);
             frame.getInvoicesTableModel().fireTableDataChanged();
         }
        }

    private void createNewItem() {
        itemDialog = new ItemDialog(frame);
        itemDialog.setVisible(true);
        
         }

    private void cancelItem() {
        int selectedInv = frame.getInvoiceTable().getSelectedRow();
        int selectedRow = frame.getItemTable().getSelectedRow();
        
         if (selectedInv != -1 && selectedRow != -1) {
             Invoice invoice = frame.getInvoices().get(selectedInv);
             invoice.getItems().remove(selectedRow);
            ItemsTableModel itemsTableModel = new ItemsTableModel(invoice.getItems());
            frame.getItemTable().setModel(itemsTableModel);
            itemsTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
            
         }
      }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try{
           String[] dateParts = date.split("-");
           if (dateParts.length < 3){
              JOptionPane.showMessageDialog(frame, "Wrong Date Format", "Error", JOptionPane.ERROR_MESSAGE);
           
           }else {
               int day = Integer.parseInt(dateParts[0]);
               int month = Integer.parseInt(dateParts[1]);
               int year = Integer.parseInt(dateParts[2]);
               if (day > 31 || month > 12){
                  JOptionPane.showMessageDialog(frame, "Wrong Date Format", "Error", JOptionPane.ERROR_MESSAGE);
                
               }else{
           Invoice invoice = new Invoice(num, date, customer);
        frame.getInvoices().add(invoice);
        frame.getInvoicesTableModel().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
           }
           }
           }catch (Exception ex){
        JOptionPane.showMessageDialog(frame, "Wrong Date Format", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }    
    private void createItemCancel() {
        itemDialog.setVisible(false);
        itemDialog.dispose();
        itemDialog = null;
        
    }

    private void createItemOK() {
        String item1 = itemDialog.getItemNameField().getText();
        String countStr = itemDialog.getItemCountField().getText();
        String priceStr = itemDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1){
            Invoice invoice = frame.getInvoices().get(selectedInvoice);
            Item item = new Item(item1, price, count, invoice);
            invoice.getItems().add(item);
            ItemsTableModel itemsTableModel = (ItemsTableModel) frame.getItemTable().getModel();
            itemsTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
            
        }
        
        itemDialog.setVisible(false);
        itemDialog.dispose();
        itemDialog = null;
    }


}
