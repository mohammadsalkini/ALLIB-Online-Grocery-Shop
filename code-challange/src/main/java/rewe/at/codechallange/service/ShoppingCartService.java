package rewe.at.codechallange.service;

import rewe.at.codechallange.model.PersonalInfo;
import rewe.at.codechallange.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart createShoppingCart(Long productId, int quantity);
    ShoppingCart addItemToShoppingCart(Long shoppingCartId, Long productId, int quantity);
    ShoppingCart removeItemFromShoppingCart(Long shoppingCartId, Long itemId);
    ShoppingCart addPersonalInfoToShoppingCart(Long cartId, PersonalInfo personalInfo);
    ShoppingCart removePersonalInfoFromShoppingCart(Long shoppingCartId);
    ShoppingCart checkoutShoppingCart(Long shoppingCartId);
    ShoppingCart openShoppingCart(Long shoppingCartId);
    ShoppingCart mergeShoppingCarts(Long shoppingCartId1, Long shoppingCartId2);

}