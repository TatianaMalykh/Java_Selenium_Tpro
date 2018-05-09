package helpers;

import org.openqa.selenium.*;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class TransactionForms {
    public static final String whiteFormsUrl = "http://192.168.1.10/test_fpn_ppg/forms/";
    public static final String gwSandboxUrl = "http://192.168.1.80/gw2test/";

    private WebDriver driver;
    private String stringFile = "";
    private String link;
    private String transactionType;

    public TransactionForms(WebDriver webDriver, String type) {
        driver = webDriver;
        transactionType = type;
    }

    private void fillFields(String[][] parameters, String searchMode) throws Exception {
        for (String[] parameter : parameters) {
            try {
                Method method = By.class.getMethod(searchMode, String.class);
                WebElement id = driver.findElement((By) method.invoke(null, parameter[0]));

                if (!parameter[1].isEmpty()) {
                    id.clear();
                    id.sendKeys(parameter[1]);
                } else {
                    id.click();
                }
            } catch (NoSuchElementException e) {
                System.out.print("ID was not found " + parameter[0]);
                throw e;
            } catch (Exception e) {
                System.out.print("Cannot fill " + parameter[0]);
                throw e;
            }

            if (parameter[0].equals("cc")) {
                logText(parameter[0] + " " + parameter[1].substring(0, 6) + "******" + parameter[1].substring(12) + " # ");
            } else {
                logText(parameter[0] + " " + parameter[1] + " # ");
            }
        }
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void logText(String text) {
        stringFile = stringFile.concat(text);
    }

    public String getStringFile() {
        return stringFile;
    }

    public String init(String[][] parametersInit) throws Exception {
        driver.get(whiteFormsUrl + "init.php");
        WebElement trType = driver.findElement(By.id(transactionType));
        trType.click();

        fillFields(parametersInit, "id");

        Thread.sleep(2000);
        WebElement submitForm = driver.findElement(By.id("butt"));
        submitForm.click();

        Thread.sleep(2000);
        return driver.findElement(By.xpath("//body")).getText();
    }

    public String charge(String[][] parametersCharge) throws Exception {
        driver.get(link);
        WebElement trType = driver.findElement(By.id(transactionType));
        trType.click();

        fillFields(parametersCharge, "id");

        WebElement submitForm = driver.findElement(By.id("ln_submit"));
        submitForm.click();

        Thread.sleep(2000);
        return driver.findElement(By.xpath("//body")).getText();
    }

    //--------------------- GWside
    public String chargeGWside(String[][] parametersCharge) throws Exception {
        driver.get(link);

        fillFields(parametersCharge, "id");

        //WebElement submitForm = driver.findElement(By.id("butt"));
        //submitForm.click();

        Thread.sleep(2000);
        return driver.findElement(By.xpath("//body")).getText();
    }

    public void perform3D(String body, String[][] parameters3D) throws Exception {
        Pattern pattern = Pattern.compile("~");
        String redirectPart = pattern.split(body)[0];

        Pattern linkPattern = Pattern.compile("Redirect:");
        String link3D = linkPattern.split(redirectPart)[1];

        driver.get(link3D);
        fillFields(parameters3D, "name");

        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("document.login.submit.click();");
        } else {
            throw new IllegalStateException("This driver does not support JavaScript!");
        }
    }

    public String complete(String[][] parametersComplete) throws Exception {
        driver.get(whiteFormsUrl + "complete.php");
        WebElement trType = driver.findElement(By.id(transactionType));
        trType.click();

        fillFields(parametersComplete, "id");

        WebElement submitForm = driver.findElement(By.id("butt"));
        submitForm.click();

        Thread.sleep(2000);
        return driver.findElement(By.xpath("//body")).getText();
    }
}

