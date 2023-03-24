package rewe.at.codechallange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewe.at.codechallange.enums.ShoppingCartState;
import rewe.at.codechallange.exception.ProductException;
import rewe.at.codechallange.exception.ShoppingCartException;
import rewe.at.codechallange.model.Item;
import rewe.at.codechallange.model.PersonalInfo;
import rewe.at.codechallange.model.Product;
import rewe.at.codechallange.model.ShoppingCart;
import rewe.at.codechallange.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    private final static Long PRODUCT_ID = 1L;
    private final static Long SHOPPING_CART_ID = 1L;
    private final static Long ITEM_ID = 1L;
    private final static Integer QUANTITY_OF_PRODUCT = 2;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductServiceImpl productService;

    @Test
    void shouldCreateShoppingCart() {
        prepareProduct();

        shoppingCartService.createShoppingCart(PRODUCT_ID, QUANTITY_OF_PRODUCT);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldNotCreateShoppingCartWhenNoProductFound() {
        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> shoppingCartService.createShoppingCart(PRODUCT_ID, QUANTITY_OF_PRODUCT), "Unable to find the product");
    }

    @Test
    void shouldAddItemToShoppingCartWhenItemNotExist() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        prepareProduct();

        shoppingCartService.addItemToShoppingCart(SHOPPING_CART_ID, PRODUCT_ID, QUANTITY_OF_PRODUCT);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldAddItemToShoppingCart() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        prepareProduct();

        shoppingCartService.addItemToShoppingCart(SHOPPING_CART_ID, PRODUCT_ID, QUANTITY_OF_PRODUCT);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldNotAddItemToShoppingCartWhenNoProductFound() {
        prepareShoppingCart(ShoppingCartState.OPEN);

        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> shoppingCartService.addItemToShoppingCart(SHOPPING_CART_ID, PRODUCT_ID, QUANTITY_OF_PRODUCT), "Unable to find the product");
    }

    @Test
    void shouldRemoveItemFromShoppingCart() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        shoppingCartService.removeItemFromShoppingCart(SHOPPING_CART_ID, ITEM_ID);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldAddPersonalInfoToShoppingCart() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        PersonalInfo personalInfo = new PersonalInfo("FirstName", "LastName", "test@test.com");
        shoppingCartService.addPersonalInfoToShoppingCart(SHOPPING_CART_ID, personalInfo);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldCheckoutShoppingCart() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        shoppingCartService.checkoutShoppingCart(SHOPPING_CART_ID);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void shouldRemovePersonalInfoFromShoppingCart() {
        prepareShoppingCart(ShoppingCartState.OPEN);
        shoppingCartService.removePersonalInfoFromShoppingCart(SHOPPING_CART_ID);
        verify(shoppingCartRepository, times(1)).save(any());
    }


    @Test
    void shouldNotGetShoppingCartWhenNoShoppingCartFound() {
        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ShoppingCartException.class, () -> shoppingCartService.getShoppingCart(SHOPPING_CART_ID), "Unable to find the shopping cart");
    }

    @Test
    void shouldNotGetShoppingCartWhenStatusNotOpen() {
        prepareShoppingCart(ShoppingCartState.CHECKED_OUT);
        assertThrows(ShoppingCartException.class, () -> shoppingCartService.getShoppingCart(SHOPPING_CART_ID), "Shopping cart is not open");
    }


    private void prepareShoppingCart(ShoppingCartState shoppingCartState) {
        ShoppingCart shoppingCart = new ShoppingCart();
        List<Item> items = new ArrayList<>();
        items.add(prepareItem());
        shoppingCart.setItems(items);
        shoppingCart.setState(shoppingCartState);
        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(shoppingCart));
    }

    private void prepareProduct() {
        Product product = new Product(PRODUCT_ID, "Tomato", BigDecimal.valueOf(0, 20));
        when(productService.getProductById(anyLong())).thenReturn(Optional.of(product));
    }

    private Item prepareItem() {
        Product product = new Product(PRODUCT_ID, "Tomato", BigDecimal.valueOf(0, 20));
        return new Item(ITEM_ID, product, QUANTITY_OF_PRODUCT);
    }
}