package gildongmu.trip.service;


import gildongmu.trip.application.UserAdapter;
import gildongmu.trip.client.S3Client;
import gildongmu.trip.domain.Image.entity.Image;
import gildongmu.trip.domain.bookmark.repository.BookmarkRepository;
import gildongmu.trip.domain.post.constant.MemberGender;
import gildongmu.trip.domain.post.constant.Status;
import gildongmu.trip.domain.post.entity.Post;
import gildongmu.trip.domain.post.repository.PostRepository;
import gildongmu.trip.domain.tag.entity.Tag;
import gildongmu.trip.dto.PostItem;
import gildongmu.trip.dto.TripDate;
import gildongmu.trip.dto.transfer.UserInfo;
import gildongmu.trip.dto.request.PostCreateRequest;
import gildongmu.trip.dto.request.PostUpdateRequest;
import gildongmu.trip.dto.request.RetrievingType;
import gildongmu.trip.dto.response.PostListResponse;
import gildongmu.trip.dto.response.PostResponse;
import gildongmu.trip.dto.response.PostSummaryResponse;
import gildongmu.trip.exception.PostException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static gildongmu.trip.exception.ErrorCode.POST_NOT_FOUND;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TagService tagService;
    private final ImageService imageService;
    private final S3Client s3Client;
    private final UserAdapter userAdapter;

    public PostListResponse findPosts(String keyword, String postFilter, String postSort, Pageable pageable, String token) {
        Page<Post> postPage = postRepository.findFilteredAndSortedPosts(keyword, postFilter, postSort, pageable);

        UserInfo user = userAdapter.getUserInfoFromToken(token);

        List<PostItem> postListItems = postPage.getContent().stream()
                .map(post -> mapToPostListItem(post, user))
                .collect(Collectors.toList());

        return new PostListResponse(
                postListItems,
                postPage.getPageable(),
                postPage.isFirst(),
                postPage.isLast(),
                postPage.getSize(),
                postPage.getNumber(),
                postPage.getSort(),
                postPage.getNumberOfElements(),
                postPage.isEmpty(),
                postPage.getTotalPages()
        );
    }

    private PostItem mapToPostListItem(Post post, UserInfo user) {
        List<Tag> tags = tagService.findTagListByPost(post);
        List<String> tagList = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());

        boolean myBookmark = checkBookmarkedByUser(user, post);

        long countOfBookmarks =
                post.getBookmarks() != null ? post.getBookmarks().size() : 0;
        UserInfo writer = userAdapter.getUserInfoFromId(post.getUserId());

        return new PostItem(
                post.getId(),
                post.getTitle(),
                writer.nickname(),
                post.getDestination(),
                TripDate.of(post.getStartDate(), post.getEndDate()),
                post.getParticipants(),
                post.getMemberGender().toString(),
                post.getContent(),
                post.getStatus().getCode(),
                tagList,
                post.getThumbnail(),
                (long) post.getComments().size(),
                countOfBookmarks,
                myBookmark
        );
    }

    private boolean checkBookmarkedByUser(UserInfo user, Post post) {
        return bookmarkRepository.existsByUserIdAndPost(user.id(), post);
    }

    public PostResponse findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Tag> tag = tagService.findTagListByPost(post);

        return PostResponse.from(post, tag, post.getImages());
    }

    public void createPost(PostCreateRequest postRequest, List<MultipartFile> images, String token) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);
        Post post = Post.builder()
                .userId(user.id())
                .title(postRequest.title())
                .content(postRequest.content())
                .destination(postRequest.destination())
                .startDate(postRequest.tripDate().startDate())
                .endDate(postRequest.tripDate().endDate())
                .memberGender(MemberGender.valueOf(postRequest.gender()))
                .participants(postRequest.numberOfPeople())
                .status(Status.OPEN)
                .build();

        List<Image> updatedImages = null;
        if (images != null) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = s3Client.upload(image);
                imageUrls.add(imageUrl);
            }

            updatedImages = imageService.saveImages(imageUrls, post);
            String thumbnail = updatedImages.get(0).getUrl();
            post.updateThumbnail(thumbnail);
        }

        postRepository.save(post);
        tagService.saveTag(post, postRequest.tag());
        // participantService.saveLeader(post, user);
    }

    public PostResponse updatePost(Long postId, List<MultipartFile> images, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        post.updateTitle(postUpdateRequest.title());
        post.updateContent(postUpdateRequest.content());
        post.updateDestination(postUpdateRequest.destination());
        post.updateStartDate(postUpdateRequest.tripDate().startDate());
        post.updateEndDate(postUpdateRequest.tripDate().endDate());
        post.updateGender(MemberGender.valueOf(postUpdateRequest.gender()));
        post.updateParticipants(postUpdateRequest.numberOfPeople());

        List<Image> existImages = imageService.findAllByPostId(postId);
        imageService.deleteAllImagesFromS3(existImages);

        List<Long> ids = imageService.findAllId(postId);
        imageService.deleteAllImagesFromDB(ids);


        List<Image> updatedImages = null;
        if (images != null) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = s3Client.upload(image);
                imageUrls.add(imageUrl);
            }
            updatedImages = imageService.saveImages(imageUrls, post);
            String thumbnail = updatedImages.get(0).getUrl();
            post.updateThumbnail(thumbnail);
        }

        tagService.deleteTag(post);
        tagService.saveTag(post, postUpdateRequest.tag());
        postRepository.save(post);

        return PostResponse.from(post, tagService.findTagListByPost(post), updatedImages);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Image> images = imageService.findAllByPostId(id);
        imageService.deleteAllImagesFromS3(images);

        tagService.deleteTag(post);
        postRepository.delete(post);
    }

    public Slice<PostItem> retrieveMyPosts(String token, String type, Pageable pageable) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);
        if (RetrievingType.LEADER.name().equals(type))
            return postRepository.findByUserIdOrderByStatusDesc(user.id(), pageable)
                    .map(post -> mapToPostListItem(post, user));
        return null;
//        return postRepository.findByParticipantUserOrderByStatusDesc(user.id(), pageable)
//                .map(post -> mapToPostListItem(post, user));
    }

    public PostSummaryResponse retrievePostSummary(String token, Long postId) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
//        int numberOfAccepted = roomRepository.findByPost(post)
//                .map(Room::getHeadcount)
//                .orElseGet(() -> 1);
//        return PostSummaryResponse.from(post, numberOfAccepted, user.id());
        return null;
    }

}
