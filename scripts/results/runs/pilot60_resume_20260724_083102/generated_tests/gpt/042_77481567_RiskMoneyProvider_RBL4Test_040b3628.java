
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
        Ratio ratio = new Ratio(0.02); // 2% risk
        riskMoneyProvider = new RiskMoneyProvider(ratio);
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
    public void testCalculateMoneyToRisk() {
        Money balance = new Money(10000, "USD");
        Money riskAmount = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(200, riskAmount.asRawValue()); // 2% of 10000 is 200
        assertEquals("USD", riskAmount.getCurrency());
    }

    @Test
    public void testCalculateMoneyToRiskWithZeroBalance() {
        Money balance = new Money(0, "USD");
        Money riskAmount = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(0, riskAmount.asRawValue()); // 2% of 0 is 0
        assertEquals("USD", riskAmount.getCurrency());
    }

    @Test
    public void testCalculateMoneyToRiskWithSmallBalance() {
        Money balance = new Money(500, "EUR");
        Money riskAmount = riskMoneyProvider.calculateMoneyToRisk(balance);
        assertEquals(10, riskAmount.asRawValue()); // 2% of 500 is 10
        assertEquals("EUR", riskAmount.getCurrency());
    }
}
