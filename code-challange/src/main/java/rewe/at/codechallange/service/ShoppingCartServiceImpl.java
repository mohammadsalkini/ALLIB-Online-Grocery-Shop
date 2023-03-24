package rewe.at.codechallange.service;

import org.springframework.stereotype.Service;
import rewe.at.codechallange.enums.ShoppingCartState;
import rewe.at.codechallange.exception.ProductException;
import rewe.at.codechallange.exception.ShoppingCartException;
import rewe.at.codechallange.model.Item;
import rewe.at.codechallange.model.PersonalInfo;
import rewe.at.codechallange.model.Product;
import rewe.at.codechallange.model.ShoppingCart;
import rewe.at.codechallange.repository.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    @Override
    public ShoppingCart createShoppingCart(Long productId, int quantity) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductException("Unable to find the product"));

        ShoppingCart shoppingCart = new ShoppingCart();
        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(quantity);
        shoppingCart.getItems().add(item);
        shoppingCart.setState(ShoppingCartState.OPEN);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart addItemToShoppingCart(Long shoppingCartId, Long productId, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductException("Unable to find the product"));

        boolean isItemExist = shoppingCart
                .getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .peek(item -> item.setQuantity(item.getQuantity() + quantity))
                .findFirst()
                .isPresent();

        if (!isItemExist) {
            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(quantity);
            shoppingCart.getItems().add(item);
        }
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeItemFromShoppingCart(Long shoppingCartId, Long itemId) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);
        shoppingCart.getItems().removeIf(item -> item.getId().equals(itemId));
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart addPersonalInfoToShoppingCart(Long shoppingCartId, PersonalInfo personalInfo) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);
        shoppingCart.setFirstName(personalInfo.getFirstName());
        shoppingCart.setLastName(personalInfo.getLastName());
        shoppingCart.setEmail(personalInfo.getEmail());
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removePersonalInfoFromShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);
        shoppingCart.setFirstName("");
        shoppingCart.setLastName("");
        shoppingCart.setEmail("");
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart checkoutShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);
        shoppingCart.setState(ShoppingCartState.CHECKED_OUT);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart openShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartId);
        shoppingCart.setState(ShoppingCartState.OPEN);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart mergeShoppingCarts(Long shoppingCartId1, Long shoppingCartId2) {
        ShoppingCart shoppingCart1 = shoppingCartRepository.findById(shoppingCartId1)
                .orElseThrow(() -> new ShoppingCartException("Unable to find shoppingCart with the id: " + shoppingCartId1));

        ShoppingCart shoppingCart2 = shoppingCartRepository.findById(shoppingCartId2)
                .orElseThrow(() -> new ShoppingCartException("Unable to find shoppingCart with the id: " + shoppingCartId2));

        if (shoppingCart1.getState() != ShoppingCartState.OPEN || shoppingCart2.getState() != ShoppingCartState.OPEN) {
            throw new ShoppingCartException("One or both of the shoppingCarts are not on the state OPEN");
        }

        shoppingCart2
                .getItems()
                .forEach(item2 -> {
                    boolean isItemExist = shoppingCart1
                            .getItems()
                            .stream()
                            .filter(item1 ->
                                    item1.getProduct().getId().equals(item2.getProduct().getId()))
                            .findFirst()
                            .map(item -> {
                                item.setQuantity(item.getQuantity() + item2.getQuantity());
                                return true;
                            })
                            .orElse(false);
                    if (!isItemExist) {
                        shoppingCart1.getItems().add(item2);
                    }
                });
        return shoppingCartRepository.save(shoppingCart1);
    }

    protected ShoppingCart getShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartException("Unable to find the shopping cart"));
        if (shoppingCart.getState() != ShoppingCartState.OPEN) {
            throw new ShoppingCartException("Shopping cart is not open");
        }
        return shoppingCart;
    }
}