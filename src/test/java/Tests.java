import config.Configuration;
import helpers.DateFormat;
import helpers.Files;
import helpers.TransactionForms;
import junit.framework.TestCase;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

@Ignore
public class Tests extends TestCase {
    private String getEnvironment() {
        return "test";
    }

    protected String getCaseNumber() {
        return "generic";
    }

    static Configuration config;
    private static WebDriver driver;

    String caseName;
    String[][] card;
    String[][] params3d;
    private String type;
    private String testOutput;

    @BeforeClass
    public static void createDriver() throws IOException {
        config = new Configuration();
        config.load();

        System.setProperty("webdriver.chrome.driver", config.environment.chromeDriver);
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    @Before
    public void before() {
        testOutput = "";
        caseName = "";
        card = null;
        params3d = null;
        type = "";
    }

    @After
    public void after() {
        DateFormat now = new DateFormat();
        String date = now.getDateInShortFormat();

        String output = date + " # " + config.params.accounts.get(caseName).description + " (" + type + ")\n" + testOutput;
        record(getCaseNumber(), output);
        inDesktop(output);
    }

    private String initMerchantSideTransaction(TransactionForms transaction) throws Exception {
        String body = transaction.init(config.getInitParams(caseName));
        assertTrue("Init failed", body.matches("^OK:.{1,45}$"));

        transaction.logText(" Init is success, enter card data to merchant side\n");
        return extractInitTransactionId(body);
    }

    private void chargeMerchantSideTransaction(TransactionForms transaction, String initTransactionId, String expectedStatus) throws Exception {
        transaction.setLink(TransactionForms.whiteFormsUrl + "charge.php");
        String[][] params = appendInitTransactionId(initTransactionId, config.getChargeParams(card));
        String body = transaction.charge(params);
        assertTrue("Charge failed", body.matches(".*Status:" + expectedStatus + ".*"));

        transaction.logText(" Not 3D, Charge is success\n");
    }

    private void charge3DMerchantSideTransaction(TransactionForms transaction, String initTransactionId) throws Exception {
        transaction.setLink(TransactionForms.whiteFormsUrl + "charge.php");
        String[][] params = appendInitTransactionId(initTransactionId, config.getChargeParams(card));
        String body = transaction.charge(params);
        assertTrue("3D link failed", body.matches("Redirect:.*"));

        transaction.perform3D(body, params3d);
        transaction.logText(" 3D, Charge is success\n");
    }

    private void completeDms(TransactionForms transaction, String initTransactionId) throws Exception {
        String[][] params = appendInitTransactionId(initTransactionId, config.getChargeHoldParams(caseName));
        String body = transaction.complete(params);
        assertTrue("Charge Hold failed", body.matches(".*Status:Success.*"));

        transaction.logText(" Charge hold is success\n");
    }

    protected void smsNot3dMerchantSide() throws Exception {
        type = "sms";
        TransactionForms transaction = new TransactionForms(driver, getEnvironment() + "_" + type);

        String initTransactionId = initMerchantSideTransaction(transaction);
        chargeMerchantSideTransaction(transaction, initTransactionId, "Success");

        testOutput = transaction.getStringFile();
    }

    protected void sms3dMerchantSide() throws Exception {
        type = "sms";
        TransactionForms transaction = new TransactionForms(driver, getEnvironment() + "_" + type);

        String initTransactionId = initMerchantSideTransaction(transaction);
        charge3DMerchantSideTransaction(transaction, initTransactionId);

        testOutput = transaction.getStringFile();
    }

    protected void dmsNot3dMerchantSide() throws Exception {
        type = "dms";
        TransactionForms transaction = new TransactionForms(driver, getEnvironment() + "_" + type);

        String initTransactionId = initMerchantSideTransaction(transaction);
        chargeMerchantSideTransaction(transaction, initTransactionId, "HoldOk");
        completeDms(transaction, initTransactionId);

        testOutput = transaction.getStringFile();
    }

    protected void dms3dMerchantSide() throws Exception {
        type = "dms";
        TransactionForms transaction = new TransactionForms(driver, getEnvironment() + "_" + type);

        String initTransactionId = initMerchantSideTransaction(transaction);
        charge3DMerchantSideTransaction(transaction, initTransactionId);
        completeDms(transaction, initTransactionId);

        testOutput = transaction.getStringFile();
    }

    private String[][] appendInitTransactionId(String initTransactionId, String[][] params) {
        String[][] initTransactionIdParam = {{"init_transaction_id", initTransactionId}};
        return ArrayUtils.addAll(params, initTransactionIdParam);
    }

    private String extractInitTransactionId(String body) {
        int startIndex = body.indexOf(':') + 1;
        int endIndex = startIndex + 40;

        return body.substring(startIndex, endIndex);
    }

    private void record(String caseNumb, String output) {
        String fileName = "log/" + caseNumb + ".log";

        Files file = new Files();
        file.write(fileName, output);
    }

    private void inDesktop(String output) {
        System.out.println(output);
    }
}


