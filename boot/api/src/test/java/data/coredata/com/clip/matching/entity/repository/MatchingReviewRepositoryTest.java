package data.coredata.com.clip.matching.entity.repository;

import com.clip.ApiApplication;
import com.clip.matching.entity.*;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.RandomMatchingReviewRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ContextConfiguration(classes = ApiApplication.class)
@DataJpaTest
public class MatchingReviewRepositoryTest {
    @Autowired
    RandomMatchingReviewRepository randomMatchingReviewRepository;
    @Autowired
    RandomMatchingRepository randomMatchingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    @DisplayName("유저는 동일한 모임에 리뷰를 중복 작성할 수 없다.")
    @Test
    void createReviewFail() {
        //given
        User user = userRepository.save(User.builder().build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());

        //when
        RandomMatchingReview randomMatchingReview = RandomMatchingReview.builder()
                .user(user)
                .randomMatching(randomMatching)
                .mood(Mood.UNSATISFIED)
                .positivePoints("test")
                .negativePoints("test")
                .build();
        randomMatchingReviewRepository.save(randomMatchingReview);

        //then
        RandomMatchingReview randomMatchingReview2 = RandomMatchingReview.builder()
                .user(user)
                .randomMatching(randomMatching)
                .mood(Mood.UNSATISFIED)
                .positivePoints("test")
                .negativePoints("test")
                .build();
        assertThatThrownBy(() -> {
            randomMatchingReviewRepository.save(randomMatchingReview2);
        }).isInstanceOf((DataIntegrityViolationException.class));
    }
}
