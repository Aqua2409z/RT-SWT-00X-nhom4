
package de.voidnode.trading4j.moneymanagement.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    @Test
    public void testConstructorWithValidRatio() {
        try {
            new RiskMoneyProvider(new Ratio(0.5)); // 50% risk
        } catch (IllegalArgumentException e) {
            fail("Constructor should not throw exception for valid ratio.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeRatio() {
        new RiskMoneyProvider(new Ratio(-0.1)); // Invalid ratio
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithGreaterThanOneRatio() {
        new RiskMoneyProvider(new Ratio(1.1)); // Invalid ratio
    }

    @Test
    public void testCalculateMoneyToRisk() {
        Money balance = new Money(10000, "USD"); // $10,000 balance
        Money expectedRisk = new Money(100, "USD"); // $100 risk (1% of $10,000)
        
        Money actualRisk = riskMoneyProvider.calculateMoneyToRisk(balance);
        
        assertEquals(expectedRisk, actualRisk);
    }

    @Test
    public void testCalculateMoneyToRiskWithZeroBalance() {
        Money balance = new Money(0, "USD"); // $0 balance
        Money expectedRisk = new Money(0, "USD"); // $0 risk
        
        Money actualRisk = riskMoneyProvider.calculateMoneyToRisk(balance);
        
        assertEquals(expectedRisk, actualRisk);
    }

    @Test
    public void testCalculateMoneyToRiskWithDifferentCurrency() {
        Money balance = new Money(10000, "EUR"); // €10,000 balance
        Money expectedRisk = new Money(100, "EUR"); // €100 risk (1% of €10,000)
        
        Money actualRisk = riskMoneyProvider.calculateMoneyToRisk(balance);
        
        assertEquals(expectedRisk, actualRisk);
    }
}
