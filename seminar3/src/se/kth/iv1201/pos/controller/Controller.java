
package se.kth.iv1201.pos.controller;


import se.kth.iv1201.pos.dbhandler.*;
import se.kth.iv1201.pos.dto.SaleInfoDto;
import se.kth.iv1201.pos.model.Item;
import se.kth.iv1201.pos.model.Observer;
import se.kth.iv1201.pos.model.PaymentController;
import se.kth.iv1201.pos.model.Sale;


/**
 * Denna klass kontrollerar andra delar i vår system
 * @author Dariush Aghadai Ghaderi, darag@kth.se
 * @author Simon Lagerqvist, simlag@kth.se
 * @version 1.9
 * @since 2018-05-06
 */

public class Controller {

    private PaymentController paymentController;
    private ExternalSystem externalSystem;
    private Sale sale;
    private Item item;
    private CustomerRegister customerRegister;
    private DiscountRules discountRules;
    private InventorySystem inventorySystem;
  //  private ItemId itemId;


    /**
     * Skapar en ny instans
     * @param externalSystem
     * @param paymentController
     */
    public Controller(ExternalSystem externalSystem, PaymentController paymentController)
    {
        this.paymentController = paymentController;
        this.externalSystem = externalSystem;

    }
    /**
     * Metoden informerar om att en ny försäljning har startas
     */
    public void startSale()
    {
        this.sale = new Sale();
    }

    /**
     * Metoden används för att slå in itemId
     * @param id items id
     * @throws InvalidItemException undantag som kastas om varan inte hittades.
     * @throws OperationFailedException undantag som kastas när operationene mislykas p.g.a. fel vid exekveringen.
     * @return returnerar det item för försäljning
     */
    public SaleInfoDto enterItemId(int id)throws InvalidItemException, OperationFailedException
    {
        try
        {
            Item item = externalSystem.getItem(id);
            if(item != null)
            {
                sale.addItem(item);
            }
            return sale.getSale();
        }
        catch (DatabaseErrorException databaseErrorException)
        {
            throw new OperationFailedException("Registraition of the following item failed: " + id,databaseErrorException);
        }
    }


    /**
     * Metoden används för att ansluta försäljningen
     * @return returnerar att försäljningen är avslutad
     */
    public SaleInfoDto endSale()
    {
        return sale.finishRegistration();
    }

    /**
     * Metoden används för att controllera kundens id och lagrar det
     * @param id kundens id
     * @return returnerar kundens id
     */
    public String checkCustomerId(int id)
    {
        this.customerRegister = new CustomerRegister();
        return customerRegister.getInfoOfCustomerId(id);
    }

    /**
     * Metoden används för rabbet för den totala kostnaden
     * @return returnerar rabbaten
     */
    public double requestDscount()
    {
        this.discountRules = new DiscountRules(sale.getSale());
        return discountRules.DiscountInPrecent();
    }



    public void addRentalObserver(Observer totalRevenueObserver)
    {
        sale.addRentalObserver(totalRevenueObserver);
    }


    public String getItemId(int id) throws InvalidItemException, OperationFailedException
    {

       // this.itemId = new ItemId();
      //  return itemId.matchItemId(id);
        try
        {
            this.inventorySystem = new InventorySystem();
            return inventorySystem.matchItemId(id).getItemName();
        }
        catch (DatabaseErrorException databaseErrorException)
        {
            throw new OperationFailedException("Registraition of the following item failed: " + id,databaseErrorException);
        }
    }




    /**
     * Metoden för för betalning med kontanter och retur lämnas
     * @param cashAmount mängden kontanter som kunden ger
     * @return kostnaden för hela köpet
     */

    public double cashPayment(int cashAmount)
    {
        double change = paymentController.cashPayment(cashAmount, sale.getSale());
        sale.notifyObservers();
        return change;
    }


}

