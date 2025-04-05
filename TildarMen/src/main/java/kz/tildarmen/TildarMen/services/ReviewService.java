package kz.tildarmen.TildarMen.services;

import kz.tildarmen.TildarMen.dto.ReviewDto;
import kz.tildarmen.TildarMen.mapper.ReviewMapper;
import kz.tildarmen.TildarMen.model.Review;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.ReviewRepository;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.CreateReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TranslatorService translatorService;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final TranslatorRepository translatorRepository;

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<ReviewDto> getAllTranslatorReview(Long translatorId) {
        Translator translator = translatorService.getTranslatorById(translatorId);
        return reviewMapper.toDtoList(reviewRepository.findAllByTranslator(translator));
    }

    public ReviewDto createTranslatorReview(Long translatorId, Long userId, CreateReviewRequest request) {
        Translator translator = translatorService.getTranslatorById(translatorId);
        translator.addRating(request.getRating());
        User user = userService.getUserById(userId);
        Review review = new Review();
        review.setComment(request.getComment());
        review.setUser(user);
        review.setCreationDate(LocalDateTime.now());
        review.setRating(request.getRating());
        review.setTranslator(translator);
        translatorRepository.save(translator);
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    public void deleteTranslatorReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        Translator translator = translatorService.getTranslatorById(review.getTranslator().getId());
        translator.removeRating(review.getRating());
        translatorRepository.save(translator);
        reviewRepository.deleteById(reviewId);
    }

}
