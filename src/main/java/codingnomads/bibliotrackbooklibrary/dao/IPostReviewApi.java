package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.forms.ReviewForm;

public interface IPostReviewApi {

    boolean postReview(ReviewForm reviewForm);

}
