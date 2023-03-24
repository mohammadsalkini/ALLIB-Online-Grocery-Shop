package rewe.at.codechallange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import rewe.at.codechallange.enums.ShoppingCartState;
import rewe.at.codechallange.model.Item;
import rewe.at.codechallange.model.PersonalInfo;
import rewe.at.codechallange.model.Product;
import rewe.at.codechallange.model.ShoppingCart;
import rewe.at.codechallange.repository.ProductRepository;
import rewe.at.codechallange.repository.ShoppingCartRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShoppingCartControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void testCreateShoppingCart() {
        ResponseEntity<ShoppingCart> response = restTemplate.postForEntity("/shopping-carts?productId=1&quantity=2", null, ShoppingCart.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ShoppingCart cart = response.getBody();
        assert cart != null;
        assertNotNull(cart.getId());
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddItemToShoppingCart() {
        ShoppingCart shoppingCart = prepareShoppingCart();
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        ResponseEntity<ShoppingCart> response = restTemplate.postForEntity(
                String.format("/shopping-carts/%d/items?productId=1&quantity=2", savedShoppingCart.getId()), null, ShoppingCart.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        savedShoppingCart = response.getBody();
        assert savedShoppingCart != null;
        assertEquals(1, savedShoppingCart.getItems().size());
        assertEquals(2, savedShoppingCart.getItems().get(0).getQuantity());
    }

    @Test
    void testRemoveItemFromShoppingCart() {
        Product apple = productRepository.findById(1L).orElseThrow();
        Item item = new Item(1L, apple, 2);
        ShoppingCart shoppingCart = prepareShoppingCart();
        shoppingCart.setItems(Collections.singletonList(item));
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        ResponseEntity<ShoppingCart> response = restTemplate.exchange(
                String.format("/shopping-carts/%d/items/%d", savedShoppingCart.getId(), item.getId()), HttpMethod.DELETE, null, ShoppingCart.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        savedShoppingCart = response.getBody();
        assert savedShoppingCart != null;
        assertEquals(0, savedShoppingCart.getItems().size());
    }

    @Test
    void testAddPersonalInfoToShoppingCart() {
        ShoppingCart shoppingCart = prepareShoppingCart();
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        PersonalInfo personalInfo = new PersonalInfo("FirstNameTest", "LastNameTest", "test@test.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonalInfo> request = new HttpEntity<>(personalInfo, headers);
        ResponseEntity<ShoppingCart> response = restTemplate.postForEntity(
                String.format("/shopping-carts/%d/personal-info", savedShoppingCart.getId()), request, ShoppingCart.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        savedShoppingCart = response.getBody();
        assert savedShoppingCart != null;
        assertEquals("FirstNameTest", savedShoppingCart.getFirstName());
        assertEquals("LastNameTest", savedShoppingCart.getLastName());
        assertEquals("test@test.com", savedShoppingCart.getEmail());
    }

    private static ShoppingCart prepareShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setState(ShoppingCartState.OPEN);
        return shoppingCart;
    }
}