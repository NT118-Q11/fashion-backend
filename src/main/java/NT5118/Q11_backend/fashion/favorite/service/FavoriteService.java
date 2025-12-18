package NT5118.Q11_backend.fashion.favorite.service;

import NT5118.Q11_backend.fashion.favorite.dto.FavoriteResponse;
import java.util.List;

public interface FavoriteService {
    FavoriteResponse addFavorite(String userId, String productId);
    void removeFavorite(String userId, String productId);
    List<FavoriteResponse> getFavorites(String userId);
    boolean isFavorite(String userId, String productId);
}
