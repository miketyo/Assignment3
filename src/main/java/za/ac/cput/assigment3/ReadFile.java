/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assigment3;

import java.io.EOFException;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 *
 * @author Mike Somelezo Tyolani (220187568)
 */
public class ReadFile 
{
    private final String stakeholderOut = "stakeholder.ser";
    
    
    FileWriter fWriter;
    PrintWriter pWriter;
    
   
    FileInputStream file;
    ObjectInputStream object;
    
    public void openFile(String filename)
    {
        try
        {
            fWriter = new FileWriter(new File(filename));
            pWriter = new PrintWriter(fWriter);
            System.out.println(filename + " created");
            
        } catch (IOException ioe)
        {
            System.exit(1);
        }
    }
    
    
    private ArrayList<Customer> customersList()
    {
        ArrayList<Customer> customers = new ArrayList<>();
        
        try
        {
            file = new FileInputStream(new File(stakeholderOut));
            object = new ObjectInputStream(file);
            
            // throws an EOFException 
            while (true)
            {
                Object obj = object.readObject();
                
                if (obj instanceof Customer)
                {
                    customers.add((Customer) obj);
                }
            }
            
        } catch (EOFException eofe)
        {
            
        } catch (IOException | ClassNotFoundException e)
        {
           System.exit(1);
            
        } finally
        {
            try
            {
                file.close();
                object.close();
                
            } catch (IOException e)
            {
            }
        }
        
        
        if (!customers.isEmpty())
        {
            
            Collections.sort(customers,
                    (Customer c1, Customer c2) -> 
                            c1.getStHolderId().compareTo(c2.getStHolderId())
            );
        }
        
        return customers;
    }
    private ArrayList<Supplier> suppliersList()
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        
        try
        {
            file = new FileInputStream(new File(stakeholderOut));
            object = new ObjectInputStream(file);
            
            
            while (true)
            {
                Object obj = object.readObject();
                
                if (obj instanceof Supplier)
                {
                    suppliers.add((Supplier) obj);
                }
            }
            
        } catch (EOFException eofe)
        {
            
        } catch (IOException | ClassNotFoundException e)
        {
        } finally
        {
            try
            {
                file.close();
                object.close();
                
            } catch (IOException e)
            {
            }
        }
        
      
        if (!suppliers.isEmpty())
        {
            
            Collections.sort(
                    suppliers, 
                    (Supplier s1, Supplier s2) -> 
                            s1.getName().compareTo(s2.getName())
            );
        }
        
        return suppliers;
    }
    
    private void writeCustomerOutFile()
    {
        String stri = "======================= CUSTOMERS =========================\n";
        String str = "%s\t%-10s\t%-10s\t%-10s\t%-10s\n";
        String st = "===========================================================\n";
        
        try
        {   
            pWriter.print(stri);
            pWriter.printf(str, "ID", "Name", "Surname", 
                    "Date Of Birth", "Age");
            pWriter.print(st);
            
            for (int i = 0; i < customersList().size(); i++)
            {   
                pWriter.printf(
                        str,
                        customersList().get(i).getStHolderId(),
                        customersList().get(i).getFirstName(),
                        customersList().get(i).getSurName(),
                        formatDate(customersList().get(i).getDateOfBirth()),
                        calculateAge(customersList().get(i).getDateOfBirth())
                );
            }
            
            pWriter.printf(
                    "\nNumber of customers who can rent: %d", 
                    canRent());
            
            pWriter.printf(
                    "\nNumber of customers who cannot rent: %d", 
                    canNotRent());
            
        } catch (Exception e)
        {
        }
    }
    
     private void writeSupplierOutFile()
    {
        String su = "======================= SUPPLIERS =========================\n";
        String sup = "%s\t%-20s\t%-10s\t%-10s\n";
        String sul = "===========================================================\n";
        
        try
        {
            pWriter.print(su);
            pWriter.printf(sup, "ID", "Name", "Prod Type",
                "Description");
            pWriter.print(sul);
            for (int i = 0; i < suppliersList().size(); i++)
            {
                pWriter.printf(
                        sup,
                        suppliersList().get(i).getStHolderId(),
                        suppliersList().get(i).getName(),
                        suppliersList().get(i).getProductType(),
                        suppliersList().get(i).getProductDescription()
                );
            }
            
        } catch (Exception e)
        {
        }
    }
    
    public void closeFile(String filename)
    {
        try
        {
            fWriter.close();
            pWriter.close();
            System.out.println(filename + " is closed");

        } catch (IOException ex)
        {
        }
    }
    private String formatDate(String dob)
    {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy", 
                Locale.ENGLISH);

        LocalDate parseDob = LocalDate.parse(dob); 

        
        return parseDob.format(formatter);
    }
    
    private int calculateAge(String dob)
    {
        LocalDate parseDob = LocalDate.parse(dob); 
        int dobYear  = parseDob.getYear();
        
        ZonedDateTime todayDate = ZonedDateTime.now();
        int currentYear = todayDate.getYear();
        
        
        return currentYear - dobYear;
    }
    
    private int canRent()
    {
        int canRent = 0;
        
        for (int i = 0; i < customersList().size(); i++)
        {
            
            if (customersList().get(i).getCanRent())
            {
                canRent += 1;
            }
        }
        
        return canRent;
    }
    
    private int canNotRent()
    {
        int canNotRent = 0;
        
        for (int i = 0; i < customersList().size(); i++)
        {
            
            if (!customersList().get(i).getCanRent())
            {
                canNotRent += 1;
            }
        }
        return canNotRent;
    }
   
    public static void main(String[] args)
    {
        ReadFile rf = new ReadFile();
        
        rf.openFile("1. customerOutFile.txt");
        rf.writeCustomerOutFile();
        rf.closeFile("2. customerOutFile.txt");
        rf.openFile("3. supplierOutFile.txt");
        rf.writeSupplierOutFile();
        rf.closeFile("4. supplierOutFile.txt");
    }
    
}

