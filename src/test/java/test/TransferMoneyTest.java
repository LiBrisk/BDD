package test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import data.DataHelper;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.*;


public class TransferMoneyTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Order(1)
    @Test()
    @DisplayName("ValidTransferFromFirstToSecond")
    void shouldTransferMoneyFromFirstToSecond() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getPossibleAmount(startingBalance1);
        var moneyTransferPage = dashboardPage.cardToTransfer(secondCard);
        moneyTransferPage.makeValidTransfer(firstCard, String.valueOf(amount));
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1 - amount, actualBalance1);
        Assertions.assertEquals(startingBalance2 + amount, actualBalance2);
    }

    @DisplayName("ValidTransferFromSecondToFirst")
    @Order(2)
    @Test
    void shouldTransferMoneyFromSecondToFirst() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getPossibleAmount(startingBalance2);
        var moneyTransferPage = dashboardPage.cardToTransfer(firstCard);
        moneyTransferPage.makeValidTransfer(secondCard, String.valueOf(amount));
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1 + amount, actualBalance1);
        Assertions.assertEquals(startingBalance2 - amount, actualBalance2);
    }

    @DisplayName("From2to2NoTransfer")
    @Order(3)
    @Test
    void shouldTransferMoneyFromSecondToSecond() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getPossibleAmount(startingBalance2);
        var moneyTransferPage = dashboardPage.cardToTransfer(secondCard);
        moneyTransferPage.transferMoney(secondCard, String.valueOf(amount));
        moneyTransferPage.depositError("Перевод средств невозможен");
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1, actualBalance1);
        Assertions.assertEquals(startingBalance2, actualBalance2);
    }

    @DisplayName("From1to1NoTransfer")
    @Order(4)
    @Test
    void shouldTransferMoneyFromFirstToFirst() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getPossibleAmount(startingBalance1);
        var moneyTransferPage = dashboardPage.cardToTransfer(firstCard);
        moneyTransferPage.transferMoney(firstCard, String.valueOf(amount));
        moneyTransferPage.depositError("Перевод средств невозможен");
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1, actualBalance1);
        Assertions.assertEquals(startingBalance2, actualBalance2);
    }

    @DisplayName("TransferFromFirst_SumMoreThanBalance")
    @Order(5)
    @Test
    void shouldTransferMoneyFromFirstToSecondNegative() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getImpossibleAmount(startingBalance1);
        var moneyTransferPage = dashboardPage.cardToTransfer(secondCard);
        moneyTransferPage.transferMoney(firstCard, String.valueOf(amount));
        moneyTransferPage.depositError("Перевод невозможен. Недостаточно средств");
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1, actualBalance1);
        Assertions.assertEquals(startingBalance2, actualBalance2);
    }

    @DisplayName("TransferFromSecond_SumMoreThanBalance")
    @Order(6)
    @Test
    void shouldTransferMoneyFromSecondToFirstNegative() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getCardNumber1();
        var startingBalance1 = dashboardPage.getCardBalance(firstCard);
        var secondCard = DataHelper.getCardNumber2();
        var startingBalance2 = dashboardPage.getCardBalance(secondCard);
        var amount = DataHelper.getImpossibleAmount(startingBalance2);
        var moneyTransferPage = dashboardPage.cardToTransfer(firstCard);
        moneyTransferPage.transferMoney(secondCard, String.valueOf(amount));
        moneyTransferPage.depositError("" + "Перевод невозможен. Недостаточно средств");
        var actualBalance1 = dashboardPage.getCardBalance(firstCard);
        var actualBalance2 = dashboardPage.getCardBalance(secondCard);
        Assertions.assertEquals(startingBalance1, actualBalance1);
        Assertions.assertEquals(startingBalance2, actualBalance2);
    }
}
