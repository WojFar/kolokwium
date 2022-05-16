package edu.iis.mto.coffee;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @BeforeEach
    void setUp() {
        coffeeMachine = new CoffeeMachine(grinder, milkProvider, receipes);

    }

    @Test
    void checkIfMakeReturnsCoffeWithStatusErrorWhenTheIsNotSuchRecipeInCoffeeRecipes() {
        when(receipes.getReceipe(any())).thenReturn(null);
        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.STANDARD).build();
        
        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getStatus() != null);
    }

    @Test
    void checkIfMakeReturnsWithStatusReadyWhenEverythingIsOK() {
        Map<CoffeeSize,Integer> coffeSizes = new TreeMap<CoffeeSize,Integer>();
        coffeSizes.put(CoffeeSize.SMALL, 10);

        when(receipes.getReceipe(any())).thenReturn(CoffeeReceipe.builder().withWaterAmounts(coffeSizes).build());

        CoffeeOrder order = CoffeeOrder.builder().withType(CoffeeType.ESPRESSO).withSize(CoffeeSize.STANDARD).build();

        Coffee result = coffeeMachine.make(order);
        assertTrue(result.getStatus() == Status.READY);
    }



    



    @Test
    void itCompiles() {
        MatcherAssert.assertThat(true, equalTo(true));
    }



}
