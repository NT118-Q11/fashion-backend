package NT5118.Q11_backend.fashion.favorite.service.implementations;

import NT5118.Q11_backend.fashion.favorite.dto.FavoriteResponse;
import NT5118.Q11_backend.fashion.favorite.model.Favorite;
import NT5118.Q11_backend.fashion.favorite.repository.FavoriteRepository;
import NT5118.Q11_backend.fashion.favorite.service.FavoriteService;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository; // To verify product exists and enrich response

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
    }

    @Override
    public FavoriteResponse addFavorite(String userId, String productId) {
        // Check if product exists
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }

        // Check if already favorited
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            return FavoriteResponse.fromFavorite(existing.get());
        }

        Favorite favorite = new Favorite(userId, productId);
        Favorite saved = favoriteRepository.save(favorite);
        return FavoriteResponse.fromFavorite(saved);
    }

    @Override
    public void removeFavorite(String userId, String productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<FavoriteResponse> getFavorites(String userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
            .map(fav -> {
                FavoriteResponse response = FavoriteResponse.fromFavorite(fav);
                // Enrich with product details
                productRepository.findById(fav.getProductId())
                    .ifPresent(response::setProduct);
                return response;
            })
            .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(String userId, String productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }
}
