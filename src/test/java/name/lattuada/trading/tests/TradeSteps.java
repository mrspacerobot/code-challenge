package name.lattuada.trading.tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TradeSteps {

    private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
    private final RestUtility restUtility;
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private OrderDTO buyOrder;
    private OrderDTO sellOrder;

    TradeSteps() {
        restUtility = new RestUtility();
        securityMap = new HashMap<>();
        userMap = new HashMap<>();
    }

    // TODO implement: Given for "one security {string} and two users {string} and {string} exist"
    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsers(String securityName, String userName1, String userName2) {
        createUser(userName1);
        createUser(userName2);
        createSecurity(securityName);
    }

    @Given("one security {string} and three users {string} and {string} and {string} exist")
    public void oneSecurityAndThreeUsers(String securityName, String userName1, String userName2, String userName3) {
        createUser(userName1);
        createUser(userName2);
        createUser(userName3);
        createSecurity(securityName);
    }

    @When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
    @And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
    public void userPutAnOrder(String userName, String orderType, String securityName, Double price, Long quantity) {
        logger.trace("Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
                userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
        assertTrue(String.format("Unknown user \"%s\"", userName),
                userMap.containsKey(userName));
        assertTrue(String.format("Unknown security \"%s\"", securityName),
                securityMap.containsKey(securityName));
        createOrder(userName,
                EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)),
                securityName,
                price,
                quantity);
    }

    @Then("a trade occurs with the price of {double} and quantity of {long}")
    public void aTradeOccursWithThePriceOfAndQuantityOf(Double price, Long quantity) {
        logger.trace("Got price = \"{}\"; quantity = \"{}\"",
                price, quantity);
        TradeDTO trade = restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class);
        assertEquals("Price not expected", trade.getPrice(), price);
        assertEquals("Quantity not expected", trade.getQuantity(), quantity);
    }

    @Then("no trades occur")
    public void noTradesOccur() {
        assertThatThrownBy(() -> restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class)).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    private void createUser(String userName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userName);
        userDTO.setPassword(RandomStringUtils.randomAlphanumeric(64));
        UserDTO userReturned = restUtility.post("api/users",
                userDTO,
                UserDTO.class);
        userMap.put(userName, userReturned);
        logger.info("User created: {}", userReturned);
    }

    private void createSecurity(String securityName) {
        SecurityDTO securityDTO = new SecurityDTO();
        securityDTO.setName(securityName);
        SecurityDTO securityReturned = restUtility.post("api/securities",
                securityDTO,
                SecurityDTO.class);
        securityMap.put(securityName, securityReturned);
        logger.info("Security created: {}", securityReturned);
    }

    private void createOrder(String userName,
                             EOrderType orderType,
                             String securityName,
                             Double price,
                             Long quantity){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setType(orderType);
        orderDTO.setSecurityId(securityMap.get(securityName).getId());
        orderDTO.setPrice(price);
        orderDTO.setQuantity(quantity);
        orderDTO.setUserId(userMap.get(userName).getId());
        OrderDTO orderReturned = restUtility.post("api/orders",
                orderDTO,
                OrderDTO.class);
        if(orderReturned.getType() == EOrderType.BUY){
            buyOrder = orderReturned;
        }
        if(orderReturned.getType() == EOrderType.SELL){
            sellOrder = orderReturned;
        }

        logger.info("Order created: {}", orderReturned);
    }

}
