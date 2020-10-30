package pageobjects.paytmfirst;

import com.one97.paytm.appautomation.logging.Logger;
import com.one97.paytm.appautomation.utils.ActionHelper;

import org.openqa.selenium.By;

import utils.Constants;

public class PaytmFirstCardPassbookPasswordScreen {

    private static PaytmFirstCardPassbookPasswordScreen instance = new PaytmFirstCardPassbookPasswordScreen();
    private By noNetworkPopupHeader_TV = By.id("net.one97.paytm:id/w_custom_dialog_title");
    private By noNetworkPopupOk_Btn = By.id("net.one97.paytm:id/w_custom_dialog_btn_positive");
    private By enterPassword_TL = By.xpath("(//android.widget.EditText[@resource-id='password'])");
    private By payCreditCardBill_TV = By.xpath("(//android.view.View[@text='Pay Credit Card Bill'])");
    private By creditCardBillPayment_TV = By.xpath("//android.widget.TextView[@resource-id='net.one97.paytm:id/topview' and @text = 'Credit Card Bill Payment']");
    private By payBilForOtherCreditCard_TV = By.xpath("//android.widget.TextView[@resource-id='net.one97.paytm:id/footer_text' and @text = 'Pay Bill for other Credit Card']");
    private By viewOffersAndBenefits_TV = By.xpath("(//android.view.View[@text='View Offers and Benefits'])");
    private By creditCardNumber = By.xpath("(//android.view.View[@text='4381 xxxx xxxx 2072'])");
    private By updatedOn = By.xpath("(//android.view.View[contains(@text,'Last Updated on:')])");
    private By creditCardNumberIssued = By.xpath("(//android.view.View[@text='xxxx xxxx xxxx xxxx'])");

    private PaytmFirstCardPassbookPasswordScreen(){

    }
    public static PaytmFirstCardPassbookPasswordScreen getInstance(){

        Logger.logPass("User On Passbook Password Screen");

        return instance;
    }

    public String getText_NoNetworkPopup_TV() {
        String Header = ActionHelper.getText(noNetworkPopupHeader_TV);
        //String SubHeader= ActionHelper.getText(noNetworkPopupSubHeader_TV);
        return Header;
    }

    public void click_NoNetworkPopupOk_Btn() {
        ActionHelper.click(noNetworkPopupOk_Btn);
        ActionHelper.waitForPageProgress();
    }

    public boolean isDisplayed_EnterPassword_TL(){
        return ActionHelper.isPresent(enterPassword_TL);
    }

    public void enterPassword_ET(){
        ActionHelper.sendKeys(enterPassword_TL,"1234@abhiabhi");
        ActionHelper.click(By.xpath("(//android.widget.Button[@text='no Proceed Securely'])"));
        ActionHelper.waitForPageProgress();
    }

    public void click_PayCreditCardBill_TV(){
        ActionHelper.click(payCreditCardBill_TV);
    }

    public void click_ViewOffersAndBenefits(){
        ActionHelper.click(viewOffersAndBenefits_TV);
    }

    public String getText_CreditCardBillPayment_TV(){
        return ActionHelper.getText(creditCardBillPayment_TV);
    }

    public boolean isDisplayed_PayBillForOtherCreditCard_TV(){
        return ActionHelper.isPresent(payBilForOtherCreditCard_TV);
    }

    public String getText_CreditCardNumber(){
        return ActionHelper.getText(creditCardNumber);
    }

    public boolean isDisplayed_UpdatedOn(){
        return ActionHelper.isPresent(updatedOn);
    }

    public String getText_CreditCardNumberIssued(){
        return ActionHelper.getText(creditCardNumberIssued);
    }

}



