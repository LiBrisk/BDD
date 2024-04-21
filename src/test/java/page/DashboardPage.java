package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";
    private static SelenideElement heading = $("[data-test-id=dashboard]");
    private static SelenideElement personalAccount = $("[data-test-id=dashboard]");
    private static SelenideElement refreshButton = $("data-test-id=action-reload");
    private static ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String text = cards.findBy(text(cardInfo.getCardNumber().substring(12, 16))).getText();
        return getBalanceFromText(text);
    }

    public int getBalanceFromText(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferMoney cardToTransfer(DataHelper.CardInfo info) {
        cards.findBy(text(info.getCardNumber().substring(12, 16))).$("button").click();
        return new TransferMoney();
    }
}
