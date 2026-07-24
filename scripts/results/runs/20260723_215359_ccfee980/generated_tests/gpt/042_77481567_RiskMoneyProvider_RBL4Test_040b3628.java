
package de.voidnode.trading4j.moneymanagement.standard;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.voidnode.trading4j.domain.Ratio;
import de.voidnode.trading4j.domain.monetary.Money;

public class RiskMoneyProvider_RBL4Test_040b3628 {

    private RiskMoneyProvider riskMoneyProvider;

    @Before
    public void setUp() {
        riskMoneyProvider = new RiskMoneyProvider(new Ratio(0.01)); // 1% risk
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeRatio() {
        new RiskMoneyProvider(new Ratio(-0.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithGreaterThanOneRatio() {
        new RiskMoneyProvider(new Ratio(1.01));
    }

    @Test
    public void testCalculateMoneyToRiskWithZeroBalance() {
        Money balance = new Money(0, "USD");
        Money riskMoney = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(0, riskMoney.asRawValue());
    }

    @Test
    public void testCalculateMoneyToRiskWithPositiveBalance() {
        Money balance = new Money(10000, "USD");
        Money riskMoney = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(100, riskMoney.asRawValue()); // 1% of 10000
    }

    @Test
    public void testCalculateMoneyToRiskWithDifferentCurrency() {
        Money balance = new Money(5000, "EUR");
        Money riskMoney = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(50, riskMoney.asRawValue()); // 1% of 5000
        assertEquals("EUR", riskMoney.getCurrency());
    }

    @Test
    public void testCalculateMoneyToRiskWithHighBalance() {
        Money balance = new Money(1000000, "USD");
        Money riskMoney = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(10000, riskMoney.asRawValue()); // 1% of 1000000
    }
}
