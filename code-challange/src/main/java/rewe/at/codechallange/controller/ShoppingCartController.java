package rewe.at.codechallange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rewe.at.codechallange.model.PersonalInfo;
import rewe.at.codechallange.model.ShoppingCart;
import rewe.at.codechallange.service.ShoppingCartService;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    
    @PostMapping
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestParam Long productId, @RequestParam int quantity) {
        ShoppingCart shoppingCart = shoppingCartService.createShoppingCart(productId, quantity);
        return new ResponseEntity<>(shoppingCart, HttpStatus.CREATED);
    }

    @PostMapping("/{shoppingCartId}/items")
    public ResponseEntity<ShoppingCart> addItemToShoppingCart(@PathVariable Long shoppingCartId, @RequestParam Long productId, @RequestParam int quantity) {
        ShoppingCart shoppingCart = shoppingCartService.addItemToShoppingCart(shoppingCartId, productId, quantity);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/{shoppingCartId}/items/{itemId}")
    public ResponseEntity<ShoppingCart> removeItemFromShoppingCart(@PathVariable Long shoppingCartId, @PathVariable Long itemId) {
        ShoppingCart shoppingCart = shoppingCartService.removeItemFromShoppingCart(shoppingCartId, itemId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/personal-info")
    public ResponseEntity<ShoppingCart> addPersonalInfoToShoppingCart(@PathVariable Long shoppingCartId, @RequestBody PersonalInfo personalInfo) {
        ShoppingCart shoppingCart = shoppingCartService.addPersonalInfoToShoppingCart(shoppingCartId, personalInfo);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/{shoppingCartId}/personal-info")
    public ResponseEntity<ShoppingCart> removePersonalInfoFromShoppingCart(@PathVariable Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartService.removePersonalInfoFromShoppingCart(shoppingCartId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/checkout")
    public ResponseEntity<ShoppingCart> checkoutShoppingCart(@PathVariable Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartService.checkoutShoppingCart(shoppingCartId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/open")
    public ResponseEntity<ShoppingCart> openShoppingCart(@PathVariable Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartService.openShoppingCart(shoppingCartId);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/merge")
    public ResponseEntity<ShoppingCart> mergeShoppingCarts(@PathVariable Long shoppingCartId, @RequestParam Long shoppingCartId2) {
        ShoppingCart shoppingCart = shoppingCartService.mergeShoppingCarts(shoppingCartId, shoppingCartId2);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }
}