
package externalSystem;


import customerRegister.CustomerRegister;
import printer.Printer;
import item.Item;

public class ExternalSystem {
    private Printer printer = new Printer();
  //  private CustomerRegister customerRegister = new CustomerRegister();


    public ExternalSystem(Printer printer){
        this.printer = printer;
    }

  /*  public CustomerRegister getCustomerRegister(){
        return customerRegister;
    } */




  /*  public boolean InformationSystem(int itemCost, String itemList){
        return true;
    } */


}