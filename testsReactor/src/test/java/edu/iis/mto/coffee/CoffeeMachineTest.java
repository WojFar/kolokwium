package edu.iis.mto.coffee;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.iis.mto.coffee.machine.Coffee;
import edu.iis.mto.coffee.machine.CoffeeGrinder;
import edu.iis.mto.coffee.machine.CoffeeMachine;
import edu.iis.mto.coffee.machine.CoffeeOrder;
import edu.iis.mto.coffee.machine.GrinderException;
import edu.iis.mto.coffee.machine.MilkProvider;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {

    @Mock
    CoffeeGrinder grinder;

    @Mock
    MilkProvider milkProvider;

    @Mock
    CoffeeReceipes receipes;

    CoffeeMachine coffeeMachine;

    Map<CoffeeSize,Integer> coffeSizes;
    
    @Test
    void itCompiles() {
        MatcherAssert.assertThat(true, equalTo(true));
    }

    @BeforeEach
    void setUp() {
        coffeeMachine = new CoffeeMachine(grinder, milkProvider, receipes);

        coffeSizes = new TreeMap<CoffeeSize,Integer>();
        coffeSizes.put(CoffeeSize.SMALL, 10);
        coffeSizes.put(CoffeeSize.DOUBLE, 20);
        coffeSizes.put(CoffeeSize.STANDARD, 15);

    }

    @Test
    void checkIfMakeReturnsCoffeWithStatusErrorWhenTheIsNotSuchRecipeInCoffeeRecipes() {
        when(receipes.getReceipe(any())).thenReturn(null);
        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.STANDARD).build();
        
        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getStatus() == Status.ERROR);
    }

    @Test
    void checkIfMakeReturnsWithStatusReadyWhenEverythingIsOK() { 

        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());
        try {   //just java
            when(grinder.grind(any())).thenReturn(true);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.LATTE).withSize(CoffeeSize.STANDARD).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getStatus() == Status.READY);
    }

    @Test
    void checkIfMakeReturnsCoffeWithoutMilkWhenEverythingIsOK() { 

        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());
        try {   //just java
            when(grinder.grind(any())).thenReturn(true);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.STANDARD).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getMilkAmout() == 0);
    }

    @Test
    void checkIfMakeReturnsCoffeWithMilkWhenEverythingIsOK() {    
        final int milkAmount = 10;
        when(milkProvider.pour(milkAmount)).thenReturn(milkAmount);
        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).withMilkAmount(milkAmount).build());
        try {   //just java
            when(grinder.grind(any())).thenReturn(true);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.STANDARD).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getMilkAmout() == milkAmount);
    }


    @Test
    void checkIfMakeReturnsCoffeCorrectWaterAmountWhenEverythingIsOK(){
        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());
        try {   //just java
            when(grinder.grind(any())).thenReturn(true);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.SMALL).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getWaterAmount() == 10);
    }


    @Test
    void checkIfMakeReturnsCoffeeWithErrorIfGrinderExceptionIsThrown() {
        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());
        try {   //just java
            when(grinder.grind(any())).thenThrow(GrinderException.class);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.SMALL).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getStatus() == Status.ERROR);
    }

    @Test
    void checkIfMakeReturnsCoffeeWithMessageIfGrinderHasNoBeansExceptionIsThrown() {
        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());
        try {   //just java
            when(grinder.grind(any())).thenReturn(false);
        } catch (GrinderException e) {
            fail();
        }

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.SMALL).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getMessage() != null);
    }


    



    



}
