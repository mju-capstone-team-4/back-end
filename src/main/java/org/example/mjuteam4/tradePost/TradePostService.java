package org.example.mjuteam4.tradePost;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.member.Member;
import org.example.mjuteam4.member.MemberService;
import org.example.mjuteam4.storage.StorageService;
import org.example.mjuteam4.tradePost.dto.request.TradePostRequest;
import org.example.mjuteam4.tradePost.entity.TradePost;
import org.example.mjuteam4.tradePost.exception.TradePostNotFound;
import org.example.mjuteam4.tradePostImage.TradePostImageService;
import org.example.mjuteam4.tradePostImage.entity.TradePostImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class TradePostService {
    private final TradePostRepository tradePostRepository;
    private final TradePostImageService tradePostImageService;
    private final MemberService memberService;
    private final StorageService storageService;

    public TradePost createTradePost(Long memberId, TradePostRequest tradePostRequest) {
        TradePost tradePost = TradePost.create(tradePostRequest);
        TradePostImage tradePostImage = tradePostImageService.createTradePostImage(tradePostRequest.getImage());
        tradePost.addTradePostImage(tradePostImage);
        Member member = memberService.findByMemberId(memberId);
        member.addTradePost(tradePost);
        return tradePostRepository.save(tradePost);
    }

    public Page<TradePost> tradePostList(Pageable pageable) {
        return tradePostRepository.findAll(pageable);
    }

    public TradePost tradePostDetail(Long tradePostId) {
        return tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
    }

    public TradePost modifyTradePost(Long tradePostId, TradePostRequest tradePostRequest) {
        // 수정 대상 질문 조회 후 제목, 내용 수정
        TradePost modifiedTradePost = tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new).modifyTradePost(tradePostRequest);
        // 이미지 수정
        // 이미지 변경 요청이 있다면, 기존 s3이미지 삭제하고 새로운 이미지 업로드
        MultipartFile requestedImage = tradePostRequest.getImage();
        if(requestedImage != null && !requestedImage.isEmpty()){

            String originalImageUrl = modifiedTradePost.getTradePostImage().getImageUrl();
            tradePostImageService.deleteImageService(originalImageUrl);

            TradePostImage tradePostImage = tradePostImageService.createTradePostImage(requestedImage);

            modifiedTradePost.setTradePostImage(tradePostImage);
        }

        return modifiedTradePost;
    }

    public void deleteTradePost(Long tradePostId) {
        // 1. s3 버킷에 있는 이미지 우선 삭제
        TradePost tradePost = tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
        String imageUrl = tradePost.getTradePostImage().getImageUrl();
        tradePostImageService.deleteImageService(imageUrl);

        // 2. db row 삭제
        tradePostRepository.deleteById(tradePostId);
    }

    public TradePost findTradePostById(Long tradePostId) {
        return tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
    }

}
